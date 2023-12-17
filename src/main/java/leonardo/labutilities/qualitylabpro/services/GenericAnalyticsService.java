package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.main.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGeneric;
import leonardo.labutilities.qualitylabpro.repositories.GenericAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GenericAnalyticsService {
    private final GenericAnalyticsRepository genericAnalyticsRepository;
    private final GenericValidatorService genericValidatorService;

    public List<GenericAnalytics> sendValues(List<ValuesOfLevelsGeneric> valuesOfLevelsList) {
        List<GenericAnalytics> analyticsList = new ArrayList<>();
        for (ValuesOfLevelsGeneric values : valuesOfLevelsList) {
            if (!genericAnalyticsRepository.existsByDateAndLevelAndName(values.date(), values.level(), values.name())) {
                for (ValuesOfLevelsGeneric valuesFiltered : valuesOfLevelsList) {
                    var analyticsLevels = new GenericAnalytics(valuesFiltered, genericValidatorService);
                    if(!genericAnalyticsRepository.existsByDateAndLevelAndName(valuesFiltered.date(),valuesFiltered.level(), valuesFiltered.name())) {
                        genericAnalyticsRepository.save(analyticsLevels);
                        analyticsList.add(analyticsLevels);
                    }
                }
                return analyticsList;
            }
        }
        return analyticsList;
    }
    public Page<ValuesOfLevelsGeneric> getResults(Pageable pageable) {
        return genericAnalyticsRepository.findAll(pageable).map(ValuesOfLevelsGeneric::new);
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsGeneric> getResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();        }
        return genericAnalyticsRepository.findAllByName(pageable, nameUpper).stream()
                .map(ValuesOfLevelsGeneric::new).toList();
    }
    @Cacheable(value ={"name", "level"})
    public List<ValuesOfLevelsGeneric> getResultsByNameAndLevel(Pageable pageable, String name, String level) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        if (Objects.equals(level, "1")) {
            level = "PCCC1";
        } else if (Objects.equals(level, "2")) {
            level = "PCCC2";
        } else {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        return genericAnalyticsRepository.findAllByNameAndLevel(pageable, nameUpper, level).stream()
                .map(ValuesOfLevelsGeneric::new).toList();
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsGeneric> getResultsByDateAsc(String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        return genericAnalyticsRepository.findAllByNameOrderByDateAsc(nameUpper).stream()
                .map(ValuesOfLevelsGeneric::new).toList();
    }
    @Cacheable(value = "name")
    public List<ValuesOfLevelsGeneric> getResultsByDateDesc(String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        return genericAnalyticsRepository.findAllByNameOrderByDateDesc(nameUpper).stream()
                .map(ValuesOfLevelsGeneric::new).toList();
    }
}