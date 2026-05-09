package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Información de paginación")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto implements Serializable {

    @Schema(description = "Número de página actual (inicia en 1)", example = "1")
    @JsonProperty("pageNumber")
    private long pageNumber;
    
    @Schema(description = "Cantidad de registros por página", example = "10")
    @JsonProperty("pageSize")
    private long pageSize;
    
    @Schema(description = "Total de páginas disponibles", example = "5")
    @JsonProperty("totalPages")
    private long totalPages;
    
    @Schema(description = "Total de registros en la base de datos", example = "50")
    @JsonProperty("totalRows")
    private long totalRows;
}
