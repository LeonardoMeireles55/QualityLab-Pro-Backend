package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.analytics.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytics, Long> {
    boolean existsByName(String name);
}
