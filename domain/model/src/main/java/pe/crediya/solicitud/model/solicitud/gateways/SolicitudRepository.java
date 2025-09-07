package pe.crediya.solicitud.model.solicitud.gateways;

import pe.crediya.solicitud.model.solicitud.EstadoSolicitud;
import pe.crediya.solicitud.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface SolicitudRepository {
    Mono<Solicitud> guardarSolicitud(Solicitud s);

    Mono<Boolean> existeSolicitudActivaPorDocumento(String documentoIdentidad);

    Flux<Solicitud> obtenerSolicitudesPendientesRevision();

    public Flux<Solicitud> buscarPorEstados(Set<EstadoSolicitud> estados);
}
