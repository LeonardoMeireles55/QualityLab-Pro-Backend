package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAnalyticsHelperService {

    GenericAnalytics findAnalyticsById(Long id);

    List<GenericValuesRecord> submitAnalytics(List<GenericValuesRecord> valuesOfLevelsList);

    List<GenericValuesRecord> findAll(Pageable pageable);

    List<GenericValuesRecord> findAnalyticsByName(Pageable pageable, String name);

    List<GenericValuesRecord> findAllAnalyticsByDate(String dateStart, String dateEnd);

    List<GenericValuesRecord> getAllByNameInAndDateBetween(
            List<String> names, String startDate, String endDate);

    List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level);

    List<GenericValuesRecord>
    findAllAnalyticsByNameAndLevelAndDate(String name, String level, String dateStart, String dateEnd);

    void removeAnalyticsById(Long id);
}
