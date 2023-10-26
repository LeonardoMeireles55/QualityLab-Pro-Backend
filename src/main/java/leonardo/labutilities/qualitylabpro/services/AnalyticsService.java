package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.analytics.Analytics;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevels;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevelsList;
import leonardo.labutilities.qualitylabpro.repositories.AnalyticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalyticsService {

    private final AnalyticRepository analyticRepository;
    private final ValidatorService validatorService;

    public Analytics sendValues(ValuesOfLevels values) {
        return analyticRepository.save(new Analytics(values, validatorService));
    }

    public List<Analytics> sendValuesList(List<ValuesOfLevels> valuesOfLevelsList) {
        List<Analytics> analyticsList = new ArrayList<>();
        for (ValuesOfLevels value : valuesOfLevelsList) {
            var analyticsLevels = new Analytics(value, validatorService);
            analyticRepository.save(analyticsLevels);
            analyticsList.add(analyticsLevels);
        }
        return analyticsList;
    }

    public void deleteValues(Long id) {
        if (!analyticRepository.existsById(id)) {
            throw new RuntimeException("invalid values or not exists");
        }
        analyticRepository.deleteById(id);
    }

    public void deleteValuesAll() {
        analyticRepository.deleteAll();
    }

    public List<ValuesOfLevelsList> getResultsAll() {
        if (!analyticRepository.findAll().isEmpty()) {
            return analyticRepository.findAll().stream().map(ValuesOfLevelsList::new).toList();
        }
        throw new ErrorHandling.NoContentException("List is empty");
    }

    public List<ValuesOfLevelsList> getResultsByName(String name) {
        if (!analyticRepository.existsByName(name)) {
            throw new RuntimeException("Test name not exist in Database");
        }
        return analyticRepository.findAllByName(name).stream()
                .map(ValuesOfLevelsList::new).toList();
    }

    public String getResultsByNameLevel1(String name) {
        if (analyticRepository.existsByName(name)) {
            var listByNameLevel1 = analyticRepository.findAllByName(name).stream().map(ValuesOfLevelsList::new).toList();
            return listByNameLevel1.stream()
                    .map(analytic -> analytic.date() + " Level1: " + analytic.name() + " ---> "
                            + analytic.normalValue() + " : " + analytic.normalValid() + ", Regra: "
                            + analytic.normalObs()).toList().toString();
        }
        throw new RuntimeException("Test name not exist in Database");
    }

    public String getResultsByNameLevel2(String name) {
        if (analyticRepository.existsByName(name)) {
            var list = analyticRepository.findAll().stream().map(ValuesOfLevelsList::new).toList();
            List<String> analyticListByNameList = new ArrayList<>();
            for (ValuesOfLevelsList item : list) {
                if (item.name().contains(name.toUpperCase())) {
                    analyticListByNameList.add(item.date() + " Level2: " + item.name() + " ---> "
                            + item.highValue() + " : " +
                            item.highValid() + ", Regra: " + item.highObs());
                }
            }
            return analyticListByNameList.toString();
        }
        throw new RuntimeException("Test name not exist in Database");
    }
}
