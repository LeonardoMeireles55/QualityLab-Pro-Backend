package leonardo.labutilities.qualitylabpro.controllers.analytics;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import leonardo.labutilities.qualitylabpro.dtos.analytics.*;
import leonardo.labutilities.qualitylabpro.entities.GenericAnalytics;
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

@Validated
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/generic-analytics")
@RestController()
public abstract class GenericAnalyticsController {

	private final AnalyticsHelperService analyticsHelperService;

	public GenericAnalyticsController(AnalyticsHelperService analyticsHelperService) {
		this.analyticsHelperService = analyticsHelperService;
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Void> deleteAnalyticsResultById(@PathVariable Long id) {
		analyticsHelperService.deleteAnalyticsById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<GenericValuesRecord> getAnalyticsById(@PathVariable Long id) {
		GenericAnalytics genericAnalytics = analyticsHelperService.findById(id);
		return ResponseEntity.ok(new GenericValuesRecord(genericAnalytics));
	}

	@PostMapping
	@Transactional
	public ResponseEntity<List<GenericValuesRecord>> postAnalytics(
			@Valid @RequestBody List<@Valid GenericValuesRecord> values) {
		analyticsHelperService.saveNewAnalyticsRecords(values);
		return ResponseEntity.status(201).build();
	}

	@GetMapping()
	public ResponseEntity<CollectionModel<EntityModel<GenericValuesRecord>>> getAllAnalytics(
			@PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
		List<GenericValuesRecord> resultsList = analyticsHelperService.findAll(pageable);

		List<EntityModel<GenericValuesRecord>> resultModels = resultsList.stream()
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
	public ResponseEntity<CollectionModel<EntityModel<GenericValuesRecord>>> getAllAnalyticsByName(
			@RequestParam String name,
			@PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
		List<GenericValuesRecord> resultsList =
				analyticsHelperService.findAnalyticsByNameWithPagination(pageable, name);

		List<EntityModel<GenericValuesRecord>> resultModels = resultsList.stream()
				.map(result -> EntityModel.of(result,
						linkTo(methodOn(getClass()).getAnalyticsById(result.id())).withSelfRel(),
						linkTo(methodOn(getClass()).getAllAnalyticsByName(name, pageable))
								.withRel("search")))
				.collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(resultModels,
				linkTo(methodOn(getClass()).getAllAnalyticsByName(name, pageable)).withSelfRel()));
	}

	@GetMapping("date-range")
	public abstract ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsDateBetween(
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate);


	@GetMapping("name-and-level")
	public abstract ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByNameAndLevel(
			Pageable pageable, @RequestParam String name, @RequestParam String level);

	@GetMapping("name-and-level-date-range")
	public abstract ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByNameAndLevelDateRange(
			@RequestParam String name, @RequestParam String level,
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate);

	@GetMapping("/mean-standard-deviation")
	public abstract ResponseEntity<MeanAndStdDeviationRecord> getMeanAndStandardDeviation(
			@RequestParam String name, @RequestParam String level,
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate);
}
