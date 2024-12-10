package leonardo.labutilities.qualitylabpro.service.analytics;

import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IAnalyticsHelperService {

    GenericAnalytics findAnalyticsById(Long id);

    void submitAnalytics(List<GenericValuesRecord> valuesOfLevelsList);

    List<GenericValuesRecord> findAll(Pageable pageable);

    List<GenericValuesRecord> findAnalyticsByName(Pageable pageable, String name);

    List<GenericValuesRecord> findAllAnalyticsByDate(LocalDateTime dateStart, LocalDateTime dateEnd);

    List<GenericValuesRecord> getAllByNameInAndDateBetween(
            List<String> names, LocalDateTime startDate, LocalDateTime endDate);

    List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level);

    List<GenericValuesRecord>
    findAllAnalyticsByNameAndLevelAndDate(String name, String level, LocalDateTime dateStart, LocalDateTime dateEnd);

    void removeAnalyticsById(Long id);
}
