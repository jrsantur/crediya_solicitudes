package pe.crediya.solicitud.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos de validacion de toekn")
public class UsuarioDto {
    String documento;
    String nombres;
    String apellidos;
    String salarioBase;
}
