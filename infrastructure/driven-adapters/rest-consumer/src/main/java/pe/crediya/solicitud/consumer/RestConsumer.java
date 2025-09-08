package pe.crediya.solicitud.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.crediya.solicitud.consumer.exception.AuthServiceException;
import pe.crediya.solicitud.model.solicitud.TokenValidation;
import pe.crediya.solicitud.model.usuario.Usuario;
import pe.crediya.solicitud.model.usuario.gateways.UsuarioRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;


@Service
@RequiredArgsConstructor
public class RestConsumer implements UsuarioRepository {

    @Qualifier("validatoken")
    private final WebClient clientToken;

    @Qualifier("datosUsuario")
    private final WebClient clientUsuario;
    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
    @CircuitBreaker(name = "testGet" /*, fallbackMethod = "testGetOk"*/)
    public Mono<TokenValidation> testGet() {
        return clientToken
                .get()
                .retrieve()
                .bodyToMono(TokenValidation.class);
    }

// Possible fallback method
//    public Mono<String> testGetOk(Exception ignored) {
//        return client
//                .get() // TODO: change for another endpoint or destination
//                .retrieve()
//                .bodyToMono(String.class);
//    }
    /*
    @CircuitBreaker(name = "testPost")
    public Mono<TokenValidation> testPost() {
        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();
        return client
                .post()
                .body(Mono.just(request), ObjectRequest.class)
                .retrieve()
                .bodyToMono(TokenValidation.class);
    }
     */

    @Override
    public Mono<TokenValidation> validarToken(String token) {
        return clientToken.
                get()
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(TokenValidation.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(WebClientResponseException.Unauthorized.class, ex -> {
                    return Mono.just(new TokenValidation(false, 401, "Token no autorizado", null, null));
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    return Mono.just(new TokenValidation(false, ex.getStatusCode().value(),
                            "Error HTTP: " + ex.getStatusText(), null, null));
                })
                .onErrorResume(TimeoutException.class, ex -> {
                    return Mono.just(new TokenValidation(false, 500, "Timeout", null, null));
                })
                .onErrorResume(Exception.class, ex -> {
                    return Mono.just(new TokenValidation(false, 500, "Error inesperado", null, null));
                });
    }

    @Override
    public Mono<Usuario> obtenerUsuario(String documento) {
        return clientUsuario.get()
                .retrieve()
                .onStatus(s -> s.value() == 404, rsp -> Mono.error(new RuntimeException("404")))
                .bodyToMono(Usuario.class)
                .onErrorResume(ex -> ex.getMessage().contains("404"), e -> Mono.empty())
                .timeout(Duration.ofSeconds(5));
    }

}
