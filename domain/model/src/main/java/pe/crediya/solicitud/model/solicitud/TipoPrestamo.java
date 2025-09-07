package pe.crediya.solicitud.model.solicitud;

import lombok.Getter;
import pe.crediya.solicitud.model.solicitud.exception.ReglaDeNegocioException;

import java.util.Arrays;
import java.util.Set;

public enum TipoPrestamo {
    CONSUMO("Crédito de consumo",
            500,  50000,
            Set.of(6, 12, 24, 36) ,
             0.3, 0.8),
    HIPOTECARIO("Crédito hipotecario",
            10000, 1500000,
            Set.of(60, 120, 180, 240, 300) ,
            0.07, 0.15 ),
    VEHICULAR("Crédito vehicular",
            3000, 150000,
            Set.of(12, 24, 36, 48, 60),
            0.1, 0.16),
    PYME("Crédito para PyME",
            2000, 300000,
            Set.of(6, 12, 18, 24, 36),
            0.2, 0.4);

    @Getter
    public final String descripcion;
    @Getter
    public final double minMonto;
    @Getter
    public final double maxMonto;
    @Getter
    public final Set<Integer> plazosPermitidos;
    @Getter
    public final double tasaMinima;
    @Getter
    public final double tasaMaxima;

    TipoPrestamo(String desc, double min, double max, Set<Integer> plazos, double tasaMinima, double tasaMaxima) {
        this.descripcion = desc;
        this.minMonto = min;
        this.maxMonto = max;
        this.plazosPermitidos = plazos;
        this.tasaMinima = tasaMinima;
        this.tasaMaxima = tasaMaxima;
    }

    public static TipoPrestamo fromCodigo(String tipo) {
        return Arrays.stream(values())
                .filter(t -> t.name().equalsIgnoreCase(tipo) || t.descripcion.equalsIgnoreCase(tipo))
                .findFirst()
                .orElseThrow(() -> new ReglaDeNegocioException("Tipo de préstamo no válido: " + tipo));
    }

}
