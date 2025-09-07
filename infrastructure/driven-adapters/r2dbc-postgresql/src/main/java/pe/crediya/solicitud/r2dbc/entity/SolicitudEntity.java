package pe.crediya.solicitud.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitud")
public class SolicitudEntity {

    @Id
    private String id;

    @Column("documento_identidad_cliente")
    private String documentoIdentidad;

    @Column("monto")
    private double monto;

    @Column("plazo_meses")
    private Integer plazoMeses;

    @Column("tipo_prestamo")
    private String tipoPrestamo;

    @Column("estado")
    private String estado;

    @Column("create_at")
    private LocalDateTime fechaCreacion;

    @Column("update_at")
    private LocalDateTime fechaActualizacion;

}
