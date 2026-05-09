package cl.tenpo.fgeissbuhler.test.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Respuesta genérica de error para Controller Advice
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private String errorCode;
    private String errorMessage;
    private String datetime;
}
