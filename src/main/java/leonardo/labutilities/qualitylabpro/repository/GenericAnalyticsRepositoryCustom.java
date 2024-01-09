package leonardo.labutilities.qualitylabpro.repository;

import leonardo.labutilities.qualitylabpro.domain.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfLevelsGenericRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface GenericAnalyticsRepositoryCustom extends JpaRepository<GenericAnalytics, Long> {
    boolean existsByName(String name);
    List<GenericAnalytics> findAllByName(Pageable pageable, String name);
    boolean existsByDateAndLevelAndName(String date, String level, String value);

    List<GenericAnalytics> findAllByNameOrderByDateAsc(String name);
    List<GenericAnalytics> findAllByNameOrderByDateDesc(String name);

    List<GenericAnalytics> findAllByLevel(String level);

    List<GenericAnalytics> findAllByNameAndLevel(Pageable pageable, String name, String Level);

    @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1 AND ga.level = ?2 AND ga.date BETWEEN ?3 AND ?4 ORDER BY ga.date ASC")
    List<GenericAnalytics> findAllByNameAndLevelAndDateBetween(String name, String level, String startDate, String endDate);

}

