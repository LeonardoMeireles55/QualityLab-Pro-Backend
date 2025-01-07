package leonardo.labutilities.qualitylabpro.repositories;

import java.time.LocalDateTime;
import java.util.List;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenericAnalyticsRepository extends JpaRepository<GenericAnalytics, Long> {
        boolean existsByName(String name);

        @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1")
        List<GenericValuesRecord> findAllByName(String name, Pageable pageable);

        boolean existsByDateAndLevelAndName(LocalDateTime date, String level, String value);

        @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1 AND ga.level = ?2")
        List<GenericValuesRecord> findAllByNameAndLevel(Pageable pageable, String name,
                        String level);

        @Query("SELECT ga FROM generic_analytics ga WHERE ga.name IN (?1) AND ga.date BETWEEN ?2 AND ?3")
        List<GenericValuesRecord> findAllByNameInAndDateBetween(List<String> names,
                        LocalDateTime startDate, LocalDateTime endDate);

        @Query("SELECT ga FROM generic_analytics ga WHERE ga.name IN (?1) ORDER BY ga.date ASC")
        List<GenericValuesRecord> findAllByNameIn(List<String> names, Pageable pageable);

        @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1 AND ga.level = ?2 AND ga.date BETWEEN ?3 AND ?4 ORDER BY ga.date ASC")
        List<GenericValuesRecord> findAllByNameAndLevelAndDateBetween(String name, String level,
                        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

        @Query("SELECT ga FROM generic_analytics ga WHERE ga.date BETWEEN ?1 AND ?2 ORDER BY ga.date DESC")
        List<GenericValuesRecord> findAllByDateBetween(LocalDateTime startDate,
                        LocalDateTime endDate);

        @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1 AND ga.date BETWEEN ?2 AND ?3 GROUP BY ga.level, ga.id ORDER BY ga.date ASC")
        List<GenericValuesRecord> findAllByNameAndDateBetweenGroupByLevel(String name,
                        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
