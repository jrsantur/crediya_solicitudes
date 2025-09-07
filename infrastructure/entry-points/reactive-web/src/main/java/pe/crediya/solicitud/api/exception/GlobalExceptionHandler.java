package pe.crediya.solicitud.api.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.WebExceptionHandler;
import pe.crediya.solicitud.api.dto.ErrorResponseDto;
import pe.crediya.solicitud.model.solicitud.exception.ReglaDeNegocioException;
import pe.crediya.solicitud.model.solicitud.exception.TipoPrestamoInvalidoException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) return Mono.error(ex);

        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().toString();

        logError(ex, method, path);

        ErrorResponseDto body = mapToError(ex, path);
        HttpStatus status = determineHttpStatus(body.getCodigo());

        var resp = exchange.getResponse();
        resp.setStatusCode(status);
        resp.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(body);
        } catch (Exception ser) {
            bytes = fallbackBytes(path);
        }
        return resp.writeWith(Mono.just(resp.bufferFactory().wrap(bytes)));
    }

    private ErrorResponseDto mapToError(Throwable ex, String path) {
        if (ex instanceof ReglaDeNegocioException e) {
            return ErrorResponseDto.builder()
                    .codigo("DOMAIN_VALIDATION_ERROR")
                    .mensaje("Error de validación de dominio")
                    .detalles(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(path).build();
        } else if (ex instanceof TipoPrestamoInvalidoException e) {
            return ErrorResponseDto.builder()
                    .codigo("TIPO_PRESTAMO_INVALIDO")
                    .mensaje("Tipo de préstamo no válido")
                    .detalles(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(path).build();
        } else if (ex instanceof ServerWebInputException e) {
            return ErrorResponseDto.builder()
                    .codigo("BAD_REQUEST")
                    .mensaje("Payload inválido")
                    .detalles(e.getReason())
                    .timestamp(LocalDateTime.now())
                    .path(path).build();
        }
        return ErrorResponseDto.builder()
                .codigo("INTERNAL_SERVER_ERROR")
                .mensaje("Error interno del servidor")
                .detalles("Ha ocurrido un error inesperado")
                .timestamp(LocalDateTime.now())
                .path(path).build();
    }

    private HttpStatus determineHttpStatus(String codigo) {
        return switch (codigo) {
            case "DOMAIN_VALIDATION_ERROR", "VALIDATION_ERROR", "REGLA_NEGOCIO_ERROR",
                 "BAD_REQUEST", "TIPO_PRESTAMO_INVALIDO" -> HttpStatus.BAD_REQUEST;
            case "SOLICITUD_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "OPERATION_NOT_ALLOWED" -> HttpStatus.CONFLICT;
            case "EXTERNAL_SERVICE_ERROR" -> HttpStatus.SERVICE_UNAVAILABLE;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private void logError(Throwable ex, String method, String path) {
        if (ex instanceof ReglaDeNegocioException || ex instanceof TipoPrestamoInvalidoException) {
            log.warn("Error de dominio en {} {}: {}", method, path, ex.getMessage());
        } else if (ex instanceof ServerWebInputException || ex instanceof IllegalArgumentException) {
            log.warn("Error de validación en {} {}: {}", method, path, ex.getMessage());
        } else {
            log.error("Error no controlado en {} {}: {}", method, path, ex.getMessage(), ex);
        }
    }

    private byte[] fallbackBytes(String path) {
        try {
            var fallback = Map.of(
                    "codigo", "INTERNAL_SERVER_ERROR",
                    "mensaje", "Error interno del servidor",
                    "detalles", "No se pudo serializar la respuesta de error",
                    "timestamp", LocalDateTime.now().toString(),
                    "path", path
            );
            return mapper.writeValueAsBytes(fallback);
        } catch (Exception ignore) {
            return "{\"codigo\":\"INTERNAL_SERVER_ERROR\",\"mensaje\":\"Error interno\"}".getBytes(StandardCharsets.UTF_8);
        }
    }
}
