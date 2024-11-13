package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.model.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.service.AnalyticsHelperService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
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
    public ResponseEntity<ValuesOfLevelsGenericRecord> getAnalyticsById(@PathVariable Long id) {
        GenericAnalytics genericAnalytics = analyticsHelperService.findAnalyticsById(id);
        return ResponseEntity.ok(new ValuesOfLevelsGenericRecord(genericAnalytics));
    }

    @GetMapping("/results")
    public ResponseEntity<CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>>>
    getAllAnalyticsHateoas( @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        List<ValuesOfLevelsGenericRecord> resultsList = analyticsHelperService.findAll(pageable);

        List<EntityModel<ValuesOfLevelsGenericRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(getClass())
                                .slash(result.id())
                                .withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resultModels,
                linkTo(methodOn(getClass()).getAllAnalyticsHateoas(pageable)).withSelfRel()));
    }

    @GetMapping("/results/search/{name}")
    public ResponseEntity<CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>>> getAllAnalyticsByNameOrderByDate(
            @PathVariable String name,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        List<ValuesOfLevelsGenericRecord> resultsList = analyticsHelperService.findAnalyticsByName(pageable, name);

        List<EntityModel<ValuesOfLevelsGenericRecord>> resultModels = resultsList.stream()
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
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByDateBetween(
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(analyticsHelperService.findAllAnalyticsByDate(dateStart, dateEnd));
    }

    public abstract ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAnalyticsByLevel(
            Pageable pageable, @RequestParam String name, @RequestParam String level);

    public abstract ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByDateRange(
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

