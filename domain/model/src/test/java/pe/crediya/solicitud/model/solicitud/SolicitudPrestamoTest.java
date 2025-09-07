package pe.crediya.solicitud.model.solicitud;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pe.crediya.solicitud.model.solicitud.exception.ReglaDeNegocioException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SolicitudPrestamo Domain Model Tests")
class SolicitudPrestamoTest {
    private static final String DOCUMENTO_VALIDO = "12345678901";
    private static final double MONTO_VALIDO = 5000000;
    private static final Integer PLAZO_VALIDO = 24;
    private static final TipoPrestamo TIPO_VALIDO = TipoPrestamo.CONSUMO;

    @Test
    @DisplayName("Debe crear solicitud válida con todos los campos obligatorios")
    void debeCrearSolicitudValida() {
        // Given - When
        Solicitud solicitud = Solicitud.builder()
                .documentoIdentidad(DOCUMENTO_VALIDO)
                .monto(MONTO_VALIDO)
                .plazoMeses(PLAZO_VALIDO)
                .tipoPrestamo(TIPO_VALIDO)
                .build();

        // Then
        assertAll(
                () -> assertEquals(DOCUMENTO_VALIDO, solicitud.getDocumentoIdentidad()),
                () -> assertEquals(MONTO_VALIDO, solicitud.getMonto()),
                () -> assertEquals(PLAZO_VALIDO, solicitud.getPlazoMeses()),
                () -> assertEquals(TIPO_VALIDO, solicitud.getTipoPrestamo()),
                () -> assertEquals(EstadoSolicitud.PENDIENTE_REVISION, solicitud.getEstadoSolicitud()),
                () -> assertNotNull(solicitud.getCreadaEn()),
                () -> assertNotNull(solicitud.getUpdateAt())
        );
    }

    @Test
    @DisplayName("Debe asignar estado por defecto PENDIENTE_REVISION")
    void debeAsignarEstadoPorDefecto() {
        // Given - When
        Solicitud solicitud = Solicitud.builder()
                .documentoIdentidad(DOCUMENTO_VALIDO)
                .monto(MONTO_VALIDO)
                .plazoMeses(PLAZO_VALIDO)
                .tipoPrestamo(TIPO_VALIDO)
                .build();

        // Then
        assertEquals(EstadoSolicitud.PENDIENTE_REVISION, solicitud.getEstadoSolicitud());
    }

    @Test
    @DisplayName("Debe asignar fechas automáticamente si no se proporcionan")
    void debeAsignarFechasAutomaticamente() {
        // Given
        LocalDateTime antes = LocalDateTime.now();

        // When
        Solicitud solicitud = Solicitud.builder()
                .documentoIdentidad(DOCUMENTO_VALIDO)
                .monto(MONTO_VALIDO)
                .plazoMeses(PLAZO_VALIDO)
                .tipoPrestamo(TIPO_VALIDO)
                .build();

        LocalDateTime despues = LocalDateTime.now();

        // Then
        assertAll(
                () -> assertTrue(solicitud.getCreadaEn().isAfter(antes) || solicitud.getCreadaEn().isEqual(antes)),
                () -> assertTrue(solicitud.getCreadaEn().isBefore(despues) || solicitud.getCreadaEn().isEqual(despues)),
                () -> assertNotNull(solicitud.getUpdateAt())
        );
    }




    @Test
    @DisplayName("Debe fallar con documento nulo")
    void debeFallarConDocumentoNulo() {
        // Given - When - Then
        ReglaDeNegocioException exception = assertThrows(ReglaDeNegocioException.class, () ->
                Solicitud.builder()
                        .documentoIdentidad(null)
                        .monto(MONTO_VALIDO)
                        .plazoMeses(PLAZO_VALIDO)
                        .tipoPrestamo(TIPO_VALIDO)
                        .build()
        );

        assertEquals("El documento de identidad es obligatorio", exception.getMessage());
    }

    @Test
    @DisplayName("Debe fallar con documento vacío")
    void debeFallarConDocumentoVacio() {
        // Given - When - Then
        ReglaDeNegocioException exception = assertThrows(ReglaDeNegocioException.class, () ->
                Solicitud.builder()
                        .documentoIdentidad("")
                        .monto(MONTO_VALIDO)
                        .plazoMeses(PLAZO_VALIDO)
                        .tipoPrestamo(TIPO_VALIDO)
                        .build()
        );

        assertEquals("El documento de identidad es obligatorio", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567", "1234567890123456", "abc12345678", "123-456-789"})
    @DisplayName("Debe fallar con documentos inválidos")
    void debeFallarConDocumentosInvalidos(String documentoInvalido) {
        // Given - When - Then
        ReglaDeNegocioException exception = assertThrows(ReglaDeNegocioException.class, () ->
                Solicitud.builder()
                        .documentoIdentidad(documentoInvalido)
                        .monto(MONTO_VALIDO)
                        .plazoMeses(PLAZO_VALIDO)
                        .tipoPrestamo(TIPO_VALIDO)
                        .build()
        );

        assertEquals("El documento de identidad debe contener entre 8 y 15 dígitos numéricos", exception.getMessage());
    }
}
