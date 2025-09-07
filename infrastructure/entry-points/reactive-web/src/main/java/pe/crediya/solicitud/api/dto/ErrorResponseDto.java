package pe.crediya.solicitud.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar para errores")
public class ErrorResponseDto {

    @Schema(description = "Código de error", example = "VALIDATION_ERROR")
    @JsonProperty("codigo")
    private String codigo;

    @Schema(description = "Mensaje de error", example = "Datos de entrada inválidos")
    @JsonProperty("mensaje")
    private String mensaje;

    @Schema(description = "Detalles adicionales del error")
    @JsonProperty("detalles")
    private String detalles;

    @Schema(description = "Timestamp del error")
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Path donde ocurrió el error", example = "/api/v1/solicitud")
    @JsonProperty("path")
    private String path;
}
