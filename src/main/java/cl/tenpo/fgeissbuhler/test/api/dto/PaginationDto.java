package cl.tenpo.fgeissbuhler.test.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Estructura de información para paginación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto implements Serializable {

    @JsonProperty("pageNumber")
    private long pageNumber;
    @JsonProperty("pageSize")
    private long pageSize;
    @JsonProperty("totalPages")
    private long totalPages;
    @JsonProperty("totalRows")
    private long totalRows;
}
