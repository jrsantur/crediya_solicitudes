package pe.crediya.solicitud.model.solicitud;
import lombok.*;
//import lombok.NoArgsConstructor;
import pe.crediya.solicitud.model.solicitud.exception.ReglaDeNegocioException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private String id;
    private String documentoIdentidad;
    private double monto;
    private int plazoMeses;
    private double tasaInteres;
    private TipoPrestamo tipoPrestamo;
    @Builder.Default
    private EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE_REVISION;
    @Builder.Default
    private LocalDateTime creadaEn = LocalDateTime.now();
    private LocalDateTime updateAt;

    private Solicitud(String documentoIdentidad, double monto, int plazo, TipoPrestamo tipo) {
        this.documentoIdentidad = documentoIdentidad;
        this.monto = monto;
        this.plazoMeses = plazo;
        this.tipoPrestamo = tipo;
        this.estadoSolicitud = EstadoSolicitud.PENDIENTE_REVISION;
        this.creadaEn = LocalDateTime.now();
    }

    public Mono<Solicitud> validarDatos(Solicitud solicitud){
        if (solicitud.getDocumentoIdentidad() == null || solicitud.getDocumentoIdentidad().isBlank())
            return Mono.error(new ReglaDeNegocioException("documentoIdentidad es requerido"));
        if(solicitud.getMonto() <= 0)
            return Mono.error(new ReglaDeNegocioException("Monto debe ser mayor a 0"));
        if(solicitud.getPlazoMeses() <= 0)
            return Mono.error(new ReglaDeNegocioException("Plazo debe ser mayor a 0"));
        if(solicitud.getTipoPrestamo() == null)
            return Mono.error(new ReglaDeNegocioException("tipoPrestamo es requerido"));
        if(TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getMinMonto()> solicitud.getMonto() )
            return Mono.error(new ReglaDeNegocioException("monto debe ser mayor o igual a " + TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getMinMonto()));
        if(TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getMaxMonto()< solicitud.getMonto() )
            return Mono.error(new ReglaDeNegocioException("monto debe ser menor o igual a " + TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getMaxMonto()));
        if (!TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getPlazosPermitidos().contains(getPlazoMeses()))
            return Mono.error(new ReglaDeNegocioException("plazoMeses no permitido para el tipo de prestamo " + getTipoPrestamo().getDescripcion()));
        if(TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getTasaMinima() < solicitud.getTasaInteres() ||
                TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getTasaMaxima() > solicitud.getTasaInteres() ){
            return Mono.error(new ReglaDeNegocioException("Tasa de interes debe ser mayor que "+ TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getTasaMinima() +
                    " y menos qie "+TipoPrestamo.fromCodigo(getTipoPrestamo().getDescripcion()).getTasaMaxima()));
        }
        return Mono.just(solicitud);
    }
}
