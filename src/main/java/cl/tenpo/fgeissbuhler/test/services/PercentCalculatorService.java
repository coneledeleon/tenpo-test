package cl.tenpo.fgeissbuhler.test.services;

import cl.tenpo.fgeissbuhler.test.api.dto.PercentResponseDto;
import java.math.BigDecimal;

/**
 * Servicio de cálculo de porcentajes
 */
public interface PercentCalculatorService {
    
    PercentResponseDto calculatePercentage(BigDecimal num1, BigDecimal num2);
}
