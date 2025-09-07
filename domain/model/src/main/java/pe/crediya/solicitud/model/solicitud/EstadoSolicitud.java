package pe.crediya.solicitud.model.solicitud;

import pe.crediya.solicitud.model.solicitud.exception.ReglaDeNegocioException;

import java.util.Arrays;
import java.util.Set;

public enum EstadoSolicitud {
    PENDIENTE_REVISION("PENDIENTE_REVISION", "Pendiente de Revisión", true),
    APROBADA("APROBADA", "Aprobada", false),
    DESAPROBADA("DESAPROBADA", "Desaprobada", false),
    REVISION_MANUAL("REVISION_MANUAL", "Revision manual", true),
    DESEMBOLSADA("DESEMBOLSADA", "Desembolsada", false);

    private final String codigo;
    private final String descripcion;
    private final boolean puedeModificar; // Si se puede modificar la solicitud en este estado

    EstadoSolicitud(String codigo, String descripcion, boolean puedeModificar) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.puedeModificar = puedeModificar;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isPuedeModificar() {
        return puedeModificar;
    }

    public static EstadoSolicitud fromCodigo(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new ReglaDeNegocioException("Estado de préstamo no puede ser nulo o vacío");
        }

        String estadoLimpio = estado.trim().toUpperCase().replace(" ", "_");

        return Arrays.stream(values())
                .filter(e -> e.codigo.equals(estadoLimpio) ||
                        e.name().equals(estadoLimpio) ||
                        e.descripcion.equalsIgnoreCase(estado.trim()))
                .findFirst()
                .orElseThrow(() -> new ReglaDeNegocioException("Estado de préstamo no válido: " + estado));
    }

    // Estados válidos para transiciones
    public Set<EstadoSolicitud> getEstadosPermitidos() {
        return switch (this) {
            case PENDIENTE_REVISION -> Set.of(APROBADA, DESAPROBADA);
            case APROBADA -> Set.of(DESEMBOLSADA);
            case REVISION_MANUAL -> Set.of(APROBADA, DESAPROBADA);
            case DESAPROBADA, DESEMBOLSADA -> Set.of(); // Sin transiciones permitidas
        };
    }

    public static Set<EstadoSolicitud> getEstadosParaRevisionAsesor() {
        return Set.of(
                PENDIENTE_REVISION,
                DESAPROBADA,
                REVISION_MANUAL
        );
    }

    public boolean requiereRevisionAsesor() {
        return getEstadosParaRevisionAsesor().contains(this);
    }
}
