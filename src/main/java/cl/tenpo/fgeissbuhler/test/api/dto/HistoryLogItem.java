package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Item individual del historial de solicitudes",
        example = "{\"created\":\"2026-05-08T14:30:00\",\"requestUrl\":\"/api/v1/percents\",\"params\":{\"num1\":\"100\",\"num2\":\"200\"},\"response\":{\"num1\":100,\"num2\":200,\"appliedPercentage\":0.15,\"result\":345,\"datetime\":\"2026-05-08T14:30:00\"},\"duration\":1523}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLogItem implements Serializable {

    @Schema(description = "Fecha y hora de la solicitud en formato ISO 8601", example = "2026-05-08T14:30:00")
    @JsonProperty("created")
    private String created;

    @Schema(description = "URL del endpoint solicitado", example = "/api/v1/percents")
    @JsonProperty("requestUrl")
    private String requestUrl;

    @Schema(description = "Parámetros enviados en la solicitud")
    @JsonProperty("params")
    private Map<String, Object> params;
    
    @Schema(description = "Código HTTP respondido en la solicitud")
    @JsonProperty("statusCode")
    private Integer statusCode;

    @Schema(description = "Respuesta obtenida en formato JSON. Puede ser PercentResponseDto (éxito) o ErrorResponse (error)",
            oneOf = {PercentResponseDto.class, ErrorResponse.class})
    @JsonProperty("response")
    private Object response;

    @Schema(description = "Duración de la solicitud en milisegundos", example = "1523")
    @JsonProperty("duration")
    private Long duration;

}
