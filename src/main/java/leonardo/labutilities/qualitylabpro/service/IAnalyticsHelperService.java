package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAnalyticsHelperService {

    GenericAnalytics findAnalyticsById(Long id);

    List<GenericAnalytics> submitAnalytics(List<ValuesOfLevelsGenericRecord> valuesOfLevelsList);

    List<ValuesOfLevelsGenericRecord> findAll(Pageable pageable);

    List<ValuesOfLevelsGenericRecord> findAnalyticsByName(Pageable pageable, String name);

    List<ValuesOfLevelsGenericRecord> findAllAnalyticsByDate(String dateStart, String dateEnd);

    List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level);

    List<ValuesOfLevelsGenericRecord> findAllAnalyticsByNameAndLevelAndDate(String name, String level, String dateStart, String dateEnd);

    void removeAnalyticsById(Long id);
}
