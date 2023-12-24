package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.component.AnalyticsValidatorComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.Analytics;
import leonardo.labutilities.qualitylabpro.record.valuesOfAnalytics.ValuesOfLevelsRecord;
import leonardo.labutilities.qualitylabpro.record.valuesOfAnalytics.ValuesOfLevelsListRecord;
import leonardo.labutilities.qualitylabpro.repository.AnalyticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnalyticsService {

    private final AnalyticRepository analyticRepository;
    private final AnalyticsValidatorComponent analyticsValidatorComponent;

    public Analytics sendValues(ValuesOfLevelsRecord values) {
        return analyticRepository.save(new Analytics(values, analyticsValidatorComponent));
    }

    public List<Analytics> sendValuesList(List<ValuesOfLevelsRecord> valuesOfLevelsRecordList) {
        return valuesOfLevelsRecordList.stream()
                .map(values -> {
                    Analytics analyticsLevels = new Analytics(values, analyticsValidatorComponent);
                    analyticRepository.save(analyticsLevels);
                    return analyticsLevels;
                })
                .collect(Collectors.toList());
    }

    public void deleteValues(Long id) {
        if (!analyticRepository.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException("values by id not found");
        }
        analyticRepository.deleteById(id);
    }

    public void deleteValuesAll() {
        if(analyticRepository.findAll().isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("already empty");
        }
        analyticRepository.deleteAll();
    }

    public List<ValuesOfLevelsListRecord> getResultsAll(Pageable pageable) {
        if (!analyticRepository.findAll().isEmpty()) {
            return analyticRepository.findAll(pageable).stream().map(ValuesOfLevelsListRecord::new).toList();
        }
        throw new ErrorHandling.ResourceNotFoundException("Results is empty");
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsListRecord> getResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();
        if (!analyticRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results by name not found");        }
        return analyticRepository.findAllByName(pageable, nameUpper).stream()
                .map(ValuesOfLevelsListRecord::new).toList();
    }
}
