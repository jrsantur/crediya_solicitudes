package pe.crediya.solicitud.usecase.solicitud;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.crediya.solicitud.model.solicitud.Solicitud;
import pe.crediya.solicitud.model.solicitud.exception.ReglaDeNegocioException;
import pe.crediya.solicitud.model.solicitud.gateways.SolicitudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;

    public Mono<Solicitud> guardarSolitud(Solicitud solicitud) {
        return solicitud
                .validarDatos(solicitud)
                .flatMap(solicitudRepository::guardarSolicitud);
    }

    public Flux<Solicitud> obtenerSolicitudesPendientesRevision(int pagina,int tamaño ){
        return solicitudRepository.obtenerSolicitudesPendientesRevision()
                .skip((long) pagina*tamaño )
                .take(tamaño);
    }
}
