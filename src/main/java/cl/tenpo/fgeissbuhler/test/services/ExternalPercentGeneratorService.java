package cl.tenpo.fgeissbuhler.test.services;

import java.math.BigDecimal;

/**
 * Servicio que obtiene el valor porcentual a añadir
 */
public interface ExternalPercentGeneratorService {
    
    BigDecimal getApplicablePercentage(BigDecimal num1, BigDecimal num2);
}
