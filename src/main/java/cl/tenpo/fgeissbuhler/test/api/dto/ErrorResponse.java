package cl.tenpo.fgeissbuhler.test.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Respuesta genérica de error")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    @Schema(description = "Código de error interno", example = "01")
    private String errorCode;
    
    @Schema(description = "Mensaje descriptivo del error", example = "Error en la solicitud: num1 must be greater than 0")
    private String errorMessage;
    
    @Schema(description = "Fecha y hora del error en formato ISO 8601", example = "2026-05-08T14:30:00")
    private String datetime;
}
