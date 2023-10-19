package leonardo.labutilities.qualitylabpro.controller;

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
@RequestMapping("/defaultsvalues")
public class DefaultValuesController {
    private final DefaultValuesRepository repository;
    private final DefaultValuesService defaultValuesService;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<DefaultValues> register(@RequestBody @Valid DefaultRegister values, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/defaultvalues/{id}").build().toUri();
        return ResponseEntity.created(uri).body(defaultValuesService.register(values));
    }
    @Transactional
    @PostMapping("/listregister")
    public ResponseEntity<List<ValuesOfRegisted>> listRegister(@RequestBody @Valid List<DefaultRegister> defaultRegisters, UriComponentsBuilder uriComponentsBuilder) {
        var registered = defaultValuesService.listRegister(defaultRegisters);
        URI listUri = uriComponentsBuilder.path("/defaultvalues/getdefaultsvalues").build().toUri();
        return ResponseEntity.created(listUri).body(registered);
    }

    @RequestMapping("/getdefaultsvalues")
    public ResponseEntity<List<DefaultValues>> getDefaultsValues() {
        return ResponseEntity.ok().body(defaultValuesService.getDefaultsValues());
    }
    @GetMapping
    @RequestMapping("/getdefaultsbyname/{name}")
    public ResponseEntity<List<DefaultRegisterList>> getValuesByName(@PathVariable String name) {
        return ResponseEntity.ok().body(defaultValuesService.getValuesByName(name));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ValuesOfRegisted> getValuesById(@PathVariable Long id){
        var defaultValues = repository.getReferenceById(id);

        return ResponseEntity.ok(new ValuesOfRegisted(defaultValues));
    }
    @Transactional
    @DeleteMapping("/deletevaluesbyname/{name}")
    public ResponseEntity<String> deleteValues(@PathVariable String name){
        repository.deleteByName(name);

        return ResponseEntity.noContent().build();
    }
    @Transactional
    @DeleteMapping("/deletevalues/all")
    public void deleteValuesAll( ){
        repository.deleteAll();
    }
    @Transactional
    @PutMapping("/patchvalues/{id}/{value}")
    public void updateValues(@PathVariable Long id, Double value){

        var patch = repository.getReferenceById(id);

        patch.setNormalDp(value);
    }
}