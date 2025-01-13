package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.List;

import leonardo.labutilities.qualitylabpro.dtos.analytics.AnalyticsRecord;
import leonardo.labutilities.qualitylabpro.repositories.AnalyticsRepository;
import leonardo.labutilities.qualitylabpro.utils.exception.CustomGlobalErrorHandling;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HematologyAnalyticsService extends AbstractAnalyticsService {

	public HematologyAnalyticsService(AnalyticsRepository analyticsRepository) {
		super(analyticsRepository);
	}

	@Override
	@Cacheable(cacheNames = "analytics-cache",
			key = "#name + '-' + #level + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
	public List<AnalyticsRecord> findAnalyticsByNameAndLevel(Pageable pageable, String name,
															 String level) {
		ensureNameExists(name);
		return findAnalyticsByNameAndLevelWithPagination(pageable, name, convertLevel(level));
	}

	@Override
	@Cacheable(cacheNames = "analytics-cache",
			key = "#name + '-' + #level + '-' + #dateStart.toString() + '-' + #dateEnd.toString()")
	public List<AnalyticsRecord> findAllAnalyticsByNameAndLevelAndDate(String name,
																	   String level, LocalDateTime dateStart, LocalDateTime dateEnd) {
		ensureNameExists(name);
		return findAnalyticsByNameLevelAndDate(name, convertLevel(level), dateStart, dateEnd);
	}

	@Override
	public String convertLevel(String inputLevel) {
		return switch (inputLevel) {
			case "1" -> "low";
			case "2" -> "normal";
			case "3" -> "high";
			default -> throw new CustomGlobalErrorHandling.ResourceNotFoundException(
					"Level not found.");
		};
	}
}
