package pe.crediya.solicitud.model.solicitud.exception;

public class MontoFueraDeRangoException extends ReglaDeNegocioException {
    public MontoFueraDeRangoException(String tipo, double min, double max){
        super("Monto fuera de rango para " + tipo + " ["+min+".."+max+"]");
    }
}