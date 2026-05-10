package cl.tenpo.fgeissbuhler.test.services.impl;

import cl.tenpo.fgeissbuhler.test.api.dto.PercentResponseDto;
import cl.tenpo.fgeissbuhler.test.exceptions.InvalidArgumentException;
import cl.tenpo.fgeissbuhler.test.services.ExternalPercentGeneratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PercentCalculatorServiceImplTest {

    @Mock
    private ExternalPercentGeneratorService percentGenService;

    @InjectMocks
    private PercentCalculatorServiceImpl percentCalculatorService;

    @Test
    void calculatePercentage_cuandoInputValido_retornaResultadoCorrecto() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("200");
        BigDecimal mockPercent = new BigDecimal("0.15");

        when(percentGenService.getApplicablePercentage(num1, num2)).thenReturn(mockPercent);

        PercentResponseDto result = percentCalculatorService.calculatePercentage(num1, num2);

        assertNotNull(result);
        assertEquals(num1, result.getNum1());
        assertEquals(num2, result.getNum2());
        assertEquals(mockPercent, result.getAppliedPercentage());
        assertEquals(new BigDecimal("345.00"), result.getResult());
        assertNotNull(result.getDatetime());

        verify(percentGenService).getApplicablePercentage(num1, num2);
    }

    @Test
    void calculatePercentage_cuandoPorcentajeCero_retornaSuma() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("200");
        BigDecimal mockPercent = BigDecimal.ZERO;

        when(percentGenService.getApplicablePercentage(num1, num2)).thenReturn(mockPercent);

        PercentResponseDto result = percentCalculatorService.calculatePercentage(num1, num2);

        assertNotNull(result);
        assertEquals(num1, result.getNum1());
        assertEquals(num2, result.getNum2());
        assertEquals(mockPercent, result.getAppliedPercentage());
        assertEquals(new BigDecimal("300"), result.getResult());

        verify(percentGenService).getApplicablePercentage(num1, num2);
    }

    @Test
    void calculatePercentage_cuandoNum1EsNulo_lanzaInvalidArgumentException() {
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> percentCalculatorService.calculatePercentage(null, new BigDecimal("200"))
        );

        assertEquals("Los parámetros de entrada no pueden ser nulos.", exception.getMessage());
        verifyNoInteractions(percentGenService);
    }

    @Test
    void calculatePercentage_cuandoNum2EsNulo_lanzaInvalidArgumentException() {
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> percentCalculatorService.calculatePercentage(new BigDecimal("100"), null)
        );

        assertEquals("Los parámetros de entrada no pueden ser nulos.", exception.getMessage());
        verifyNoInteractions(percentGenService);
    }

    @Test
    void calculatePercentage_cuandoPorcentajeEsNulo_lanzaRuntimeException() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("200");

        when(percentGenService.getApplicablePercentage(num1, num2)).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> percentCalculatorService.calculatePercentage(num1, num2)
        );

        assertEquals("Valor de porcentaje aplicado negativo. Valor no manejado", exception.getMessage());
        verify(percentGenService).getApplicablePercentage(num1, num2);
    }

    @Test
    void calculatePercentage_cuandoPorcentajeEsNegativo_lanzaRuntimeException() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("200");
        BigDecimal mockPercent = new BigDecimal("-0.1");

        when(percentGenService.getApplicablePercentage(num1, num2)).thenReturn(mockPercent);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> percentCalculatorService.calculatePercentage(num1, num2)
        );

        assertEquals("Valor de porcentaje aplicado negativo. Valor no manejado", exception.getMessage());
        verify(percentGenService).getApplicablePercentage(num1, num2);
    }
}
