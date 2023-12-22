package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ValidateOnExecution;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.main.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfLevelsGeneric;
import leonardo.labutilities.qualitylabpro.services.GenericAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/bio")
@Validated
public class GenericAnalyticsController {
    private final GenericAnalyticsService genericAnalyticsService;

    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendValues", method = RequestMethod.POST)
    public ResponseEntity<List<GenericAnalytics>> sendValues
            (@RequestBody List<@Valid ValuesOfLevelsGeneric> values) {
        Stream<GenericAnalytics> valuesOfGenericsList = genericAnalyticsService.sendValues(values);
        var response = ResponseEntity.ok().body(valuesOfGenericsList.toList());
        if(!response.getBody().isEmpty()) {
            return response;
        }
        throw new ErrorHandling.DataIntegrityViolationException();
    }
    @GetMapping
    @RequestMapping(value = "/getResults" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGeneric>> getResults(Pageable pageable) {
        return ResponseEntity.ok().body(genericAnalyticsService.getAllResults(pageable).getContent());
    }
    @GetMapping
    @RequestMapping(value = "/getResultsByName/{name}" , method = RequestMethod.GET)
    public ResponseEntity<Stream<ValuesOfLevelsGeneric>> getResultsByName(Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByName(pageable, name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName/orderAsc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<Stream<ValuesOfLevelsGeneric>> getResultsByNameOrderByDateAsc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateAsc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName/orderDesc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<Stream<ValuesOfLevelsGeneric>> getResultsByNameOrderByDateDesc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateDesc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByNameLevel/{name}/{level}" , method = RequestMethod.GET)
    public ResponseEntity<Stream<ValuesOfLevelsGeneric>> getResultsByLevel
            (Pageable pageable, @PathVariable String name, @PathVariable String level) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByNameAndLevel(pageable, name, level));
    }
}
