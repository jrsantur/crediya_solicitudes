package pe.crediya.solicitud.usecase.auth;

import lombok.RequiredArgsConstructor;
import pe.crediya.solicitud.model.solicitud.TokenValidation;
import pe.crediya.solicitud.model.usuario.Usuario;
import pe.crediya.solicitud.model.usuario.gateways.UsuarioRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {
    private final UsuarioRepository authRepository;

    public Mono<TokenValidation> validarToken(String token) {
        return authRepository.validarToken(token);
    }

    public Mono<Usuario> obtenerUsuario(String documento){
        return authRepository.obtenerUsuario(documento);
    }
}
