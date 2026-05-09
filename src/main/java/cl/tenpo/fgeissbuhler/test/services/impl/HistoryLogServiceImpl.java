package cl.tenpo.fgeissbuhler.test.services.impl;

import cl.tenpo.fgeissbuhler.test.api.dto.HistoryLogItem;
import cl.tenpo.fgeissbuhler.test.api.dto.HistoryLogResponseDto;
import cl.tenpo.fgeissbuhler.test.api.dto.PaginationDto;
import cl.tenpo.fgeissbuhler.test.model.entities.PercentageHistory;
import cl.tenpo.fgeissbuhler.test.model.repositories.PercentageHistoryRepository;
import cl.tenpo.fgeissbuhler.test.services.HistoryLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Gestion de registros históricos de solicitudes;
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryLogServiceImpl implements HistoryLogService {

    private final PercentageHistoryRepository historyRepository;
    private final ObjectMapper mapper;

    @Override
    public void persistLog(String requestPath, String requestParams,Integer status, String response, long duration) {
        historyRepository.save(new PercentageHistory(requestPath, requestParams, status, response, duration));
    }

    @Override
    public HistoryLogResponseDto getHistoryLog(Integer page, Integer size) {

        // En caso de que alguno de los parámetros no venga, se asumen valores de paginación por defecto
        page = Objects.isNull(page) ? 0 : page - 1; // se asume la paginación a nivel de usuario comienza en 1
        size = Objects.isNull(size) ? 10 : size;
        Pageable pagination = PageRequest.of(page, size);

        Page<PercentageHistory> pagedHistory = historyRepository.findAll(pagination);
        List<HistoryLogItem> history = new ArrayList();
        if (!pagedHistory.getContent().isEmpty()) {
            pagedHistory.getContent().forEach(item -> {
                Object resp = null;
                Map<String, Object> prms = null;

                try {
                    resp = mapper.readValue(item.getResponse(), new TypeReference<Map<String, Object>>() {});
                    prms = mapper.readValue(item.getParams(), new TypeReference<Map<String, Object>>() {});
                } catch (JsonProcessingException ex) {
                    log.error("No se pudo parsear una respuesta...", ex);
                }
                history.add(new HistoryLogItem(
                        item.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
                        item.getEndpoint(),
                        prms,
                        item.getStatusCode(),
                        resp,
                        item.getDuration()));
            });
        }

        HistoryLogResponseDto response = new HistoryLogResponseDto();
        response.setHistory(history);
        response.setPagination(new PaginationDto(
                pagedHistory.getNumber() + 1,
                pagedHistory.getSize(),
                pagedHistory.getTotalPages(),
                pagedHistory.getTotalElements()));

        return response;
    }

}
