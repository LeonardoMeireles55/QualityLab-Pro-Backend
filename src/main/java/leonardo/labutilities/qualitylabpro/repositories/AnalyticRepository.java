package leonardo.labutilities.qualitylabpro.repositories;

import leonardo.labutilities.qualitylabpro.domain.entitys.Analytics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticRepository extends JpaRepository<Analytics, Long> {
    boolean existsByName(String name);
    List<Analytics> findAllByName(Pageable pageable, String name);
}
