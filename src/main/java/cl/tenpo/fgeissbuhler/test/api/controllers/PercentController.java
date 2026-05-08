package cl.tenpo.fgeissbuhler.test.api.controllers;

import cl.tenpo.fgeissbuhler.test.api.aspects.PersistLog;
import cl.tenpo.fgeissbuhler.test.api.dto.PercentResponseDto;
import cl.tenpo.fgeissbuhler.test.services.PercentCalculatorService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

/**
 * Controlador de cálulo de porcentajes adicionales
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RateLimiter(name = "global")
@RequestMapping("/api/v1/percents")
public class PercentController {
    
    private final PercentCalculatorService percentCalcService;
    
    @GetMapping("/{num1}/{num2}")
    @PersistLog
    public ResponseEntity<PercentResponseDto> calculatePercentage(
            @PathVariable("num1") @NotEmpty @Positive String num1, 
            @PathVariable("num2") @NotEmpty @Positive String num2){
        
        log.info("Se solicita aplicar porcentaje sobre {} y {}...", num1, num2);
        return ResponseEntity.ok().body(percentCalcService.calculatePercentage(
                new BigDecimal(num1), new BigDecimal(num2)));
    }

}
