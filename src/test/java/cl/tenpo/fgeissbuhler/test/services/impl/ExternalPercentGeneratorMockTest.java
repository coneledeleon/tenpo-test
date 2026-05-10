package cl.tenpo.fgeissbuhler.test.services.impl;

import cl.tenpo.fgeissbuhler.test.exceptions.ExtPercentServiceException;
import cl.tenpo.fgeissbuhler.test.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExternalPercentGeneratorMockTest {

    private final ExternalPercentGeneratorMock service = new ExternalPercentGeneratorMock();

    @Test
    void getApplicablePercentage_cuandoNum1EsNulo_lanzaInvalidArgumentException() {
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> service.getApplicablePercentage(null, BigDecimal.ONE)
        );
        assertEquals("Los parámetros de entrada no pueden ser nulos.", exception.getMessage());
    }

    @Test
    void getApplicablePercentage_cuandoNum2EsNulo_lanzaInvalidArgumentException() {
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> service.getApplicablePercentage(BigDecimal.ONE, null)
        );
        assertEquals("Los parámetros de entrada no pueden ser nulos.", exception.getMessage());
    }

    @Test
    void getApplicablePercentage_cuandoExitoso_retornaValorEntreCeroYUno() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("200");
        int validResults = 0;

        for (int i = 0; i < 100; i++) {
            try {
                BigDecimal result = service.getApplicablePercentage(num1, num2);
                assertTrue(BigDecimal.ZERO.compareTo(result) <= 0, "El valor debe ser >= 0");
                assertTrue(BigDecimal.ONE.compareTo(result) >= 0, "El valor debe ser <= 1");
                validResults++;
            } catch (ExtPercentServiceException e) {
                assertEquals("Error forzado", e.getMessage());
            }
        }

        assertTrue(validResults > 0, "Se esperaba al menos un resultado exitoso en 100 iteraciones");
    }

    @Test
    void getApplicablePercentage_cuandoFalla_lanzaExtPercentServiceException() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("200");
        int failures = 0;

        for (int i = 0; i < 100; i++) {
            try {
                service.getApplicablePercentage(num1, num2);
            } catch (ExtPercentServiceException e) {
                assertEquals("Error forzado", e.getMessage());
                failures++;
            }
        }

        assertTrue(failures > 0, "Se esperaba al menos un fallo en 100 iteraciones (50% de tasa de fallo)");
    }
}
