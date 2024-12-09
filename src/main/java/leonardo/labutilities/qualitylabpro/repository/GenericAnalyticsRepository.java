package leonardo.labutilities.qualitylabpro.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;

public interface GenericAnalyticsRepository extends JpaRepository<GenericAnalytics, Long> {

    boolean existsByName(String name);

    List<GenericValuesRecord> findAllByName(Pageable pageable, String name);

    boolean existsByDateAndLevelAndName(String date, String level, String value);

//    List<GenericValuesRecord> findAllByNameOrderByDate(String name, Sort sort);
//
//    List<GenericValuesRecord> findAllByNameOrderByDateAsc(String name);
//
//    List<GenericValuesRecord> findAllByNameOrderByDateDesc(String name);
//
//    List<GenericValuesRecord> findAllByLevel(String level);

    @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1 AND ga.level = ?2")
    List<GenericValuesRecord> findAllByNameAndLevel(Pageable pageable, String name, String level);

    @Query("SELECT ga FROM generic_analytics ga WHERE ga.name IN (?1) AND ga.date BETWEEN ?2 AND ?3 ORDER BY ga.date ASC")
    List<GenericValuesRecord> findAllByNameInAndDateBetween(
            List<String> names,
            String startDate,
            String endDate,
            Pageable pageable);

    @Query("SELECT ga FROM generic_analytics ga WHERE ga.name = ?1 AND ga.level = ?2 AND ga.date BETWEEN ?3 AND ?4")
    List<GenericValuesRecord> findAllByNameAndLevelAndDateBetween
            (String name, String level, String startDate, String endDate, Pageable pageable);

    @Query("SELECT ga FROM generic_analytics ga WHERE ga.date BETWEEN ?1 AND ?2 ORDER BY ga.date ASC")
    List<GenericValuesRecord> findAllByDateBetween
            (String startDate, String endDate);



}

