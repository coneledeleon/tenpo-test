package cl.tenpo.fgeissbuhler.test.api.aspects;

import cl.tenpo.fgeissbuhler.test.api.dto.PercentResponseDto;
import cl.tenpo.fgeissbuhler.test.services.HistoryLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Aspecto para loging de solicitud y respuesta
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
@Component("requestResponseLogAspect")
public class RequestResponseLogAspect {
    
    private final HistoryLogService historyService;
    
    @Around("@annotation(cl.tenpo.fgeissbuhler.test.api.aspects.PersistLog)")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        
        String methodPath = "desconocido";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            methodPath = attributes.getRequest().getRequestURL().toString();
        }
        
        try {
            log.info("Ejecutando la solicitud...");            
            Object result = joinPoint.proceed();
            
            persistLog(methodPath,
                    null,
                    (new ObjectMapper()).writeValueAsString(((ResponseEntity<PercentResponseDto>)result).getBody()),
                    System.currentTimeMillis() - start);
            
            return result;
        } catch (Throwable ex) {
            log.error("Ocurrió un error al procesar la solicitud: ", ex);
            throw ex;
        }
    }
    
    @Async
    private void persistLog(String requestPath, Map<String, Object> requestParams, String response, long duration) {
        historyService.persistLog(requestPath, requestParams, response, duration);
        log.info("Solicitud logeada correctamente");
    }
}
