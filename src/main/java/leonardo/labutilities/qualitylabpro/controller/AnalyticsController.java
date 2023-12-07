package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.main.Analytics;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevelsDTO;
import leonardo.labutilities.qualitylabpro.records.valuesOf.ValuesOfLevelsListDTO;
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
    @RequestMapping(value = "/getresults/" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsListDTO>> getResultsAll(Pageable pageable) {
        return ResponseEntity.ok().body(analyticsService.getResultsAll(pageable));
    }
    @GetMapping
    @RequestMapping(value = "/getresultsbyname/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsListDTO>> getResults(Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok().body(analyticsService.getResultsByName(pageable, name));
    }
    @GetMapping
    @RequestMapping(value = "/getresultsbynamelevel/{name}", method = RequestMethod.GET)
        public ResponseEntity<List<ValuesOfLevelsListDTO>> getResultsByNameLevel
            (Pageable pageable, @PathVariable String name, @PathVariable String level) {
            return ResponseEntity.ok().body(analyticsService.getResultsByNameLevel(pageable, name, level));
        }

    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendvalues", method = RequestMethod.POST)
    public ResponseEntity<Analytics> sendValues
            (@RequestBody @Valid ValuesOfLevelsDTO values, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/getresults/name").build().toUri();
        return ResponseEntity.created(uri).body(analyticsService.sendValues(values));
    }

    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendvalueslist", method = RequestMethod.POST)
    public ResponseEntity<List<Analytics>> sendValuesList
            (@RequestBody List<ValuesOfLevelsDTO> valuesOfLevelsDTOList, UriComponentsBuilder uriComponentsBuilder) {
        URI uri = uriComponentsBuilder.path("/getresults").build().toUri();
        return ResponseEntity.created(uri).body(analyticsService.sendValuesList(valuesOfLevelsDTOList));
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