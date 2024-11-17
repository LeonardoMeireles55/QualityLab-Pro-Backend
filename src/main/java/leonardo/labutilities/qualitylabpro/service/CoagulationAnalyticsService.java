package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.dto.analytics.BiochemistryValuesRecord;
import leonardo.labutilities.qualitylabpro.infra.exception.CustomGlobalErrorHandling;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

// Derived class
@Service
public class CoagulationAnalyticsService extends AbstractAnalyticsHelperService {

    public CoagulationAnalyticsService(GenericAnalyticsRepository genericAnalyticsRepository,
                                       RulesValidatorComponent rulesValidatorComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }

    @Override
    public List<BiochemistryValuesRecord> findAllAnalyticsByNameAndLevel(Pageable pageable, String name, String level) {
        this.ensureNameExists(name);
        return this.findAllGenericAnalyticsByNameAndLevel(pageable, name,
                this.convertLevel(level));
    }

    @Override
    public List<BiochemistryValuesRecord> findAllAnalyticsByNameAndLevelAndDate
            (String name, String level, String dateStart, String dateEnd) {
        this.ensureNameExists(name);
        return this.findAllGenericAnalyticsByNameAndLevelAndDate(name.toUpperCase(),
                this.convertLevel(level), dateStart, dateEnd);
    }

    @Override
    public MeanAndStandardDeviationRecord generateMeanAndStandardDeviation(String name, String level, String dateStart, String dateEnd) {

        var filteredResult =
                getFilteredRecords(findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd));

        double sum = filteredResult.stream().mapToDouble(BiochemistryValuesRecord::value).sum();

        int count = filteredResult.size();

        return calculateMeanAndStandardDeviation(sum, count);
    }

    @Override
    public String convertLevel(String inputLevel) {
        return switch (inputLevel) {
            case "1" -> "Normal C. Assayed";
            case "2" -> "Low Abn C. Assayed";
            default -> throw new CustomGlobalErrorHandling.ResourceNotFoundException("Level not found.");
        };
    }
}