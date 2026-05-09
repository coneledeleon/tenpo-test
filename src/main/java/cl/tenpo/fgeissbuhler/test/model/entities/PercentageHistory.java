package cl.tenpo.fgeissbuhler.test.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Entidad que maneja los registros históricos de consultas de porcentajes a la API
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "percentage_history")
public class PercentageHistory {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    
    @Column(name = "endpoint", nullable = false, length = 500)
    private String endpoint;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "params")
    private String params;
    
    @Column(name = "response", nullable = false, length = 2000)
    private String response;
    
    @Column(name = "duration_ms")
    private Long duration;

    public PercentageHistory(String endpoint, String params, String response, Long duration) {
        this.endpoint = endpoint;
        this.params = params;
        this.response = response;
        this.duration = duration;
    }
}
