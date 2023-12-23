package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.domain.entitys.DefaultValues;
import leonardo.labutilities.qualitylabpro.records.defaultValues.DefaultRegisterRecord;
import leonardo.labutilities.qualitylabpro.records.defaultValues.DefaultRegisterListRecord;
import leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics.ValuesOfRegistedRecord;
import leonardo.labutilities.qualitylabpro.repositories.DefaultValuesRepository;
import leonardo.labutilities.qualitylabpro.services.DefaultValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/defaultsValues")
public class DefaultValuesController {
    private final DefaultValuesRepository repository;
    private final DefaultValuesService defaultValuesService;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<DefaultValues> registerDefaultValues
            (@RequestBody @Valid DefaultRegisterRecord values, UriComponentsBuilder uriComponentsBuilder) {
        DefaultValues defaultValues = defaultValuesService.register(values);
        URI uri = uriComponentsBuilder.path("/defaultsValues/{id}").buildAndExpand(defaultValues.getId()).toUri();
        return ResponseEntity.created(uri).body(defaultValues);
    }
    @Transactional
    @PostMapping("/listRegister")
    public ResponseEntity<List<ValuesOfRegistedRecord>> listRegisterDefaultValues
            (@RequestBody @Valid List<DefaultRegisterRecord> defaultRegisterRecords,
             UriComponentsBuilder uriComponentsBuilder) {

        List<ValuesOfRegistedRecord> defaultValues = defaultValuesService.listRegister(defaultRegisterRecords);
        URI listUri = uriComponentsBuilder.path("/defaultValues/getDefaultsValues")
                .buildAndExpand(defaultValues.stream().map(ValuesOfRegistedRecord::id).toList()).toUri();
        return ResponseEntity.created(listUri).body(defaultValues);
    }
    @GetMapping
    @RequestMapping(value = "/getDefaultsValues", method = RequestMethod.GET)
    public ResponseEntity<List<DefaultValues>> getDefaultsValues() {
        return ResponseEntity.ok().body(defaultValuesService.getDefaultsValues());
    }
    @GetMapping
    @RequestMapping(value = "/getDefaultsByName/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<DefaultRegisterListRecord>> getValuesByName(@PathVariable String name) {
        return ResponseEntity.ok().body(defaultValuesService.getValuesByName(name));
    }
    @GetMapping()
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ValuesOfRegistedRecord> getValuesById(@PathVariable Long id){
        var defaultValues = repository.getReferenceById(id);

        return ResponseEntity.ok(new ValuesOfRegistedRecord(defaultValues));
    }
    @Transactional
    @DeleteMapping("/deleteValuesById/{id}")
    public ResponseEntity<String> deleteValuesById(@PathVariable Long id){
        defaultValuesService.deleteValuesById(id);

        return ResponseEntity.ok().body("deleted default values by ID. " + id);
    }
    @Transactional
    @DeleteMapping("/deleteValuesByName/{name}")
    public ResponseEntity<String> deleteValuesByName(@PathVariable String name){
        defaultValuesService.deleteValues(name);

        return ResponseEntity.ok().body("deleted default values by name. " + name);
    }
    @Transactional
    @DeleteMapping("/deleteValues/all")
    public ResponseEntity<String> deleteValuesAll( ){
        defaultValuesService.deleteValuesAll();

        return ResponseEntity.ok().body("deleted all default values.");
    }
    @Transactional
    @PutMapping("/patchValues/{id}/{value}")
    public void updateValuesById(@PathVariable Long id, Double value1, Double value2, Double value3, Double value4){

        var patch = repository.getReferenceById(id);

        patch.setNormalSd(value1);
        patch.setHighSd(value2);
        patch.setNormalMean(value3);
        patch.setHighMean(value4);

        repository.save(patch);
    }
}