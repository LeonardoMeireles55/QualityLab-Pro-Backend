package leonardo.labutilities.qualitylabpro.controllers.analytics;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.dtos.analytics.AnalyticsRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GroupedMeanAndStdRecordByLevel;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GroupedResultsByLevel;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStdDeviationRecord;
import leonardo.labutilities.qualitylabpro.services.analytics.AnalyticsHelperService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/generic-analytics")
@RestController()
public abstract class AnalyticsController {

    private final AnalyticsHelperService analyticsHelperService;

    public AnalyticsController(AnalyticsHelperService analyticsHelperService) {
        this.analyticsHelperService = analyticsHelperService;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteAnalyticsResultById(@PathVariable Long id) {
        analyticsHelperService.deleteAnalyticsById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalyticsRecord> getAnalyticsById(@PathVariable Long id) {
        return ResponseEntity.ok(analyticsHelperService.findById(id));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<List<AnalyticsRecord>> postAnalytics(
            @Valid @RequestBody List<@Valid AnalyticsRecord> values) {
        analyticsHelperService.saveNewAnalyticsRecords(values);
        return ResponseEntity.status(201).build();
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<AnalyticsRecord>>> getAllAnalytics(
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        List<AnalyticsRecord> resultsList = analyticsHelperService.findAll(pageable);

        List<EntityModel<AnalyticsRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(getClass()).slash(result.id()).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resultModels,
                linkTo(methodOn(getClass()).getAllAnalytics(pageable)).withSelfRel()));
    }


    @GetMapping("/grouped-by-level")
    public ResponseEntity<List<GroupedResultsByLevel>> getGroupedByLevel(@RequestParam String name,
                                                                         @RequestParam("startDate") LocalDateTime startDate,
                                                                         @RequestParam("endDate") LocalDateTime endDate) {
        List<GroupedResultsByLevel> groupedData =
                analyticsHelperService.findAnalyticsWithGroupedResults(name, startDate, endDate);
        return ResponseEntity.ok(groupedData);
    }

    @GetMapping("/grouped-by-level/mean-deviation")
    public ResponseEntity<List<GroupedMeanAndStdRecordByLevel>> getMeanAndDeviationGrouped(
            @RequestParam String name, @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<GroupedMeanAndStdRecordByLevel> groupedData = analyticsHelperService
                .calculateGroupedMeanAndStandardDeviation(name, startDate, endDate);
        return ResponseEntity.ok(groupedData);
    }


    @GetMapping("/name")
    public ResponseEntity<CollectionModel<EntityModel<AnalyticsRecord>>> getAllAnalyticsByName(
            @RequestParam String name,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        List<AnalyticsRecord> resultsList =
                analyticsHelperService.findAnalyticsByNameWithPagination(pageable, name);

        List<EntityModel<AnalyticsRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(methodOn(getClass()).getAnalyticsById(result.id())).withSelfRel(),
                        linkTo(methodOn(getClass()).getAllAnalyticsByName(name, pageable))
                                .withRel("search")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(resultModels,
                linkTo(methodOn(getClass()).getAllAnalyticsByName(name, pageable)).withSelfRel()));
    }

    @GetMapping("date-range")
    public abstract ResponseEntity<List<AnalyticsRecord>> getAllAnalyticsDateBetween(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate);


    @GetMapping("name-and-level")
    public abstract ResponseEntity<List<AnalyticsRecord>> getAllAnalyticsByNameAndLevel(
            Pageable pageable, @RequestParam String name, @RequestParam String level);

    @GetMapping("name-and-level-date-range")
    public abstract ResponseEntity<List<AnalyticsRecord>> getAllAnalyticsByNameAndLevelDateRange(
            @RequestParam String name, @RequestParam String level,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate);

    @GetMapping("/mean-standard-deviation")
    public abstract ResponseEntity<MeanAndStdDeviationRecord> getMeanAndStandardDeviation(
            @RequestParam String name, @RequestParam String level,
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate);
}
