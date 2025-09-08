package pe.crediya.solicitud.model.usuario;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Usuario {
    long id;
    private String documento;
    private String nombre;
    private String apellidos;
    private String email;
    private LocalDateTime fechaCreacion;
}
