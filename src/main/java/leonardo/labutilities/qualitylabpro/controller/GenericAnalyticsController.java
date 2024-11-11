package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.dto.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/analytics")
@Validated
@Slf4j
public class GenericAnalyticsController {
    private final AnalyticsService analyticsService;
    public GenericAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<List<GenericAnalytics>> sendValues(
            @RequestBody List<@Valid ValuesOfLevelsGenericRecord> values) {
        List<GenericAnalytics> valuesOfGenericsList = analyticsService.submitAnalyticsValues(values);

        if (valuesOfGenericsList.isEmpty()) {
            throw new ErrorHandling.DataIntegrityViolationException();
        }

        return ResponseEntity.ok(valuesOfGenericsList);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAnalyticsResultById(@PathVariable Long id) {
        analyticsService.removeAnalyticsById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericAnalytics> getResultsById(@PathVariable Long id) {
        GenericAnalytics defaultValues = analyticsService.findResultsById(id);
        return ResponseEntity.ok(defaultValues);
    }

    @GetMapping("/results")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResults(Pageable pageable) {
        return ResponseEntity.ok(analyticsService.listAllResults(pageable));
    }

    @GetMapping("/results/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>>> getAllResultsHateoas(Pageable pageable) {
        List<ValuesOfLevelsGenericRecord> resultsList = analyticsService.listAllResults(pageable);

        List<EntityModel<ValuesOfLevelsGenericRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(methodOn(GenericAnalyticsController.class)
                                .getAllResultsByName(Pageable.unpaged(), result.name()))
                                .withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>> collectionModel =
                CollectionModel.of(resultModels,
                        linkTo(methodOn(GenericAnalyticsController.class).getAllResultsHateoas(pageable))
                                .withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/results/search")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResultsByName(
            Pageable pageable, @RequestParam String name) {
        return ResponseEntity.ok(analyticsService.findResultsByName(pageable, name));
    }

    @GetMapping("/results/search/order/{order}")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResultsByNameOrderByDate(
            @RequestParam String name,
            @PathVariable String order) {

        List<ValuesOfLevelsGenericRecord> results = analyticsService.findResultsByNameOrderedByDate(name, order);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/search/level")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getResultsByLevel(
            Pageable pageable, @RequestParam String name, @RequestParam String level) {
        return ResponseEntity.ok(analyticsService.getAllResultsByNameAndLevel(pageable, name, level));
    }

    @GetMapping("/results/search/date-range")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResultsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(analyticsService.getAllResultsByNameAndLevelAndDate(name, level, dateStart, dateEnd));
    }

    @GetMapping("/results/date-range")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResultsByDateBetween(
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(analyticsService.getAllResultsByDate(dateStart, dateEnd));
    }
}
