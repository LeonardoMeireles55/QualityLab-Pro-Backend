package leonardo.labutilities.qualitylabpro.services;

import jakarta.annotation.PostConstruct;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.DefaultValues;
import leonardo.labutilities.qualitylabpro.record.defaultValues.DefaultRegisterRecord;
import leonardo.labutilities.qualitylabpro.record.defaultValues.DefaultRegisterListRecord;
import leonardo.labutilities.qualitylabpro.record.valuesOfAnalytics.ValuesOfRegistedRecord;
import leonardo.labutilities.qualitylabpro.repository.DefaultValuesRepositoryCustom;
import leonardo.labutilities.qualitylabpro.repository.LotRepository;
import leonardo.labutilities.qualitylabpro.repository.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static leonardo.labutilities.qualitylabpro.domain.entitys.DefaultValues.defaultValuesMap;

@RequiredArgsConstructor
@Service
public class DefaultValuesService {

    private final DefaultValuesRepositoryCustom defaultValuesRepositoryCustom;
    private final LotRepository lotRepository;
    private final UserRepositoryCustom userRepositoryCustom;

    @PostConstruct
    public void loadDefaultsValues() {
        Iterable<DefaultValues> defaultValuesList = defaultValuesRepositoryCustom.findAll();
        for (DefaultValues defaultValue : defaultValuesList) {
            defaultValuesMap.put(defaultValue.getName(), defaultValue);
        }
    }

    public DefaultValues register(DefaultRegisterRecord values) {
        if (lotRepository.existsById(values.lotId()) && userRepositoryCustom.existsById(values.user_id())) {
            var defaultValues = new DefaultValues(values);
            if (!defaultValuesRepositoryCustom.existsByName(values.name())) {
                defaultValuesMap.put(defaultValues.getName(), defaultValues);
                loadDefaultsValues();
                return defaultValuesRepositoryCustom.save(defaultValues);
            }
            throw new ErrorHandling.DataIntegrityViolationException();
        }
        throw new ErrorHandling.ResourceNotFoundException("User or Lot not Found.");
    }

    public List<ValuesOfRegistedRecord> listRegister(List<DefaultRegisterRecord> defaultRegisterRecords) {
        try {
            System.out.printf(defaultRegisterRecords.get(0).user_id().toString());
            if (lotRepository.existsById(defaultRegisterRecords.get(0).lotId()) ||
                    userRepositoryCustom.existsById(defaultRegisterRecords.get(0).user_id())) {
                return defaultRegisterRecords.stream()
                        .map(defaultRegisterRecord -> {
                            DefaultValues defaultValues = new DefaultValues(defaultRegisterRecord);
                            defaultValuesMap.put(defaultRegisterRecord.name(), defaultValues);
                            defaultValuesRepositoryCustom.save(defaultValues);
                            return new ValuesOfRegistedRecord(defaultValues);
                        }).toList();
            } throw new ErrorHandling.ResourceNotFoundException("User or Lot not Found.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            loadDefaultsValues();
        }
    }
    public List<DefaultValues> getDefaultsValues() {
        List<DefaultValues> defaultValuesList = defaultValuesRepositoryCustom.findAll();
        if(defaultValuesList.isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("defaultValuesList is empty.");
        }
        return defaultValuesList;
    }
    @Cacheable(value = "name")
    public List<DefaultRegisterListRecord> getValuesByName(String name) {
        if(!defaultValuesRepositoryCustom.existsByName(name.toUpperCase())) {
            throw new ErrorHandling.ResourceNotFoundException("DefaultValues not exists");
        }
        return defaultValuesRepositoryCustom.findAll().stream()
                .filter(s -> Objects.equals(s.getName(), name.toUpperCase()))
                .map(DefaultRegisterListRecord::new).toList();
    }
    public void deleteValuesById(Long id){
        if(!defaultValuesRepositoryCustom.existsById(id)) {
            throw new ErrorHandling.ResourceNotFoundException("DefaultValues not exists.");
        }
        defaultValuesRepositoryCustom.deleteById(id);
        defaultValuesMap.clear();
    }
    public void deleteValues(String name){
        if(!defaultValuesRepositoryCustom.existsByName(name)) {
            throw new ErrorHandling.ResourceNotFoundException("DefaultValues not exists.");
        }
        defaultValuesRepositoryCustom.deleteByName(name);
        defaultValuesMap.clear();
    }
    public void deleteValuesAll() {
        if(defaultValuesRepositoryCustom.findAll().isEmpty()) {
            throw new ErrorHandling.ResourceNotFoundException("DefaultValues not exists.");
        }
        defaultValuesRepositoryCustom.deleteAll();
        defaultValuesMap.clear();
    }
}