package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.service.AnalyticsHelperService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<List<GenericValuesRecord>>
    postAnalytics(@Valid @RequestBody List<@Valid GenericValuesRecord> values) {
        List<GenericValuesRecord> valuesOfGenericsList = analyticsHelperService.submitAnalytics(values);
        return ResponseEntity.ok(valuesOfGenericsList);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAnalyticsResultById(@PathVariable Long id) {
        analyticsHelperService.removeAnalyticsById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericValuesRecord> getAnalyticsById(@PathVariable Long id) {
        GenericAnalytics genericAnalytics = analyticsHelperService.findAnalyticsById(id);
        return ResponseEntity.ok(new GenericValuesRecord(genericAnalytics));
    }

    @GetMapping("/results")
    public ResponseEntity<CollectionModel<EntityModel<GenericValuesRecord>>>
    getAllAnalyticsHateoas( @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        List<GenericValuesRecord> resultsList = analyticsHelperService.findAll(pageable);

        List<EntityModel<GenericValuesRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(getClass())
                                .slash(result.id())
                                .withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resultModels,
                linkTo(methodOn(getClass()).getAllAnalyticsHateoas(pageable)).withSelfRel()));
    }

    @GetMapping("/results/names/date-range")
    public abstract ResponseEntity<List<GenericValuesRecord>>
    getAllAnalyticsDateBetween(@RequestParam String startDate,
                               @RequestParam String endDate,
                               Pageable pageable);

    @GetMapping("/results/search/{name}")
    public ResponseEntity<CollectionModel<EntityModel<GenericValuesRecord>>> getAllAnalyticsByNameOrderByDate(
            @PathVariable String name,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        List<GenericValuesRecord> resultsList = analyticsHelperService.findAnalyticsByName(pageable, name);

        List<EntityModel<GenericValuesRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(methodOn(getClass())
                                .getAnalyticsById(result.id()))
                                .withSelfRel(),
                        linkTo(methodOn(getClass())
                                .getAllAnalyticsByNameOrderByDate(name, pageable))
                                .withRel("search")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(resultModels,
                        linkTo(methodOn(getClass())
                                .getAllAnalyticsByNameOrderByDate(name, pageable))
                                .withSelfRel()));
    }

    @GetMapping("/results/date-range")
    public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateBetween(
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(analyticsHelperService.findAllAnalyticsByDate(dateStart, dateEnd));
    }

    public abstract ResponseEntity<List<GenericValuesRecord>> getAnalyticsByLevel(
            Pageable pageable, @RequestParam String name, @RequestParam String level);

    public abstract ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd);

    public abstract ResponseEntity<MeanAndStandardDeviationRecord> getMeanAndStandardDeviation(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd);
}

