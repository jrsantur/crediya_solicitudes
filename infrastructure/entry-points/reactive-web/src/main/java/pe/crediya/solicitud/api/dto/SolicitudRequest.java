package pe.crediya.solicitud.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear una solicitud de préstamo")
public class SolicitudRequest {
    @NotBlank(message = "El documento de identidad es obligatorio")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "El documento debe contener entre 8 y 15 dígitos")
    @Schema(description = "Documento de identidad del cliente", example = "12345678901")
    @JsonProperty("documentoIdentidad")
    private String documentoIdentidad;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "100000", message = "El monto mínimo es $100,000")
    @DecimalMax(value = "50000000", message = "El monto máximo es $50,000,000")
    @Digits(integer = 10, fraction = 2, message = "Formato de monto inválido")
    @Schema(description = "Monto solicitado del préstamo", example = "5000000.00")
    @JsonProperty("monto")
    private double monto;


    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 6, message = "El plazo mínimo es 6 meses")
    @Max(value = 60, message = "El plazo máximo es 60 meses")
    @Schema(description = "Plazo del préstamo en meses", example = "24")
    @JsonProperty("plazoMeses")
    private Integer plazoMeses;

    @NotBlank(message = "El tipo de préstamo es obligatorio")
    @Schema(description = "Tipo de préstamo solicitado",
            example = "PERSONAL",
            allowableValues = {"PERSONAL", "VEHICULAR", "HIPOTECARIO", "COMERCIAL", "EDUCATIVO"})
    @JsonProperty("tipoPrestamo")
    private String tipoPrestamo;
}
