package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.List;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesGroupByLevel;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStandardDeviationRecordGroupByLevel;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
import org.springframework.data.domain.Pageable;

public interface IAnalyticsHelperService {

        List<GenericValuesGroupByLevel> getGroupedByLevel(String name, LocalDateTime startDate,
                        LocalDateTime endDate);

        List<MeanAndStandardDeviationRecordGroupByLevel> calculateMeanAndStandardDeviationGrouped(
                        List<GenericValuesGroupByLevel> records);

        List<GenericValuesGroupByLevel> getGroupedFilteredRecords(
                        List<GenericValuesGroupByLevel> records);

        boolean groupedShouldIncludeRecord(GenericValuesGroupByLevel record);

        boolean shouldIncludeRecord(GenericValuesRecord record);

        GenericAnalytics findAnalyticsById(Long id);

        void submitAnalytics(List<GenericValuesRecord> valuesOfLevelsList);

        List<GenericValuesRecord> findAll(Pageable pageable);

        List<GenericValuesRecord> findAnalyticsByName(Pageable pageable, String name);

        List<GenericValuesRecord> findAllAnalyticsByDate(LocalDateTime dateStart,
                        LocalDateTime dateEnd);

        List<GenericValuesRecord> getAllByNameInAndDateBetween(List<String> names,
                        LocalDateTime startDate, LocalDateTime endDate);

        List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(Pageable pageable, String name,
                        String level);

        List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate(String name, String level,
                        LocalDateTime dateStart, LocalDateTime dateEnd);

        void removeAnalyticsById(Long id);
}
