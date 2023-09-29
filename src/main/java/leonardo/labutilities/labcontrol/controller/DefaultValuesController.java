package leonardo.labutilities.labcontrol.controller;

import jakarta.annotation.PostConstruct;
import leonardo.labutilities.labcontrol.analitics.DefaultValues;
import leonardo.labutilities.labcontrol.main.DefaultRepositoyStatic;
import leonardo.labutilities.labcontrol.main.DefaultValuesRepository;
import leonardo.labutilities.labcontrol.records.ValuesOfRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static leonardo.labutilities.labcontrol.controller.DefaultValuesManager.defaultValuesMap;

@RestController
@RequestMapping("/defaultsvalues")
public class DefaultValuesController {
    @Autowired
    private DefaultValuesRepository repository;
    @Autowired
    private DefaultRepositoyStatic repositoyStatic;

    @PostMapping
    @Transactional
    @RequestMapping("/register")
    public void register(@RequestBody ValuesOfRegister values) {
        var defaultValues = new DefaultValues(values);
        repository.save(new DefaultValues(values));
        defaultValuesMap.put(defaultValues.getName(), defaultValues);
    }
    @PostMapping("/register2")
    public void registerValues(@RequestBody List<ValuesOfRegister> valuesList) {
        for(int i = 0; i <= 4; i++) {
            var defaultValues = new DefaultValues(valuesList.get(i));
            repository.save(new DefaultValues(valuesList.get(i)));
            defaultValuesMap.put(defaultValues.getName(), defaultValues);
        }
    }

    @PostMapping("/teste")
    public String teste(@RequestBody List<ValuesOfRegister> valuesList) {
        List<String> teste = new ArrayList<>();
        for(int i = 0; i <= 4; i++) {
            teste.add(valuesList.get(i).name());
        }
        return teste.toString();

    }

    @PostConstruct
    public void getDefaultsValues() {
        Iterable<DefaultValues> defaultValuesList = repository.findAll();
        for (DefaultValues defaultValue : defaultValuesList) {
            defaultValuesMap.put(defaultValue.getName(), defaultValue);
        }
      }
    }