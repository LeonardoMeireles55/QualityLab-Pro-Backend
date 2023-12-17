package leonardo.labutilities.qualitylabpro.services;

import jakarta.annotation.PostConstruct;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.main.entitys.DefaultValues;
import leonardo.labutilities.qualitylabpro.records.defaultValues.DefaultRegisterDTO;
import leonardo.labutilities.qualitylabpro.records.defaultValues.DefaultRegisterListDTO;
import leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics.ValuesOfRegistedDTO;
import leonardo.labutilities.qualitylabpro.repositories.DefaultValuesRepository;
import leonardo.labutilities.qualitylabpro.repositories.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static leonardo.labutilities.qualitylabpro.main.entitys.DefaultValues.defaultValuesMap;

@RequiredArgsConstructor
@Service
public class DefaultValuesService {

    private final DefaultValuesRepository defaultValuesRepository;
    private final LotRepository lotRepository;
    @PostConstruct
    public void loadDefaultsValues() {
        Iterable<DefaultValues> defaultValuesList = defaultValuesRepository.findAll();
        for (DefaultValues defaultValue : defaultValuesList) {
            defaultValuesMap.put(defaultValue.getName(), defaultValue);
        }
    }
    public DefaultValues register(DefaultRegisterDTO values) {
        if(lotRepository.existsById(values.lotId())) {
            var defaultValues = new DefaultValues(values);
            if(!defaultValuesRepository.existsByName(values.name())){

                defaultValuesMap.put(defaultValues.getName(), defaultValues);
                loadDefaultsValues();
                return defaultValuesRepository.save(defaultValues);
            }
            throw new ErrorHandling.DataIntegrityViolationException();
        }
        throw new ErrorHandling.ResourceNotFoundException();
        }

    public List<ValuesOfRegistedDTO> listRegister(List<DefaultRegisterDTO> defaultRegisterDTOS) {
        try {
            return defaultRegisterDTOS.stream()
                    .map(defaultRegisterDTO -> {
                        DefaultValues defaultValues = new DefaultValues(defaultRegisterDTO);
                        defaultValuesMap.put(defaultRegisterDTO.name(), defaultValues);
                        defaultValuesRepository.save(defaultValues);
                        return new ValuesOfRegistedDTO(defaultValues);
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            loadDefaultsValues();
        }
    }
    public List<DefaultValues> getDefaultsValues() {
        List<DefaultValues> defaultValuesList = defaultValuesRepository.findAll();
        if(defaultValuesList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        return defaultValuesList;
    }
    @Cacheable(value = "name")
    public List<DefaultRegisterListDTO> getValuesByName(String name) {
        if(!defaultValuesRepository.existsByName(name.toUpperCase())) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        return defaultValuesRepository.findAll().stream()
                .filter(s -> Objects.equals(s.getName(), name.toUpperCase()))
                .map(DefaultRegisterListDTO::new).toList();
    }
    public void deleteValuesById(Long id){
        if(!defaultValuesRepository.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        defaultValuesRepository.deleteById(id);
        defaultValuesMap.clear();
    }
    public void deleteValues(String name){
        if(!defaultValuesRepository.existsByName(name)) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        defaultValuesRepository.deleteByName(name);
        defaultValuesMap.clear();
    }
    public void deleteValuesAll() {
        if(defaultValuesRepository.findAll().isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        defaultValuesRepository.deleteAll();
        defaultValuesMap.clear();
    }
}