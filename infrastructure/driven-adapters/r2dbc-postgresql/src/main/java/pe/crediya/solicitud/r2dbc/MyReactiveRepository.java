package pe.crediya.solicitud.r2dbc;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.crediya.solicitud.r2dbc.entity.SolicitudEntity;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MyReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Long>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    Flux<SolicitudEntity> findByEstadoInOrderByFechaCreacionAsc(List<String> estados);


    @Query("SELECT * FROM solicitudes_prestamo " +
            "WHERE estado IN ('PENDIENTE_REVISION', 'RECHAZADA', 'REVISION_MANUAL') " +
            "ORDER BY fecha_creacion ASC")
    Flux<SolicitudEntity> findSolicitudesPendientesRevisionAsesor();

}
