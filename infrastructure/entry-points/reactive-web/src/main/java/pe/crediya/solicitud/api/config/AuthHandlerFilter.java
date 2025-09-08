package pe.crediya.solicitud.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.crediya.solicitud.api.dto.ErrorResponseDto;
import pe.crediya.solicitud.api.exception.AuthServiceException;
import pe.crediya.solicitud.api.exception.TokenNotFoundException;
import pe.crediya.solicitud.usecase.auth.AuthUseCase;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthHandlerFilter {

    private final AuthUseCase authUseCase;

    public HandlerFunction<ServerResponse> requireAuth(String rol, HandlerFunction<ServerResponse> handler) {
        return request -> {
            String path = request.path();

            return extraerToken(request)
                    .flatMap(authUseCase::validarToken)
                    .flatMap(result -> {
                        if (!result.isValido()){
                            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(new ErrorResponseDto("UNAUTHORIZED", HttpStatus.UNAUTHORIZED + result.getMensaje(), "Servicio no autorizado", LocalDateTime.now(), "/api/v1/solicitudes"));
                        }

                        String rolUsuario = result.getClaims().get("rol").toString();
                        if (rolUsuario == null || !rolUsuario.equalsIgnoreCase(rol)) {
                            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(new ErrorResponseDto("UNAUTHORIZED", HttpStatus.UNAUTHORIZED + result.getMensaje(), "Servicio no autorizado", LocalDateTime.now(), "/api/v1/solicitudes"));
                        }

                        ServerRequest requestConUsuario = ServerRequest.create(
                                request.exchange(),
                                request.messageReaders()
                        );

                        return handler.handle(requestConUsuario);
                    })
                    .onErrorResume(AuthServiceException.class, ex -> {
                        log.error("Error comunicándose con servicio de auth", ex);
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponseDto("UNAUTHORIZED", HttpStatus.INTERNAL_SERVER_ERROR.toString() , "Servicio no autorizado", LocalDateTime.now(), "/api/v1/solicitudes"));
                        //return crearRespuestaError("Error interno de autenticación", HttpStatus.INTERNAL_SERVER_ERROR);
                    })
                    .onErrorResume(TokenNotFoundException.class, ex -> {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponseDto("UNAUTHORIZED", HttpStatus.UNAUTHORIZED.toString(), "Servicio no autorizado", LocalDateTime.now(), "/api/v1/solicitudes"));
                        //return crearRespuestaError("Token no encontrado", HttpStatus.UNAUTHORIZED);
                    });

        };
    }

    private Mono<String> extraerToken(ServerRequest request) {
        return request.headers().header(HttpHeaders.AUTHORIZATION)
                .stream()
                .findFirst()
                .filter(header -> header.startsWith("Bearer "))
                .map(Mono::just)
                .orElse(Mono.error(new TokenNotFoundException("Token no encontrado")));
    }
}
