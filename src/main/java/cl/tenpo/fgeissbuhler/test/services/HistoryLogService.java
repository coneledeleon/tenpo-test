package cl.tenpo.fgeissbuhler.test.services;

import cl.tenpo.fgeissbuhler.test.api.dto.HistoryLogResponseDto;

/**
 * Servicio de gestión de registros históricos de solicitudes
 */
public interface HistoryLogService {
    
    void persistLog(String requestPath, String requestParams, String response, long duration);
    
    HistoryLogResponseDto getHistoryLog(Integer page, Integer size);
}
