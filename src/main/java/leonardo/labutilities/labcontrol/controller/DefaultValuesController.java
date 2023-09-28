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
    @PostConstruct
    public void getDefaultsValues() {
        Iterable<DefaultValues> defaultValuesList = repository.findAll();
        for (DefaultValues defaultValue : defaultValuesList) {
            defaultValuesMap.put(defaultValue.getName(), defaultValue);
        }
      }
    }