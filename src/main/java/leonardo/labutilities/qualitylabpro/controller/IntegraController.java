package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.main.Integra400;
import leonardo.labutilities.qualitylabpro.records.integra.ValuesOfLevelsIntegraDTO;
import leonardo.labutilities.qualitylabpro.records.integra.ValuesOfLevelsIntegra;
import leonardo.labutilities.qualitylabpro.services.Integra400Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
            (@RequestBody @Valid List<ValuesOfLevelsIntegra> values) {
        List<Integra400> valuesOfLevelsIntegras = integra400Service.sendValues(values);
        return ResponseEntity.ok().body(valuesOfLevelsIntegras);
    }
    @GetMapping
    @RequestMapping(value = "/getresultsbyname/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsIntegra>> getResultsByName(@PathVariable String name) {
        return ResponseEntity.ok().body(integra400Service.getResultsByName(name));
    }

    @GetMapping
    @RequestMapping(value = "/getresultsbylevel/{name}/{level}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsIntegra>> getResultsByLevel(@PathVariable String name, @PathVariable String level) {
        return ResponseEntity.ok().body(integra400Service.getResultsByLevel(name, level));
    }
}
