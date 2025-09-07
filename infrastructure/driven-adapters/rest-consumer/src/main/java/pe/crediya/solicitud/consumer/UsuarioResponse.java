package pe.crediya.solicitud.consumer;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UsuarioResponse {

    private String documentoIdentidad;
    private String nombre;
    private String apellidos;
    private String salarioBase;

}