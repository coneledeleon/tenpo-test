package cl.tenpo.fgeissbuhler.test.services.impl;

import cl.tenpo.fgeissbuhler.test.api.dto.HistoryLogResponseDto;
import cl.tenpo.fgeissbuhler.test.model.entities.PercentageHistory;
import cl.tenpo.fgeissbuhler.test.model.repositories.PercentageHistoryRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryLogServiceImplTest {

    @Mock
    private PercentageHistoryRepository historyRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private HistoryLogServiceImpl historyService;

    private final LocalDateTime now = LocalDateTime.of(2026, 5, 9, 12, 0, 0);

    @Test
    void persistLog_cuandoSePersiste_entidadNoTieneIdYCreated() {
        doAnswer(invocation -> {
            PercentageHistory entity = invocation.getArgument(0);
            return new PercentageHistory(1L, now, entity.getEndpoint(), entity.getParams(),
                    entity.getStatusCode(), entity.getResponse(), entity.getDuration());
        }).when(historyRepository).save(any(PercentageHistory.class));

        historyService.persistLog("/api/v1/percents", "{\"num1\":\"100\"}", 200,
                "{\"result\":\"345.0\"}", 150L);

        ArgumentCaptor<PercentageHistory> captor = ArgumentCaptor.forClass(PercentageHistory.class);
        verify(historyRepository).save(captor.capture());
        PercentageHistory captured = captor.getValue();

        assertNull(captured.getId(), "El ID debe ser null antes de persistir");
        assertNull(captured.getCreated(), "El created debe ser null antes de persistir");
        assertEquals("/api/v1/percents", captured.getEndpoint());
        assertEquals("{\"num1\":\"100\"}", captured.getParams());
        assertEquals(200, captured.getStatusCode());
        assertEquals("{\"result\":\"345.0\"}", captured.getResponse());
        assertEquals(150L, captured.getDuration());
    }

    @Test
    void getHistoryLog_cuandoHayDatos_retornaHistorialCorrecto() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        PercentageHistory entity = new PercentageHistory(1L, now, "/api/v1/percents",
                "{\"num1\":\"100\",\"num2\":\"200\"}", 200, "{\"result\":\"345.0\"}", 150L);
        Page<PercentageHistory> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(Map.of("result", "345.0"))
                .thenReturn(Map.of("num1", "100", "num2", "200"));

        HistoryLogResponseDto response = historyService.getHistoryLog(1, 10);

        assertNotNull(response);
        assertEquals(1, response.getHistory().size());
        assertEquals("2026-05-09T12:00:00", response.getHistory().getFirst().getCreated());
        assertEquals("/api/v1/percents", response.getHistory().getFirst().getRequestUrl());
        assertEquals(Map.of("num1", "100", "num2", "200"),
                response.getHistory().getFirst().getParams());
        assertEquals(200, response.getHistory().getFirst().getStatusCode());
        assertEquals(Map.of("result", "345.0"), response.getHistory().getFirst().getResponse());
        assertEquals(150L, response.getHistory().getFirst().getDuration());
        assertEquals(1, response.getPagination().getPageNumber());
        assertEquals(10, response.getPagination().getPageSize());
        assertEquals(1, response.getPagination().getTotalPages());
        assertEquals(1, response.getPagination().getTotalRows());
    }

    @Test
    void getHistoryLog_cuandoNoHayDatos_retornaHistorialVacio() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PercentageHistory> page = new PageImpl<>(List.of(), pageable, 0);

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);

        HistoryLogResponseDto response = historyService.getHistoryLog(1, 10);

        assertNotNull(response);
        assertTrue(response.getHistory().isEmpty());
        assertEquals(1, response.getPagination().getPageNumber());
        assertEquals(10, response.getPagination().getPageSize());
        assertEquals(0, response.getPagination().getTotalPages());
        assertEquals(0, response.getPagination().getTotalRows());
    }

    @Test
    void getHistoryLog_cuandoPageYSizeNulos_usaValoresPorDefecto() {
        PercentageHistory entity = new PercentageHistory(1L, now, "/api/v1/percents",
                "{\"num1\":\"100\"}", 200, "{\"result\":\"345.0\"}", 150L);
        Page<PercentageHistory> page = new PageImpl<>(List.of(entity));

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);

        HistoryLogResponseDto response = historyService.getHistoryLog(null, null);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(historyRepository).findAll(captor.capture());

        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(10, captor.getValue().getPageSize());
        assertEquals(1, response.getHistory().size());
    }

    @Test
    void getHistoryLog_cuandoErrorParseoJSON_paramsYRespNull() throws Exception {
        PercentageHistory entity = new PercentageHistory(1L, now, "/api/v1/percents",
                "{\"num1\":\"100\"}", 200, "{\"result\":\"345.0\"}", 150L);
        Page<PercentageHistory> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.readValue(anyString(), any(TypeReference.class)))
                .thenThrow(new JsonParseException((com.fasterxml.jackson.core.JsonParser) null,
                        "Error al parsear"));

        HistoryLogResponseDto response = historyService.getHistoryLog(1, 10);

        assertNotNull(response);
        assertEquals(1, response.getHistory().size());
        assertNull(response.getHistory().getFirst().getParams());
        assertNull(response.getHistory().getFirst().getResponse());
    }

    @Test
    void getHistoryLog_cuandoPaginaExcedeRegistros_retornaVacio() {
        Pageable pageable = PageRequest.of(9, 10);
        Page<PercentageHistory> page = new PageImpl<>(List.of(), pageable, 20);

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);

        HistoryLogResponseDto response = historyService.getHistoryLog(10, 10);

        assertNotNull(response);
        assertTrue(response.getHistory().isEmpty());
        assertEquals(10, response.getPagination().getPageNumber());
        assertEquals(10, response.getPagination().getPageSize());
        assertEquals(2, response.getPagination().getTotalPages());
        assertEquals(20, response.getPagination().getTotalRows());
    }

    @Test
    void getHistoryLog_cuando20RegistrosPagina3Size5_retornaCorrecto() throws Exception {
        List<PercentageHistory> content = List.of(
                new PercentageHistory(11L, now, "/api/v1/percents", "{}", 200, "{}", 100L),
                new PercentageHistory(12L, now, "/api/v1/percents", "{}", 200, "{}", 100L),
                new PercentageHistory(13L, now, "/api/v1/percents", "{}", 200, "{}", 100L),
                new PercentageHistory(14L, now, "/api/v1/percents", "{}", 200, "{}", 100L),
                new PercentageHistory(15L, now, "/api/v1/percents", "{}", 200, "{}", 100L)
        );
        Pageable pageable = PageRequest.of(2, 5);
        Page<PercentageHistory> page = new PageImpl<>(content, pageable, 20);

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);
        doReturn(Map.of()).when(mapper).readValue(anyString(), any(TypeReference.class));

        HistoryLogResponseDto response = historyService.getHistoryLog(3, 5);

        assertNotNull(response);
        assertEquals(5, response.getHistory().size());
        assertEquals(3, response.getPagination().getPageNumber());
        assertEquals(5, response.getPagination().getPageSize());
        assertEquals(4, response.getPagination().getTotalPages());
        assertEquals(20, response.getPagination().getTotalRows());
    }

    @Test
    void getHistoryLog_cuandoSoloPageViene_usaSizeDefault() {
        Page<PercentageHistory> page = new PageImpl<>(List.of(), PageRequest.of(1, 10), 0);

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);

        historyService.getHistoryLog(2, null);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(historyRepository).findAll(captor.capture());

        assertEquals(1, captor.getValue().getPageNumber());
        assertEquals(10, captor.getValue().getPageSize());
    }

    @Test
    void getHistoryLog_cuandoSoloSizeViene_usaPageDefault() {
        Page<PercentageHistory> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);

        when(historyRepository.findAll(any(Pageable.class))).thenReturn(page);

        historyService.getHistoryLog(null, 20);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(historyRepository).findAll(captor.capture());

        assertEquals(0, captor.getValue().getPageNumber());
        assertEquals(20, captor.getValue().getPageSize());
    }

    @Test
    void getHistoryLog_cuandoPageInferiorACero_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> historyService.getHistoryLog(-1, 10));
    }

    @Test
    void getHistoryLog_cuandoSizeInferiorACero_lanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> historyService.getHistoryLog(1, -5));
    }
}
