package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Respuesta con el historial de solicitudes realizadas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLogResponseDto implements Serializable{
    
    @Schema(description = "Lista de registros del historial", implementation = HistoryLogItem.class)
    @JsonProperty("history")
    private List<HistoryLogItem> history;
    
    @Schema(description = "Información de paginación", implementation = PaginationDto.class)
    @JsonProperty("pagination")
    private PaginationDto pagination;
}
