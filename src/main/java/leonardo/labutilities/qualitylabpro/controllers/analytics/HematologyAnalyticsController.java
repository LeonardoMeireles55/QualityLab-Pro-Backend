package leonardo.labutilities.qualitylabpro.controllers.analytics;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import leonardo.labutilities.qualitylabpro.constants.AvailableHematologyAnalytics;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStdDeviationRecord;
import leonardo.labutilities.qualitylabpro.services.analytics.HematologyAnalyticsService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/hematology-analytics")
@RestController()
public class HematologyAnalyticsController extends GenericAnalyticsController {

	private final HematologyAnalyticsService hematologyAnalyticsService;

	public HematologyAnalyticsController(HematologyAnalyticsService hematologyAnalyticsService) {
		super(hematologyAnalyticsService);
		this.hematologyAnalyticsService = hematologyAnalyticsService;
	}

	private static final List<String> names =
			new AvailableHematologyAnalytics().availableHematologyAnalytics();


	@Override
	@GetMapping()
	public ResponseEntity<CollectionModel<EntityModel<GenericValuesRecord>>> getAllAnalytics(
			@PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
		List<GenericValuesRecord> resultsList =
				hematologyAnalyticsService.getAllByNameIn(names, pageable);

		List<EntityModel<GenericValuesRecord>> resultModels = resultsList.stream()
				.map(result -> EntityModel.of(result,
						linkTo(getClass()).slash(result.id()).withSelfRel()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(resultModels,
				linkTo(methodOn(getClass()).getAllAnalytics(pageable)).withSelfRel()));
	}

	@Override
	@GetMapping("name-and-level")
	public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByNameAndLevel(
			Pageable pageable, @RequestParam() String name, @RequestParam() String level) {
		return ResponseEntity
				.ok(hematologyAnalyticsService.findAnalyticsByNameAndLevel(pageable, name, level));
	}

	@Override
	@GetMapping("/date-range")
	public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsDateBetween(
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate) {
		List<GenericValuesRecord> resultsList =
				hematologyAnalyticsService.getAllByNameInAndDateBetween(names, startDate, endDate);
		return ResponseEntity.ok(resultsList);
	}

	@Override
	@GetMapping("name-and-level-date-range")
	public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByNameAndLevelDateRange(
			@RequestParam String name, @RequestParam String level,
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate) {
		return ResponseEntity.ok(hematologyAnalyticsService
				.findAllAnalyticsByNameAndLevelAndDate(name, level, startDate, endDate));
	}

	@Override
	@GetMapping("/mean-standard-deviation")
	public ResponseEntity<MeanAndStdDeviationRecord> getMeanAndStandardDeviation(
			@RequestParam String name, @RequestParam String level,
			@RequestParam("startDate") LocalDateTime startDate,
			@RequestParam("endDate") LocalDateTime endDate) {
		return ResponseEntity.ok(hematologyAnalyticsService.calculateMeanAndStandardDeviation(name,
				level, startDate, endDate));
	}
}
