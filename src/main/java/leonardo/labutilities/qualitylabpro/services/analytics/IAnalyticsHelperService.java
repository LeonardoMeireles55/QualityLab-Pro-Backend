package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.List;

import leonardo.labutilities.qualitylabpro.dtos.analytics.AnalyticsRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GroupedValuesByLevel;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GroupedMeanAndStdRecordByLevel;
import org.springframework.data.domain.Pageable;

public interface IAnalyticsHelperService {

	List<GroupedValuesByLevel> findGroupedAnalyticsByLevel(String name, LocalDateTime startDate,
			LocalDateTime endDate);

	List<GroupedMeanAndStdRecordByLevel> getMeanAndStandardDeviationForGroups(
			List<GroupedValuesByLevel> records);

	List<GroupedValuesByLevel> findFilteredGroupedAnalytics(List<GroupedValuesByLevel> records);

	boolean isGroupedRecordValid(GroupedValuesByLevel record);

	boolean isRecordValid(AnalyticsRecord record);

	AnalyticsRecord findById(Long id);

	void saveNewAnalyticsRecords(List<AnalyticsRecord> valuesOfLevelsList);

	List<AnalyticsRecord> findAll(Pageable pageable);

	List<AnalyticsRecord> findAnalyticsByNameWithPagination(Pageable pageable, String name);

	List<AnalyticsRecord> findAllAnalyticsByDate(LocalDateTime dateStart,
												 LocalDateTime dateEnd);

	List<AnalyticsRecord> getAllByNameInAndDateBetween(List<String> names,
													   LocalDateTime startDate, LocalDateTime endDate);

	List<AnalyticsRecord> findAnalyticsByNameAndLevel(Pageable pageable, String name,
													  String level);

	List<AnalyticsRecord> findAllAnalyticsByNameAndLevelAndDate(String name, String level,
																LocalDateTime dateStart, LocalDateTime dateEnd);

	void deleteAnalyticsById(Long id);
}
