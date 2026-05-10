package cl.tenpo.fgeissbuhler.test.api.handlers;

import cl.tenpo.fgeissbuhler.test.api.aspects.PersistLog;
import cl.tenpo.fgeissbuhler.test.api.dto.ErrorResponse;
import cl.tenpo.fgeissbuhler.test.exceptions.ExtPercentServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import cl.tenpo.fgeissbuhler.test.exceptions.InvalidArgumentException;

/**
 * Gestor global de excepciones
 */
@Slf4j
@PersistLog
@ControllerAdvice(basePackages = {"cl.tenpo.fgeissbuhler.test.api.controllers"})
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        log.error("Error en solicitud: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("01", String.format("Error en la solicitud: %s", ex.getMessage()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM--dd'T'HH:mm:ss"))));
    }
    
    
    @ExceptionHandler({RequestNotPermitted.class})
    public ResponseEntity<ErrorResponse> handleRateLimit(RuntimeException ex) {
        log.error("Error en solicitud: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorResponse("02", String.format("Demasiadas solicitudes: %s", ex.getMessage()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM--dd'T'HH:mm:ss"))));
    }
      
    @ExceptionHandler({ExtPercentServiceException.class})
    public ResponseEntity<ErrorResponse> handleRetryFailed(RuntimeException ex) {
        log.error("Error en solicitud: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY)
                .body(new ErrorResponse("03", String.format("Error en servicio externo: %s", ex.getMessage()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM--dd'T'HH:mm:ss"))));
    }
    
    @ExceptionHandler({InvalidArgumentException.class})
    public ResponseEntity<ErrorResponse> handleMethodValidationError(RuntimeException ex) {
        log.error("Error en solicitud: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErrorResponse("04", String.format("Error interno de validación: %s", ex.getMessage()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM--dd'T'HH:mm:ss"))));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleUnespectedError(Exception ex) {
        log.error("Error interno del servidor: {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("99", "Ocurrió un error interno del servidor.",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM--dd'T'HH:mm:ss"))));
    }
}
