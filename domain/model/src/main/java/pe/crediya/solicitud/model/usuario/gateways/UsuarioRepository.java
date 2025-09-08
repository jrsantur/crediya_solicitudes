package pe.crediya.solicitud.model.usuario.gateways;

import pe.crediya.solicitud.model.solicitud.TokenValidation;
import pe.crediya.solicitud.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<TokenValidation> validarToken(String token) ;

    Mono<Usuario> obtenerUsuario(String documento);
}
