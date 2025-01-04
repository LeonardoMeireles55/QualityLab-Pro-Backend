package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.List;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesGroupByLevel;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStandardDeviationRecordGroupByLevel;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
import org.springframework.data.domain.Pageable;

public interface IAnalyticsHelperService {

        List<GenericValuesGroupByLevel> findGroupedAnalyticsByLevel(String name, LocalDateTime startDate,
                                                                    LocalDateTime endDate);

        List<MeanAndStandardDeviationRecordGroupByLevel> getMeanAndStandardDeviationForGroups(
                        List<GenericValuesGroupByLevel> records);

        List<GenericValuesGroupByLevel> findFilteredGroupedAnalytics(
                        List<GenericValuesGroupByLevel> records);

        boolean isGroupedRecordValid(GenericValuesGroupByLevel record);

        boolean isRecordValid(GenericValuesRecord record);

        GenericAnalytics findById(Long id);

        void saveNewAnalyticsRecords(List<GenericValuesRecord> valuesOfLevelsList);

        List<GenericValuesRecord> findAll(Pageable pageable);

        List<GenericValuesRecord> findAnalyticsByNameWithPagination(Pageable pageable, String name);

        List<GenericValuesRecord> findAllAnalyticsByDate(LocalDateTime dateStart,
                        LocalDateTime dateEnd);

        List<GenericValuesRecord> getAllByNameInAndDateBetween(List<String> names,
                        LocalDateTime startDate, LocalDateTime endDate);

        List<GenericValuesRecord> findAnalyticsByNameAndLevel(Pageable pageable, String name,
                                                              String level);

        List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate(String name, String level,
                        LocalDateTime dateStart, LocalDateTime dateEnd);

        void deleteAnalyticsById(Long id);
}
