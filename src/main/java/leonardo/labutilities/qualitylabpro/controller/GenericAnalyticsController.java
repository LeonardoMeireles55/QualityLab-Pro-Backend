package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.service.AnalyticsHelperService;
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

@RestController("generic-analytics")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("generic-analytics")
@Validated()
public abstract class GenericAnalyticsController {

    private final AnalyticsHelperService analyticsHelperService;

    public GenericAnalyticsController(AnalyticsHelperService analyticsHelperService) {
        this.analyticsHelperService = analyticsHelperService;
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<List<GenericAnalytics>>
    postAnalytics(@Valid @RequestBody List<@Valid ValuesOfLevelsGenericRecord> values) {
        List<GenericAnalytics> valuesOfGenericsList = analyticsHelperService.submitAnalytics(values);
        return ResponseEntity.ok(valuesOfGenericsList);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAnalyticsResultById(@PathVariable Long id) {
        analyticsHelperService.removeAnalyticsById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericAnalytics> getResultsById(@PathVariable Long id) {
        GenericAnalytics defaultValues = analyticsHelperService.findAnalyticsById(id);
        return ResponseEntity.ok(defaultValues);
    }

    @GetMapping("/results")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAnalytics(Pageable pageable) {
        return ResponseEntity.ok(analyticsHelperService.findAll(pageable));
    }

    @GetMapping("/results/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>>> getAllAnalyticsHateoas(Pageable pageable) {
        List<ValuesOfLevelsGenericRecord> resultsList = analyticsHelperService.findAll(pageable);

        List<EntityModel<ValuesOfLevelsGenericRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(methodOn(GenericAnalyticsController.class)
                                .getAllAnalyticsByName(Pageable.unpaged(), result.name()))
                                .withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>> collectionModel =
                CollectionModel.of(resultModels,
                        linkTo(methodOn(GenericAnalyticsController.class).getAllAnalyticsHateoas(pageable))
                                .withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/results/search/{name}")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByName(
            Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok(analyticsHelperService.findAnalyticsByName(pageable, name));
    }

    @GetMapping("/results/search/{name}/{order}")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByNameOrderByDate(
            @PathVariable String name,
            @PathVariable String order) {

        List<ValuesOfLevelsGenericRecord> results = analyticsHelperService.findAnalyticsByNameAndOrderByDate(name, order);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/date-range")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByDateBetween(
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(analyticsHelperService.findAllAnalyticsByDate(dateStart, dateEnd));
    }

    public abstract ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAnalyticsByLevel(
            Pageable pageable, @RequestParam String name, @RequestParam String level);

    protected abstract ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd);
}
