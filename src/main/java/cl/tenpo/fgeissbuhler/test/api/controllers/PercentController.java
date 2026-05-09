package cl.tenpo.fgeissbuhler.test.api.controllers;

import cl.tenpo.fgeissbuhler.test.api.aspects.PersistLog;
import cl.tenpo.fgeissbuhler.test.api.dto.ErrorResponse;
import cl.tenpo.fgeissbuhler.test.api.dto.HistoryLogResponseDto;
import cl.tenpo.fgeissbuhler.test.api.dto.PercentResponseDto;
import cl.tenpo.fgeissbuhler.test.services.HistoryLogService;
import cl.tenpo.fgeissbuhler.test.services.PercentCalculatorService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de cálulo de porcentajes adicionales
 */
@Tag(name = "Percents", description = "Operaciones de cálculo de porcentaje adicional e historial")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/percents")
public class PercentController {

    private final PercentCalculatorService percentCalcService;
    private final HistoryLogService historyService;

    @Operation(
            summary = "Calcular porcentaje adicional",
            description = "Aplica un porcentaje adicional sobre la suma de dos números. Sujeto a rate limiting (3 solicitudes por minuto) y reintentos automáticos (3 intentos con espera de 2 segundos entre cada uno)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cálculo realizado exitosamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = PercentResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos (num1 o num2 no son positivos)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "429", description = "Demasiadas solicitudes (rate limit excedido)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(name = "Rate limit", value = "{\"errorCode\":\"02\",\"errorMessage\":\"Demasiadas solicitudes: rate limit exceeded\",\"datetime\":\"2026-05-08T14:30:00\"}"))),
        @ApiResponse(responseCode = "502", description = "Error en servicio externo después de reintentos",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(name = "Servicio externo fallido", value = "{\"errorCode\":\"03\",\"errorMessage\":\"Error en servicio externo: Error forzado\",\"datetime\":\"2026-05-08T14:30:00\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(name = "Error interno", value = "{\"errorCode\":\"99\",\"errorMessage\":\"Ocurrió un error interno del servidor.\",\"datetime\":\"2026-05-08T14:30:00\"}")))
    })
    @PersistLog
    @GetMapping
    @RateLimiter(name = "global")
    public ResponseEntity<PercentResponseDto> calculatePercentage(
            @Parameter(description = "Primer número (debe ser positivo)", required = true, example = "100")
            @RequestParam("num1") @Positive String num1,
            @Parameter(description = "Segundo número (debe ser positivo)", required = true, example = "200")
            @RequestParam("num2") @Positive String num2) {

        log.info("Se solicita aplicar porcentaje sobre {} y {}...", num1, num2);
        return ResponseEntity.ok().body(percentCalcService.calculatePercentage(
                new BigDecimal(num1), new BigDecimal(num2)));
    }

    @Operation(
            summary = "Obtener historial de solicitudes",
            description = "Retorna el historial de todas las solicitudes realizadas al endpoint de cálculo, con paginación. Página inicia en 1. Si no se especifican page y size, se usan valores por defecto: page=1, size=10."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial recuperado exitosamente",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = HistoryLogResponseDto.class))),
        @ApiResponse(responseCode = "204", description = "No hay registros en el historial (respuesta sin body)", content = @Content),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos (page o size no son positivos)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(name = "Error interno", value = "{\"errorCode\":\"99\",\"errorMessage\":\"Ocurrió un error interno del servidor.\",\"datetime\":\"2026-05-08T14:30:00\"}")))
    })
    @GetMapping("/history")
    public ResponseEntity<HistoryLogResponseDto> getRequestHistory(
            @Parameter(description = "Número de página (inicia en 1, opcional, default: 1)", example = "1")
            @RequestParam(name = "page", required = false) @Positive Integer page,
            @Parameter(description = "Cantidad de registros por página (opcional, default: 10)", example = "10")
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
