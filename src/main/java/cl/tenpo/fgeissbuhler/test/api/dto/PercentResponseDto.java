
package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO Respuesta consulta de porcentaje sobre montos
 */
@Data
@AllArgsConstructor
public class PercentResponseDto implements Serializable{
    
    @JsonProperty("num1")
    private BigDecimal num1;
    @JsonProperty("num2")
    private BigDecimal num2;
    @JsonProperty("appliedPercentage")
    private BigDecimal appliedPercentage;
    @JsonProperty("result")
    private BigDecimal result;
    @JsonProperty("datetime")
    private String datetime;
    
}
