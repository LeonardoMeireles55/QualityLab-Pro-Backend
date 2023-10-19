package leonardo.labutilities.qualitylabpro.services;

import jakarta.annotation.PostConstruct;
import leonardo.labutilities.qualitylabpro.analytics.DefaultValues;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegister;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegisterList;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfRegisted;
import leonardo.labutilities.qualitylabpro.repositories.DefaultValuesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static leonardo.labutilities.qualitylabpro.analytics.DefaultValues.defaultValuesMap;

@RequiredArgsConstructor
@Service
public class DefaultValuesService {

    private final DefaultValuesRepository defaultValuesRepository;
    @PostConstruct
    public void loadDefaultsValues() {
        Iterable<DefaultValues> defaultValuesList = defaultValuesRepository.findAll();
        for (DefaultValues defaultValue : defaultValuesList) {
            defaultValuesMap.put(defaultValue.getName(), defaultValue);
        }
    }
    public DefaultValues register(DefaultRegister values) {
        var defaultValues = new DefaultValues(values);
        defaultValuesMap.put(defaultValues.getName(), defaultValues);
        loadDefaultsValues();
        return defaultValuesRepository.save(defaultValues);
    }
    public List<ValuesOfRegisted> listRegister(List<DefaultRegister> defaultRegisters) {
        return defaultRegisters.stream()
                .map(defaultRegister -> {
                    DefaultValues defaultValues = new DefaultValues(defaultRegister);
                    defaultValuesMap.put(defaultRegister.name(), defaultValues);
                    defaultValuesRepository.save(defaultValues);
                    return new ValuesOfRegisted(defaultValues);
                }).toList();
    }
    public List<DefaultValues> getDefaultsValues() {
        Iterable<DefaultValues> defaultValuesList = defaultValuesRepository.findAll();
        List<DefaultValues> getList = new ArrayList<>();
        for (DefaultValues defaultValue : defaultValuesList) {
            getList.add(defaultValue);
        }
        return getList;
    }
    public List<DefaultRegisterList> getValuesByName(String name) {
        return defaultValuesRepository.findAll().stream()
                .filter(s -> Objects.equals(s.getName(), name.toUpperCase()))
                .map(DefaultRegisterList::new).toList();
    }
}