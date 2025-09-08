package pe.crediya.solicitud.model.solicitud;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenValidation {

    private boolean valido;
    private int codigoHttp;
    private String mensaje;
    private Map<String, Object> claims;
    private LocalDateTime fechaExpiracion;

}