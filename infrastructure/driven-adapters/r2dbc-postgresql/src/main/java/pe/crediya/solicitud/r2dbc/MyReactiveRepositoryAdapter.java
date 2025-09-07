package pe.crediya.solicitud.r2dbc;

import pe.crediya.solicitud.model.solicitud.EstadoSolicitud;
import pe.crediya.solicitud.model.solicitud.Solicitud;
import pe.crediya.solicitud.model.solicitud.exception.ReglaDeNegocioException;
import pe.crediya.solicitud.model.solicitud.gateways.SolicitudRepository;
import pe.crediya.solicitud.r2dbc.entity.SolicitudEntity;
import pe.crediya.solicitud.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud/* change for domain model */,
        SolicitudEntity/* change for adapter model */,
    Long,
    MyReactiveRepository
>  implements SolicitudRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Solicitud.class/* change for domain model */));
    }

    @Override
    public Mono<Solicitud> guardarSolicitud(Solicitud solicitud) {
        return Mono.fromCallable( () -> {
                return SolicitudEntity.builder()
                        .documentoIdentidad(solicitud.getDocumentoIdentidad())
                        .monto(solicitud.getMonto())
                        .plazoMeses(solicitud.getPlazoMeses())
                        .tipoPrestamo(solicitud.getTipoPrestamo().getDescripcion())
                        .estado(solicitud.getEstadoSolicitud().name())
                        .fechaCreacion(solicitud.getCreadaEn())
                        .build();
            }
        ).flatMap(repository::save)
        .map( entity -> {
            return Solicitud.builder()
                    .id(entity.getId())
                    .estadoSolicitud(EstadoSolicitud.fromCodigo(entity.getEstado()))
                    .creadaEn(entity.getFechaCreacion())
                    .updateAt(entity.getFechaActualizacion()).build();
        });
    }

    @Override
    public Mono<Boolean> existeSolicitudActivaPorDocumento(String documentoIdentidad) {
        return null;
    }

    @Override
    public Flux<Solicitud> obtenerSolicitudesPendientesRevision() {
        return buscarPorEstados(EstadoSolicitud.getEstadosParaRevisionAsesor());
    }

    @Override
    public Flux<Solicitud> buscarPorEstados(Set<EstadoSolicitud> estados) {

        if (estados == null || estados.isEmpty()) {
            return Flux.error(new ReglaDeNegocioException("Estados no pueden ser nulos o vacíos"));
        }
        List<String> estadosString = estados.stream()
                .map(Enum::name)
                .toList();
        return repository.findByEstadoInOrderByFechaCreacionAsc(estadosString)
                .map(this::toEntity);
    }
}
