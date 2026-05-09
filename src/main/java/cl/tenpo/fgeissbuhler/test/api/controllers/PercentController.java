package cl.tenpo.fgeissbuhler.test.api.controllers;

import cl.tenpo.fgeissbuhler.test.api.aspects.PersistLog;
import cl.tenpo.fgeissbuhler.test.api.dto.HistoryLogResponseDto;
import cl.tenpo.fgeissbuhler.test.api.dto.PercentResponseDto;
import cl.tenpo.fgeissbuhler.test.services.HistoryLogService;
import cl.tenpo.fgeissbuhler.test.services.PercentCalculatorService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de cálulo de porcentajes adicionales
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/percents")
public class PercentController {

    private final PercentCalculatorService percentCalcService;
    private final HistoryLogService historyService;

    @PersistLog
    @GetMapping("/{num1}/{num2}")
    @RateLimiter(name = "global")
    public ResponseEntity<PercentResponseDto> calculatePercentage(
            @PathVariable("num1") @NotEmpty @Positive String num1,
            @PathVariable("num2") @NotEmpty @Positive String num2) {

        log.info("Se solicita aplicar porcentaje sobre {} y {}...", num1, num2);
        return ResponseEntity.ok().body(percentCalcService.calculatePercentage(
                new BigDecimal(num1), new BigDecimal(num2)));
    }

    @GetMapping("/history")
    public ResponseEntity<HistoryLogResponseDto> getRequestHistory(
            @RequestParam(name = "page", required = false) @Positive Integer page,
            @RequestParam(name = "size", required = false) @Positive Integer size) {

        log.info("Solicitando información de historial (page: {} / size: {})", page, size);

        HistoryLogResponseDto history = historyService.getHistoryLog(page, size);

        if (history.getHistory().isEmpty()) {
            log.info("La solicitud de historial no encontró registros");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.ok().body(history);
        }
    }

}
