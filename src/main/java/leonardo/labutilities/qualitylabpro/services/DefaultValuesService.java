package leonardo.labutilities.qualitylabpro.services;

import com.sun.net.httpserver.Authenticator;
import jakarta.annotation.PostConstruct;
import leonardo.labutilities.qualitylabpro.analytics.DefaultValues;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegister;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegisterList;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfRegisted;
import leonardo.labutilities.qualitylabpro.repositories.DefaultValuesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;

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
        if(!defaultValuesRepository.existsByName(values.name())){
            defaultValuesMap.put(defaultValues.getName(), defaultValues);
            loadDefaultsValues();
            return defaultValuesRepository.save(defaultValues);
        }
        throw new ErrorHandling.DataIntegrityViolationException();
    }
    public List<ValuesOfRegisted> listRegister(List<DefaultRegister> defaultRegisters) {
        try {
            return defaultRegisters.stream()
                    .map(defaultRegister -> {
                        DefaultValues defaultValues = new DefaultValues(defaultRegister);
                        defaultValuesMap.put(defaultRegister.name(), defaultValues);
                        defaultValuesRepository.save(defaultValues);
                        return new ValuesOfRegisted(defaultValues);
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
    public List<DefaultRegisterList> getValuesByName(String name) {
        if(!defaultValuesRepository.existsByName(name.toUpperCase())) {
            throw new ErrorHandling.ResourceNotFoundException();
        }
        return defaultValuesRepository.findAll().stream()
                .filter(s -> Objects.equals(s.getName(), name.toUpperCase()))
                .map(DefaultRegisterList::new).toList();
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