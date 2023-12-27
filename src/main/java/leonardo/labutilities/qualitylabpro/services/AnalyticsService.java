package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.component.AnalyticsValidatorComponent;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.Analytics;
import leonardo.labutilities.qualitylabpro.record.valuesOfAnalytics.ValuesOfLevelsRecord;
import leonardo.labutilities.qualitylabpro.record.valuesOfAnalytics.ValuesOfLevelsListRecord;
import leonardo.labutilities.qualitylabpro.repository.AnalyticRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnalyticsService {

    private final AnalyticRepositoryCustom analyticRepositoryCustom;
    private final AnalyticsValidatorComponent analyticsValidatorComponent;

    public Analytics sendValues(ValuesOfLevelsRecord values) {
        return analyticRepositoryCustom.save(new Analytics(values, analyticsValidatorComponent));
    }

    public List<Analytics> sendValuesList(List<ValuesOfLevelsRecord> valuesOfLevelsRecordList) {
        return valuesOfLevelsRecordList.stream()
                .map(values -> {
                    Analytics analyticsLevels = new Analytics(values, analyticsValidatorComponent);
                    analyticRepositoryCustom.save(analyticsLevels);
                    return analyticsLevels;
                })
                .collect(Collectors.toList());
    }

    public void deleteValues(Long id) {
        if (!analyticRepositoryCustom.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException("values by id not found");
        }
        analyticRepositoryCustom.deleteById(id);
    }

    public void deleteValuesAll() {
        if(analyticRepositoryCustom.findAll().isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("already empty");
        }
        analyticRepositoryCustom.deleteAll();
    }

    public List<ValuesOfLevelsListRecord> getResultsAll(Pageable pageable) {
        if (!analyticRepositoryCustom.findAll().isEmpty()) {
            return analyticRepositoryCustom.findAll(pageable).stream().map(ValuesOfLevelsListRecord::new).toList();
        }
        throw new ErrorHandling.ResourceNotFoundException("Results is empty");
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsListRecord> getResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();
        if (!analyticRepositoryCustom.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException("Results by name not found");        }
        return analyticRepositoryCustom.findAllByName(pageable, nameUpper).stream()
                .map(ValuesOfLevelsListRecord::new).toList();
    }
}
