package cl.tenpo.fgeissbuhler.test.api.handlers;

import cl.tenpo.fgeissbuhler.test.api.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Manejador global de excepciones
 */
@Slf4j
@ControllerAdvice(basePackages = {"cl.tenpo.fgeissbuhler.test.api.controllers"})
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleError(RuntimeException ex) {
        log.error("Error en solicitud: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("01", String.format("Error en la solicitud: %s", ex.getMessage()),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM--dd'T'HH:mm:ss"))));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleError(Exception ex) {
        log.error("Error interno inesperado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("99", "Ocurrió un error interno del servidor",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM--dd'T'HH:mm:ss"))));
    }
}
