package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.main.Analytics;
import leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics.ValuesOfLevelsDTO;
import leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics.ValuesOfLevelsListDTO;
import leonardo.labutilities.qualitylabpro.repositories.AnalyticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalyticsService {

    private final AnalyticRepository analyticRepository;
    private final ValidatorService validatorService;

    public Analytics sendValues(ValuesOfLevelsDTO values) {
        return analyticRepository.save(new Analytics(values, validatorService));
    }

    public List<Analytics> sendValuesList(List<ValuesOfLevelsDTO> valuesOfLevelsDTOList) {
        List<Analytics> analyticsList = new ArrayList<>();
        for (ValuesOfLevelsDTO values : valuesOfLevelsDTOList) {
            var analyticsLevels = new Analytics(values, validatorService);
            analyticRepository.save(analyticsLevels);
            analyticsList.add(analyticsLevels);
        }
        return analyticsList;
    }

    public void deleteValues(Long id) {
        if (!analyticRepository.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        analyticRepository.deleteById(id);
    }

    public void deleteValuesAll() {
        if(analyticRepository.findAll().isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        analyticRepository.deleteAll();
    }

    public List<ValuesOfLevelsListDTO> getResultsAll(Pageable pageable) {
        if (!analyticRepository.findAll().isEmpty()) {
            return analyticRepository.findAll(pageable).stream().map(ValuesOfLevelsListDTO::new).toList();
        }
        throw new ErrorHandling.ResourceNotFoundException();
    }

    public List<ValuesOfLevelsListDTO> getResultsByName(Pageable pageable, String name) {
        var nameUpper = name.toUpperCase();
        if (!analyticRepository.existsByName(nameUpper)) {
            throw new ErrorHandling.ResourceNotFoundException();        }
        return analyticRepository.findAllByName(pageable, nameUpper).stream()
                .map(ValuesOfLevelsListDTO::new).toList();
    }
}
