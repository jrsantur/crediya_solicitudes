package pe.crediya.solicitud.model.solicitud.exception;

public class ReglaDeNegocioException extends RuntimeException {
    public ReglaDeNegocioException(String message) {
        super(message);
    }
    public ReglaDeNegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
