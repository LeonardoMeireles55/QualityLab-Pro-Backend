package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.main.Integra400;
import leonardo.labutilities.qualitylabpro.records.integra.ValuesOfLevelsIntegra;
import leonardo.labutilities.qualitylabpro.services.Integra400Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/integra")
public class IntegraController {
    private final Integra400Service integra400Service;
    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendvalues", method = RequestMethod.POST)
    public ResponseEntity<List<Integra400>> sendValues
            (@RequestBody @Valid List<ValuesOfLevelsIntegra> values) throws ParseException {
        List<Integra400> valuesOfLevelsIntegras = integra400Service.sendValues(values);
        return ResponseEntity.ok().body(valuesOfLevelsIntegras);
    }
    @GetMapping
    @RequestMapping(value = "/getresults" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsIntegra>> getResults(Pageable pageable) {
        return ResponseEntity.ok().body(integra400Service.getResults(pageable).getContent());
    }
    @GetMapping
    @RequestMapping(value = "/getresultsbyname/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsIntegra>> getResultsByName(@PathVariable String name) {
        return ResponseEntity.ok().body(integra400Service.getResultsByName(name));
    }

    @GetMapping
    @RequestMapping(value = "/getresultsbyname/order-asc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsIntegra>> getResultsByNameOrderByDateAsc(@PathVariable String name) {
        return ResponseEntity.ok().body(integra400Service.getResultsByDateAsc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getresultsbyname/order-desc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsIntegra>> getResultsByNameOrderByDateDesc(@PathVariable String name) {
        return ResponseEntity.ok().body(integra400Service.getResultsByDateDesc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getresultsbyname-level/{name}/{level}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsIntegra>> getResultsByLevel
            (@PathVariable String name, @PathVariable String level) {
        return ResponseEntity.ok().body(integra400Service.getResultsByNameAndLevel(name, level));
    }


}
