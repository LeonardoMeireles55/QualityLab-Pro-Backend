package leonardo.labutilities.qualitylabpro.controller.analytics;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import leonardo.labutilities.qualitylabpro.dto.analytics.*;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.service.analytics.AnalyticsHelperService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("generic-analytics")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("generic-analytics")
@Validated
public abstract class GenericAnalyticsController {

    private final AnalyticsHelperService analyticsHelperService;

    public GenericAnalyticsController(AnalyticsHelperService analyticsHelperService) {
        this.analyticsHelperService = analyticsHelperService;
    }

    @GetMapping("results/grouped-by-level")
    public ResponseEntity<List<GenericResultsGroupByLevel>> getGroupedByLevel(
            @RequestParam String name,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<GenericResultsGroupByLevel> groupedData = analyticsHelperService.getGroupedResults(name, startDate, endDate);
        return ResponseEntity.ok(groupedData);
    }

    @GetMapping("/results/mean-deviation/grouped-by-level")
    public ResponseEntity<List<MeanAndStandardDeviationRecordGroupByLevel>> getMeanAndDeviationGrouped(
            @RequestParam String name,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<MeanAndStandardDeviationRecordGroupByLevel> groupedData = analyticsHelperService.generateMeanAndStandardDeviationGrouped(name, startDate, endDate);
        return ResponseEntity.ok(groupedData);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<GenericValuesRecord>> postAnalytics(
        @Valid @RequestBody List<@Valid GenericValuesRecord> values
    ) {
        analyticsHelperService.submitAnalytics(values);
        return ResponseEntity.status(201).build();
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
    public ResponseEntity<CollectionModel<EntityModel<GenericValuesRecord>>> getAllAnalyticsHateoas(
        @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<GenericValuesRecord> resultsList = analyticsHelperService.findAll(pageable);

        List<EntityModel<GenericValuesRecord>> resultModels = resultsList
            .stream()
            .map(result ->
                EntityModel.of(result, linkTo(getClass()).slash(result.id()).withSelfRel())
            )
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                resultModels,
                linkTo(methodOn(getClass()).getAllAnalyticsHateoas(pageable)).withSelfRel()
            )
        );
    }

    @GetMapping("/results/names/date-range")
    public abstract ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsDateBetween(
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    );

    @GetMapping("/results/search/{name}")
    public ResponseEntity<
        CollectionModel<EntityModel<GenericValuesRecord>>
    > getAllAnalyticsByNameOrderByDate(
        @PathVariable String name,
        @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<GenericValuesRecord> resultsList = analyticsHelperService.findAnalyticsByName(
            pageable,
            name
        );

        List<EntityModel<GenericValuesRecord>> resultModels = resultsList
            .stream()
            .map(result ->
                EntityModel.of(
                    result,
                    linkTo(methodOn(getClass()).getAnalyticsById(result.id())).withSelfRel(),
                    linkTo(
                        methodOn(getClass()).getAllAnalyticsByNameOrderByDate(name, pageable)
                    ).withRel("search")
                )
            )
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(
                resultModels,
                linkTo(
                    methodOn(getClass()).getAllAnalyticsByNameOrderByDate(name, pageable)
                ).withSelfRel()
            )
        );
    }

    @GetMapping("/results/date-range")
    public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateBetween(
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    ) {
        return ResponseEntity.ok(analyticsHelperService.findAllAnalyticsByDate(startDate, endDate));
    }

    public abstract ResponseEntity<List<GenericValuesRecord>> getAnalyticsByLevel(
        Pageable pageable,
        @RequestParam String name,
        @RequestParam String level
    );

    public abstract ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateRange(
        @RequestParam String name,
        @RequestParam String level,
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    );

    public abstract ResponseEntity<MeanAndStandardDeviationRecord> getMeanAndStandardDeviation(
        @RequestParam String name,
        @RequestParam String level,
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    );
}
