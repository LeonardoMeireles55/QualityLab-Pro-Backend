package leonardo.labutilities.qualitylabpro.repository;

import leonardo.labutilities.qualitylabpro.domain.entitys.GenericAnalytics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenericAnalyticsRepositoryCustom extends JpaRepository<GenericAnalytics, Long> {

    boolean existsByName(String name);

    Optional<List<GenericAnalytics>> findAllByName(Pageable pageable, String name);

    boolean existsByDateAndLevelAndName(String date, String level, String value);

    Optional<List<GenericAnalytics>> findAllByNameOrderByDateAsc(String name);

    Optional<List<GenericAnalytics>> findAllByNameOrderByDateDesc(String name);

    Optional<List<GenericAnalytics>> findAllByLevel(String level);

    Optional<List<GenericAnalytics>> findAllByNameAndLevel(Pageable pageable, String name, String level);

    @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1 AND ga.level = ?2 AND ga.date BETWEEN ?3 AND ?4 ORDER BY ga.date ASC")
    Optional<List<GenericAnalytics>> findAllByNameAndLevelAndDateBetween(String name, String level, String startDate, String endDate);

    @Query("SELECT ga FROM generic_analytics ga WHERE ga.date BETWEEN ?1 AND ?2 ORDER BY ga.date ASC")
    Optional<List<GenericAnalytics>> findAllByDateBetween(String startDate, String endDate);

}

