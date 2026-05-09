package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Estructura de respuesta para servicio de consulta de log de solicitudes al endpoint principal
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLogResponseDto implements Serializable{
    
    @JsonProperty("history")
    private List<HistoryLogItem> history;
    @JsonProperty("pagination")
    private PaginationDto pagination;
}
