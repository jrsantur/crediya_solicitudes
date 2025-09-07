package pe.crediya.solicitud.api.mapper;

import org.mapstruct.Mapper;
import pe.crediya.solicitud.api.dto.SolicitudRequest;
import pe.crediya.solicitud.api.dto.SolicitudResponse;
import pe.crediya.solicitud.model.solicitud.Solicitud;
import pe.crediya.solicitud.model.solicitud.TipoPrestamo;

@Mapper(componentModel = "spring")
public interface SolicitudMapper {
    Solicitud toDomain(SolicitudRequest solicitudDto);

    SolicitudResponse toResponse(Solicitud solicitudDomain);

    default TipoPrestamo mapTipoPrestamoFromString(String tipo) {
        return TipoPrestamo.fromCodigo(tipo);
    }
}
