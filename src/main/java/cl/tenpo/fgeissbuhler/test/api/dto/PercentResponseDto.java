
package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Respuesta del cálculo de porcentaje adicional")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PercentResponseDto implements Serializable{
    
    @Schema(description = "Primer número ingresado", example = "100")
    @JsonProperty("num1")
    private BigDecimal num1;
    
    @Schema(description = "Segundo número ingresado", example = "200")
    @JsonProperty("num2")
    private BigDecimal num2;
    
    @Schema(description = "Porcentaje adicional aplicado (valor entre 0 y 1)", example = "0.15")
    @JsonProperty("appliedPercentage")
    private BigDecimal appliedPercentage;
    
    @Schema(description = "Resultado final de la operación", example = "330.0")
    @JsonProperty("result")
    private BigDecimal result;
    
    @Schema(description = "Fecha y hora de la operación en formato ISO 8601", example = "2026-05-08T14:30:00")
    @JsonProperty("datetime")
    private String datetime;
    
}
