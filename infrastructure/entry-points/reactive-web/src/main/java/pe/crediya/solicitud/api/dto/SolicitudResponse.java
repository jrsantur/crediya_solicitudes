package pe.crediya.solicitud.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.crediya.solicitud.model.usuario.Usuario;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de una solicitud de préstamo")
public class SolicitudResponse {

    @Schema(description = "Cliente que solicita el prestamo")
    @JsonProperty("cliente")
    private UsuarioDto usuarioDto;

    @Schema(description = "Solicitud del cliente ")
    @JsonProperty("solicitud")
    private SolicitudDto solicitudDto;

    @Schema(description = "Estado actual de la solicitud", example = "PENDIENTE_REVISION")
    @JsonProperty("estado")
    private String estado;

    @Schema(description = "Fecha de creación de la solicitud")
    @JsonProperty("fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización")
    @JsonProperty("fechaActualizacion")
    private LocalDateTime fechaActualizacion;



}
