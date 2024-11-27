package leonardo.labutilities.qualitylabpro.service;

import leonardo.labutilities.qualitylabpro.components.RulesValidatorComponent;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

public abstract class AbstractAnalyticsHelperService extends AnalyticsHelperService {

    public AbstractAnalyticsHelperService(
            GenericAnalyticsRepository genericAnalyticsRepository,
            RulesValidatorComponent rulesValidatorComponent) {
        super(genericAnalyticsRepository, rulesValidatorComponent);
    }

    public abstract List<GenericValuesRecord> findAllAnalyticsByNameAndLevel(
            Pageable pageable, String name, String level);

    public abstract List<GenericValuesRecord> findAllAnalyticsByNameAndLevelAndDate(
            String name, String level, String dateStart, String dateEnd);

    public abstract String convertLevel(String level);

    public abstract MeanAndStandardDeviationRecord
    generateMeanAndStandardDeviation(String name, String level, String dateStart, String dateEnd);

    private boolean shouldIncludeRecord(GenericValuesRecord record) {
        String rules = record.rules();
        return !Objects.equals(rules, "+3s") && !Objects.equals(rules, "-3s");
    }

    List<GenericValuesRecord> getFilteredRecords(List<GenericValuesRecord> records) {
        return records.stream().filter(this::shouldIncludeRecord)
                .toList();
    }

    public MeanAndStandardDeviationRecord calculateMeanAndStandardDeviation(Double totalValue, Integer size, List<Double> values) {
        // Calculate mean
        double mean = totalValue / size;

        // Calculate variance (squared differences)
        double variance = values.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .sum() / size;

        // Calculate standard deviation
        double standardDeviation = Math.sqrt(variance);

        return new MeanAndStandardDeviationRecord(mean, standardDeviation);
    }
}
