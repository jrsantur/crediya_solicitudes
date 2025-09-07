package pe.crediya.solicitud.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de validacion de toekn")
public class TokenValidationResponse {
    private boolean valido;
    private int codigoHttp;
    private String mensaje;
    private Map<String, Object> claims;
    private LocalDateTime fechaExpiracion;

}
