package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.main.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGeneric;
import leonardo.labutilities.qualitylabpro.repositories.GenericAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenericAnalyticsService {
    private final GenericAnalyticsRepository genericAnalyticsRepository;
    private final GenericValidatorService genericValidatorService;

    public Stream<GenericAnalytics> sendValues(List<ValuesOfLevelsGeneric> valuesOfLevelsList) {
        var valuesFilter = valuesOfLevelsList.stream()
                .filter(values -> !genericAnalyticsRepository.existsByDateAndLevelAndName(values.date(), values.level(),
                        values.name()));
        return valuesFilter.map(values -> {
            GenericAnalytics genericAnalytics = new GenericAnalytics(values, genericValidatorService);
            return genericAnalyticsRepository.save(genericAnalytics);
        });
    }

    @Cacheable(value = "name")
    public Page<ValuesOfLevelsGeneric> getAllResults(Pageable pageable) {
        log.info("Retrieve all results...");
        return genericAnalyticsRepository.findAll(pageable).map(ValuesOfLevelsGeneric::new);
    }
    @Cacheable(value = "name")
    public Stream<ValuesOfLevelsGeneric> getResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        log.info("Retrieve results by name...");
        return genericAnalyticsRepository.findAllByName(pageable, nameUpper).stream()
                .map(ValuesOfLevelsGeneric::new);
    }
    @Cacheable(value ={"name", "level"})
    public Stream<ValuesOfLevelsGeneric> getResultsByNameAndLevel(Pageable pageable, String name, String level) {
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
        log.info("Retrieve results by name and level...");
        return genericAnalyticsRepository.findAllByNameAndLevel(pageable, nameUpper, level).stream()
                .map(ValuesOfLevelsGeneric::new);
    }
    @Cacheable(value = "name")
    public Stream<ValuesOfLevelsGeneric> getResultsByDateAsc(String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        log.info("Retrieve results by dateAsc...");
        return genericAnalyticsRepository.findAllByNameOrderByDateAsc(nameUpper).stream()
                .map(ValuesOfLevelsGeneric::new);
    }
    @Cacheable(value = "name")
    public Stream<ValuesOfLevelsGeneric> getResultsByDateDesc(String name) {
        var nameUpper = name.toUpperCase();
        if (!genericAnalyticsRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        log.info("Retrieve results by dateDesc...");
        return genericAnalyticsRepository.findAllByNameOrderByDateDesc(nameUpper).stream()
                .map(ValuesOfLevelsGeneric::new);
    }
}