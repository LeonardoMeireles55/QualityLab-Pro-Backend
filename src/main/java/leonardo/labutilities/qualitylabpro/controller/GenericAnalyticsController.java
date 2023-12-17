package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.main.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGeneric;
import leonardo.labutilities.qualitylabpro.services.GenericAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/bio")
public class GenericAnalyticsController {
    private final GenericAnalyticsService genericAnalyticsService;
    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendValues", method = RequestMethod.POST)
    public ResponseEntity<List<GenericAnalytics>> sendValues
            (@RequestBody @Valid List<ValuesOfLevelsGeneric> values) {
        List<GenericAnalytics> valuesOfLevelsIntegras = genericAnalyticsService.sendValues(values);
        return ResponseEntity.ok().body(valuesOfLevelsIntegras);
    }
    @GetMapping
    @RequestMapping(value = "/getResults" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGeneric>> getResults(Pageable pageable) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResults(pageable).getContent());
    }
    @GetMapping
    @RequestMapping(value = "/getResultsByName/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGeneric>> getResultsByName(Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByName(pageable, name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName/order-asc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGeneric>> getResultsByNameOrderByDateAsc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateAsc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName/order-desc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGeneric>> getResultsByNameOrderByDateDesc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateDesc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName-level/{name}/{level}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGeneric>> getResultsByLevel
            (Pageable pageable, @PathVariable String name, @PathVariable String level) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByNameAndLevel(pageable, name, level));
    }
}
