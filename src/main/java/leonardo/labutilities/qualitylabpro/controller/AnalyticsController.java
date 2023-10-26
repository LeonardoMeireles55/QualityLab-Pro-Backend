package leonardo.labutilities.qualitylabpro.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.analytics.Analytics;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevelsList;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevels;
import leonardo.labutilities.qualitylabpro.services.AnalyticsService;
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
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    @RequestMapping(value = "/getresults/" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsList>> getResultsAll() {
        return ResponseEntity.ok().body(analyticsService.getResultsAll());
    }
    @GetMapping
    @RequestMapping(value = "/getresults/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsList>> getResults(@PathVariable String name) {
        return ResponseEntity.ok().body(analyticsService.getResultsByName(name));
    }
    @GetMapping
    @RequestMapping(value = "/getresultsbynamelevel1/{name}", method = RequestMethod.GET)
        public ResponseEntity<String> getResultsByNameLevel1(@PathVariable String name) {
            return ResponseEntity.ok().body(analyticsService.getResultsByNameLevel1(name));
        }
    @GetMapping
    @RequestMapping(value = "/getresultsbynamelevel2/{name}", method = RequestMethod.GET)
    public ResponseEntity<String> getResultsByNameLevel2(@PathVariable String name) {
        return ResponseEntity.ok().body(analyticsService.getResultsByNameLevel2(name));
    }
    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendvalues", method = RequestMethod.POST)
    public ResponseEntity<Analytics> sendValues(@RequestBody @Valid ValuesOfLevels values, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/getresults/name").build().toUri();
        return ResponseEntity.created(uri).body(analyticsService.sendValues(values));
    }
    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendvalueslist", method = RequestMethod.POST)
    public ResponseEntity<List<Analytics>> sendValuesList(@RequestBody List<ValuesOfLevels> valuesOfLevelsList, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/getresults").build().toUri();
        return ResponseEntity.created(uri).body(analyticsService.sendValuesList(valuesOfLevelsList));
    }
    @Transactional
    @DeleteMapping("/deletevalues/{id}")
    public ResponseEntity<String> deleteValues(@PathVariable Long id){
        analyticsService.deleteValues(id);
        return ResponseEntity.ok().body("data deleted by id");
    }
    @Transactional
    @DeleteMapping("/deletevalues/all")
    public ResponseEntity<String> deleteValuesAll(){
        analyticsService.deleteValuesAll();
        return ResponseEntity.ok().body("All data deleted with successful");
    }
}
