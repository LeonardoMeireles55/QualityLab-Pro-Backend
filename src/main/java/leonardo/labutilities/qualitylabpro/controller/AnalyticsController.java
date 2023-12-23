package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.domain.entitys.Analytics;
import leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics.ValuesOfLevelsRecord;
import leonardo.labutilities.qualitylabpro.records.valuesOfAnalytics.ValuesOfLevelsListRecord;
import leonardo.labutilities.qualitylabpro.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    @RequestMapping(value = "/getResults/" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsListRecord>> getResultsAll(Pageable pageable) {
        return ResponseEntity.ok().body(analyticsService.getResultsAll(pageable));
    }
    @GetMapping
    @RequestMapping(value = "/getResultsByName/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsListRecord>> getResultsByName(Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok().body(analyticsService.getResultsByName(pageable, name));
    }

    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendValues", method = RequestMethod.POST)
    public ResponseEntity<Analytics> sendValues
            (@RequestBody @Valid ValuesOfLevelsRecord values, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/getresults/name").build().toUri();
        return ResponseEntity.created(uri).body(analyticsService.sendValues(values));
    }

    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendValuesList", method = RequestMethod.POST)
    public ResponseEntity<List<Analytics>> sendValuesList
            (@RequestBody List<ValuesOfLevelsRecord> valuesOfLevelsRecordList, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/getresults").build().toUri();
        return ResponseEntity.created(uri).body(analyticsService.sendValuesList(valuesOfLevelsRecordList));
    }
    @Transactional
    @DeleteMapping(value = "/deleteValues/{id}")
    public ResponseEntity<String> deleteValues(@PathVariable Long id){
        analyticsService.deleteValues(id);
        return ResponseEntity.ok().body("data deleted by id");
    }
    @Transactional
    @DeleteMapping(value = "/deleteValues/all")
    public ResponseEntity<String> deleteValuesAll(){
        analyticsService.deleteValuesAll();
        return ResponseEntity.ok().body("All data deleted with successful");
    }
}