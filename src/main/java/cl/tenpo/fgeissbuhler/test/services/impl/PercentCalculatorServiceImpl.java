package cl.tenpo.fgeissbuhler.test.services.impl;

import cl.tenpo.fgeissbuhler.test.api.dto.PercentResponseDto;
import cl.tenpo.fgeissbuhler.test.services.ExternalPercentGeneratorService;
import cl.tenpo.fgeissbuhler.test.services.PercentCalculatorService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cl.tenpo.fgeissbuhler.test.exceptions.InvalidArgumentException;
import java.util.Objects;

/**
 * Servicio mock de cálculo de porcentaje para desarrollo Solo aplica para profile "dev"
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PercentCalculatorServiceImpl implements PercentCalculatorService {
    
    private final ExternalPercentGeneratorService percentGenService;

    /**
     * Para efectos de desarrollo, se retorna un valor aleatorio
     */
    @Override
    public PercentResponseDto calculatePercentage(BigDecimal num1, BigDecimal num2) {
        if(Objects.isNull(num1) || Objects.isNull(num2)){
            throw new InvalidArgumentException("Los parámetros de entrada no pueden ser nulos.");
        }
        
        final BigDecimal appliedPercent = percentGenService.getApplicablePercentage(num1, num2);
        
        if(Objects.isNull(appliedPercent) || BigDecimal.ZERO.compareTo(appliedPercent) == 1){
            throw new RuntimeException("Valor de porcentaje aplicado negativo. Valor no manejado");
        }
        
        final BigDecimal result = num1.add(num2)
                .multiply(BigDecimal.ONE.add(appliedPercent));
        log.info("Procentaje aplicado: {} | valor total: {}", appliedPercent, result);
        return new PercentResponseDto(num1, num2, appliedPercent, result, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

}
