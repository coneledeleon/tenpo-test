package cl.tenpo.fgeissbuhler.test.services.impl;

import cl.tenpo.fgeissbuhler.test.services.ExternalPercentGeneratorService;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Mock para desarrollo
 * Esto debe ser reemplazado por el cliente a servicio externo que genera el porcentaje aplicable
 */
@Service
@Profile("dev")
public class ExternalPercentGeneratorMock implements ExternalPercentGeneratorService{

    /**
     * Se devuelve un valor entre 0 y 1, para efectos de desarrollo
     */
    @Override
    public BigDecimal getApplicablePercentage(BigDecimal num1, BigDecimal num2) {
        return BigDecimal.valueOf(Math.random()).round(new MathContext(2, RoundingMode.HALF_DOWN));
    }
    
    
}
