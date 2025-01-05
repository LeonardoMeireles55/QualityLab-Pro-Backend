package leonardo.labutilities.qualitylabpro.services.analytics;

import java.time.LocalDateTime;
import java.util.List;

import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.repositories.GenericAnalyticsRepository;
import leonardo.labutilities.qualitylabpro.utils.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.utils.exception.CustomGlobalErrorHandling;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BiochemistryAnalyticsService extends AbstractAnalyticsService {

    public BiochemistryAnalyticsService(GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }

    @Override
    public List<GenericValuesRecord> findAnalyticsByNameAndLevel(Pageable pageable, String name,
                                                                 String level) {
        this.ensureNameExists(name);
        return this.findAnalyticsByNameAndLevelWithPagination(pageable, name, this.convertLevel(level));
    }

    @Override
    public List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate(String name,
            String level, LocalDateTime dateStart, LocalDateTime dateEnd) {
        ensureNameExists(name);
        return findAnalyticsByNameLevelAndDate(name, convertLevel(level), dateStart,
                dateEnd);
    }

    @Override
    public String convertLevel(String inputLevel) {
        return switch (inputLevel) {
            case "1" -> "PCCC1";
            case "2" -> "PCCC2";
            default -> throw new CustomGlobalErrorHandling.ResourceNotFoundException(
                    "Level not found.");
        };
    }
}
