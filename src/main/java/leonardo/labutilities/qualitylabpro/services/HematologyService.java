package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.components.HematologyValidatorComponent;
import leonardo.labutilities.qualitylabpro.domain.entities.HematologyAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfHematologyRecord;
import leonardo.labutilities.qualitylabpro.repository.HematologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HematologyService {
    private final HematologyRepository hematologyRepository;
    private final HematologyValidatorComponent hematologyValidatorComponent;

    public Stream<HematologyAnalytics> saveHematology(List<ValuesOfHematologyRecord> valuesOfLevelsList) {
        var valuesFilter = valuesOfLevelsList.stream()
                .filter(values -> !hematologyRepository.existsByDateAndLevelAndName(values.date(),
                        values.level(),
                        values.name()));
        return valuesFilter.map(values -> {
            HematologyAnalytics hematologyAnalytics = new HematologyAnalytics(values, hematologyValidatorComponent);
            return hematologyRepository.save(hematologyAnalytics);
        });
    }

    public List<HematologyAnalytics> GetAllHematology() {
        return hematologyRepository.findAll().stream().toList();
    }
}
