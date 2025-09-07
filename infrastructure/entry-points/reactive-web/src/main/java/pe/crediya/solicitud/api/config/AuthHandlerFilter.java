package pe.crediya.solicitud.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.crediya.solicitud.api.exception.TokenNotFoundException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthHandlerFilter {

    public HandlerFunction<ServerResponse> requireRole(String rol, HandlerFunction<ServerResponse> handler) {
        return request -> {
            String path = request.path();

        }
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
