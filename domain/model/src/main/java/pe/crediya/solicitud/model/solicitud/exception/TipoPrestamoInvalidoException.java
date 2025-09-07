package pe.crediya.solicitud.model.solicitud.exception;

public class TipoPrestamoInvalidoException extends ReglaDeNegocioException {
    public TipoPrestamoInvalidoException(String tipo){
        super("Tipo no permitido: " + tipo);
    }
}