package cl.tenpo.fgeissbuhler.test.services.impl;

import cl.tenpo.fgeissbuhler.test.exceptions.ExtPercentServiceException;
import cl.tenpo.fgeissbuhler.test.services.ExternalPercentGeneratorService;
import io.github.resilience4j.retry.annotation.Retry;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Mock para desarrollo
 * Esto debe ser reemplazado por el cliente a servicio externo que genera el porcentaje aplicable
 */
@Slf4j
@Service
@Profile("dev")
public class ExternalPercentGeneratorMock implements ExternalPercentGeneratorService{

    /**
     * Se devuelve un valor entre 0 y 1, para efectos de desarrollo
     */
    @Retry(name = "percentService")
    @Override
    public BigDecimal getApplicablePercentage(BigDecimal num1, BigDecimal num2) {
        // Se fuerza una tasa de fallo del 50%, para probar Retry
        if(Math.random() > 0.7){
            log.info("Esto es un error forzado en el servicio mock");
            throw new ExtPercentServiceException("Error forzado");
        }
        return BigDecimal.valueOf(Math.random()).round(new MathContext(2, RoundingMode.HALF_DOWN));
    }
    
    
}
