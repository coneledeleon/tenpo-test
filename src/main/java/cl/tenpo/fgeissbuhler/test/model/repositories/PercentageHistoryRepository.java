package cl.tenpo.fgeissbuhler.test.model.repositories;

import cl.tenpo.fgeissbuhler.test.model.entities.PercentageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de registros históricos de consultas a aplicación de porcentajes
 */
@Repository
public interface PercentageHistoryRepository extends JpaRepository<PercentageHistory, Long>{
    
}
