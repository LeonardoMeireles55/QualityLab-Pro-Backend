package leonardo.labutilities.qualitylabpro.repository;

import leonardo.labutilities.qualitylabpro.model.HematologyAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HematologyRepository extends JpaRepository<HematologyAnalytics, Long> {
    boolean existsByName(String name);

    boolean existsByDateAndLevelAndName(String date, String level, String name);
}
