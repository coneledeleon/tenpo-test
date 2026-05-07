package cl.tenpo.fgeissbuhler.test.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Respuesta genérica de error para Controller Advice
 */
@Data
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private String errorCode;
    private String errorMessage;
    private String datetime;
}
