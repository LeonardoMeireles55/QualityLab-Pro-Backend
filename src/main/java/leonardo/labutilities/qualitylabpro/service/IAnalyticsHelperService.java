package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.dto.analytics.BiochemistryValuesRecord;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAnalyticsHelperService {

    GenericAnalytics findAnalyticsById(Long id);

    List<GenericAnalytics> submitAnalytics(List<BiochemistryValuesRecord> valuesOfLevelsList);

    List<BiochemistryValuesRecord> findAll(Pageable pageable);

    List<BiochemistryValuesRecord> findAnalyticsByName(Pageable pageable, String name);

    List<BiochemistryValuesRecord> findAllAnalyticsByDate(String dateStart, String dateEnd);

    List<BiochemistryValuesRecord> findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level);

    List<BiochemistryValuesRecord>
    findAllAnalyticsByNameAndLevelAndDate(String name, String level, String dateStart, String dateEnd);

    void removeAnalyticsById(Long id);
}
