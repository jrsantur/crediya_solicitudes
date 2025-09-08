package pe.crediya.solicitud.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.crediya.solicitud.api.dto.SolicitudRequest;
import pe.crediya.solicitud.api.dto.SolicitudResponse;
import pe.crediya.solicitud.api.dto.SolicitudRevisionRequestDto;
import pe.crediya.solicitud.api.mapper.SolicitudMapper;
import pe.crediya.solicitud.model.solicitud.EstadoSolicitud;
import pe.crediya.solicitud.model.solicitud.Solicitud;
import pe.crediya.solicitud.model.usuario.Usuario;
import pe.crediya.solicitud.usecase.auth.AuthUseCase;
import pe.crediya.solicitud.usecase.solicitud.SolicitudUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final SolicitudUseCase solicitudUseCase;
    private final AuthUseCase authUseCase;
    private final SolicitudMapper solicitudMapper;

    public Mono<ServerResponse> crearSolicitud(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SolicitudRequest.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body es requerido")))
                .flatMap(solicitudRequest -> {
                    var solicitud = solicitudMapper.toDomain(solicitudRequest);
                    return solicitudUseCase.guardarSolitud(solicitud);
                })
                .flatMap(solicitud -> {
                    var response = SolicitudResponse.builder()
                            .id(solicitud.getId())
                            .estado(EstadoSolicitud.fromCodigo(solicitud.getEstadoSolicitud().getDescripcion()).name())
                            .fechaCreacion(solicitud.getCreadaEn())
                            .fechaActualizacion(solicitud.getUpdateAt())
                            .build();
                    return ServerResponse.ok().bodyValue(response);
                })
                .doOnError(error -> log.error("Error: {}", error.getMessage()));
    }

    public Mono<ServerResponse> obtenerSolicitudPorRevision(ServerRequest serverRequest) {
        int pagina = serverRequest.queryParam("pagina").map(Integer::parseInt).orElse(0);
        int tamano = serverRequest.queryParam("tamano").map(Integer::parseInt).orElse(10);

        return solicitudUseCase.obtenerSolicitudesPendientesRevision( pagina, tamano) // Flux<SolicitudDto>
                .flatMap( solicitudes ->  {
                    authUseCase.obtenerUsuario(solicitudes.getDocumentoIdentidad())
                            .defaultIfEmpty(Usuario.builder().documento(solicitudes.getDocumentoIdentidad()).build())
                            .map(u ->
                })
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(list));
    }
}
