package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.main.Integra400;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.records.integra.ValuesOfLevelsIntegra;
import leonardo.labutilities.qualitylabpro.repositories.Integra400Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Integra400Service {
    private final Integra400Repository integra400Repository;
    private final ValidatorServiceIntegra validatorServiceIntegra;
    private final DateFormatService dateFormatService;

    public List<Integra400> sendValues(List<ValuesOfLevelsIntegra> valuesOfLevelsList) {
        List<Integra400> analyticsList = new ArrayList<>();
        for (ValuesOfLevelsIntegra values : valuesOfLevelsList) {
            var analyticsLevels = new Integra400(values, validatorServiceIntegra, dateFormatService);
            integra400Repository.save(analyticsLevels);
            analyticsList.add(analyticsLevels);
        }
        return analyticsList;
    }

    public List<ValuesOfLevelsIntegra> getResultsByLevel(String name, String level) {
        var nameUpper = name.toUpperCase();
        if (!integra400Repository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        return integra400Repository.findAllByName(nameUpper).stream()
                .map(ValuesOfLevelsIntegra::new).filter(f -> f.level().contains(level)).toList();
    }

    public List<ValuesOfLevelsIntegra> getResultsByName(String name) {
        var nameUpper = name.toUpperCase();
        if (!integra400Repository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();        }
        return integra400Repository.findAllByName(nameUpper).stream()
                .map(ValuesOfLevelsIntegra::new).toList();
    }
}
