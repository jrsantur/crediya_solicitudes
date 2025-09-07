package pe.crediya.solicitud.model.solicitud.exception;

import java.util.Set;

public class PlazoNoPermitidoException extends ReglaDeNegocioException {
    public PlazoNoPermitidoException(String tipo, int plazo, Set<Integer> permitidos){
        super("Plazo "+plazo+" no permitido para " + tipo + ". Permitidos: " + permitidos);
    }
}
