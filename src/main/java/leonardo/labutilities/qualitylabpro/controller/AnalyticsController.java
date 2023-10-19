package leonardo.labutilities.qualitylabpro.controller;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.analytics.Analytics;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevelsList;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevels;
import leonardo.labutilities.qualitylabpro.repositories.AnalyticRepository;
import leonardo.labutilities.qualitylabpro.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static leonardo.labutilities.qualitylabpro.analytics.Analytics.analyticsHashMap;
@RequiredArgsConstructor
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    @RequestMapping("/getresults/")
    public ResponseEntity<List<ValuesOfLevelsList>> getResultsAll() {
        return ResponseEntity.ok().body(analyticsService.getResultsAll());
    }
    @GetMapping
    @RequestMapping("/getresults/{name}")
    public ResponseEntity<List<ValuesOfLevelsList>> getResults(@PathVariable String name) {
        return ResponseEntity.ok().body(analyticsService.getResultsByName(name));
    }
    @GetMapping
    @RequestMapping("/getresultsbynamelevel1/{name}")
        public ResponseEntity<String> getResultsByNameLevel1(@PathVariable String name) {
            return ResponseEntity.ok().body(analyticsService.getResultsByNameLevel1(name));
        }
    @GetMapping
    @RequestMapping("/getresultsbynamelevel2/{name}")
    public ResponseEntity<String> getResultsByNameLevel2(@PathVariable String name) {
        return ResponseEntity.ok().body(analyticsService.getResultsByNameLevel2(name));
    }
    @PostMapping
    @Transactional
    @RequestMapping("/sendvalues")
    public ResponseEntity<Analytics> sendValues(@RequestBody @Valid ValuesOfLevels values) {
        return ResponseEntity.ok().body(analyticsService.sendValues(values));
    }
    @PostMapping
    @Transactional
    @RequestMapping("/sendvalueslist")
    public ResponseEntity<List<Analytics>> sendValuesList(@RequestBody List<ValuesOfLevels> valuesOfLevelsList) {
        return ResponseEntity.ok().body(analyticsService.sendValuesList(valuesOfLevelsList));
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
