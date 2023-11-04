package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegisterList;
import leonardo.labutilities.qualitylabpro.analytics.DefaultValues;
import leonardo.labutilities.qualitylabpro.repositories.DefaultValuesRepository;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfRegisted;
import leonardo.labutilities.qualitylabpro.records.defaultvalues.DefaultRegister;
import leonardo.labutilities.qualitylabpro.services.DefaultValuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;


@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/defaultsvalues")
public class DefaultValuesController {
    private final DefaultValuesRepository repository;
    private final DefaultValuesService defaultValuesService;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<DefaultValues> registerDefaultValues
            (@RequestBody @Valid DefaultRegister values, UriComponentsBuilder uriComponentsBuilder) {
        DefaultValues defaultValues = defaultValuesService.register(values);
        URI uri = uriComponentsBuilder.path("/defaultsvalues/{id}").buildAndExpand(defaultValues.getId()).toUri();
        return ResponseEntity.created(uri).body(defaultValues);
    }
    @Transactional
    @PostMapping("/listregister")
    public ResponseEntity<List<ValuesOfRegisted>> listRegisterDefaultValues
            (@RequestBody @Valid List<DefaultRegister> defaultRegisters, UriComponentsBuilder uriComponentsBuilder) {

        List<ValuesOfRegisted> defaultValues = defaultValuesService.listRegister(defaultRegisters);
        URI listUri = uriComponentsBuilder.path("/defaultvalues/getdefaultsvalues")
                .buildAndExpand(defaultValues.stream().map(s -> s.id()).toList()).toUri();
        return ResponseEntity.created(listUri).body(defaultValues);
    }
    @GetMapping
    @RequestMapping(value = "/getdefaultsvalues", method = RequestMethod.GET)
    public ResponseEntity<List<DefaultValues>> getDefaultsValues() {
        return ResponseEntity.ok().body(defaultValuesService.getDefaultsValues());
    }
    @GetMapping
    @RequestMapping(value = "/getdefaultsbyname/{name}", method = RequestMethod.GET)
    public ResponseEntity<List<DefaultRegisterList>> getValuesByName(@PathVariable String name) {
        return ResponseEntity.ok().body(defaultValuesService.getValuesByName(name));
    }
    @GetMapping()
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ValuesOfRegisted> getValuesById(@PathVariable Long id){
        var defaultValues = repository.getReferenceById(id);

        return ResponseEntity.ok(new ValuesOfRegisted(defaultValues));
    }
    @Transactional
    @DeleteMapping("/deletevaluesbyid/{id}")
    public ResponseEntity<String> deleteValuesById(@PathVariable Long id){
        defaultValuesService.deleteValuesById(id);

        return ResponseEntity.ok().body("deleted default values by ID. " + id);
    }
    @Transactional
    @DeleteMapping("/deletevaluesbyname/{name}")
    public ResponseEntity<String> deleteValuesByName(@PathVariable String name){
        defaultValuesService.deleteValues(name);

        return ResponseEntity.ok().body("deleted default values by name. " + name);
    }
    @Transactional
    @DeleteMapping("/deletevalues/all")
    public ResponseEntity<String> deleteValuesAll( ){
        defaultValuesService.deleteValuesAll();

        return ResponseEntity.ok().body("deleted all default values.");
    }
    @Transactional
    @PutMapping("/patchvalues/{id}/{value}")
    public void updateValuesById(@PathVariable Long id, Double value1, Double value2, Double value3, Double value4){

        var patch = repository.getReferenceById(id);

        patch.setNormalDp(value1);
        patch.setHighDp(value2);
        patch.setNormalMean(value3);
        patch.setHighMean(value4);
    }
}