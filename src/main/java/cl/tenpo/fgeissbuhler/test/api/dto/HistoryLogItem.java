package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Estructura de respuesta de cada item de la lista de registros del historial
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLogItem implements Serializable {

    @JsonProperty("created")
    private String created;
    @JsonProperty("requestUtl")
    private String requestUtl;
    @JsonProperty("params")
    private Map<String, Object> params;
    @JsonProperty("response")
    private PercentResponseDto response;
    @JsonProperty("duration")
    private Long duration;

}
