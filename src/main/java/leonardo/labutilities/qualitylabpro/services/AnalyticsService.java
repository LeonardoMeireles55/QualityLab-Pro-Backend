package leonardo.labutilities.qualitylabpro.services;

import jakarta.annotation.PostConstruct;
import leonardo.labutilities.qualitylabpro.analytics.Analytics;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevels;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevelsList;
import leonardo.labutilities.qualitylabpro.repositories.AnalyticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static leonardo.labutilities.qualitylabpro.analytics.Analytics.analyticsHashMap;

@RequiredArgsConstructor
@Service
public class AnalyticsService {

    private final AnalyticRepository analyticRepository;

    public Analytics sendValues(ValuesOfLevels values) {
        return analyticRepository.save(new Analytics(values));
    }
    public List<Analytics> sendValuesList(List<ValuesOfLevels> valuesOfLevelsList) {
        List<Analytics> analyticsList = new ArrayList<>();
        for (ValuesOfLevels value : valuesOfLevelsList) {
            var analyticsLevels = new Analytics(value);
            analyticRepository.save(analyticsLevels);
            analyticsList.add(analyticsLevels);
        }
        return analyticsList;
    }
    public void deleteValues(Long id){
        analyticRepository.deleteById(id);
    }
    public void deleteValuesAll(){
        analyticRepository.deleteAll();
    }

    public List<ValuesOfLevelsList> getResultsAll() {
        return analyticRepository.findAll().stream().map(ValuesOfLevelsList::new).toList();
    }

    public List<ValuesOfLevelsList> getResultsByName(String name) {
        if (analyticRepository.existsByName(name)) {
            return analyticRepository.findAll().stream()
                    .filter(s -> Objects.equals(s.getName(), name.toUpperCase())).map(ValuesOfLevelsList::new).toList();
        }
        throw new RuntimeException("Test name not exist in Database");
    }

    public String getResultsByNameLevel1(String name) {
        if (analyticRepository.existsByName(name)) {
            var list = analyticRepository.findAll().stream().map(ValuesOfLevelsList::new).toList();
            List<String> analyticListByNameList = new ArrayList<>();
            for (ValuesOfLevelsList item : list) {
                if (item.name().contains(name.toUpperCase())) {
                    analyticListByNameList.add(item.date() + " Nível1: " + item.name() + " ---> " + item.normalValue() + " : " +
                            item.normalValid() + ", Regra: " + item.normalObs());
                }
            }
            return analyticListByNameList.toString();
        }
        throw new RuntimeException("Test name not exist in Database");
    }

    public String getResultsByNameLevel2(String name) {
        if (analyticRepository.existsByName(name)) {
            var list = analyticRepository.findAll().stream().map(ValuesOfLevelsList::new).toList();
            List<String> analyticListByNameList = new ArrayList<>();
            for (ValuesOfLevelsList item : list) {
                if (item.name().contains(name.toUpperCase())) {
                    analyticListByNameList.add(item.date() + " Nível2: " + item.name() + " ---> " + item.highValue() + " : " +
                            item.highValid() + ", Regra: " + item.highObs());
                }
            }
            return analyticListByNameList.toString();
        }
        throw new RuntimeException("Test name not exist in Database");
    }
}
