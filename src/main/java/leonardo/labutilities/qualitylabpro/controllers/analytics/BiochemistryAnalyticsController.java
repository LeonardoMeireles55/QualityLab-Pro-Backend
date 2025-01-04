package leonardo.labutilities.qualitylabpro.controllers.analytics;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import leonardo.labutilities.qualitylabpro.constants.AvailableBiochemistryAnalytics;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStdDeviationRecord;
import leonardo.labutilities.qualitylabpro.services.analytics.BiochemistryAnalyticsService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("biochemistry-analytics")
@RequestMapping("biochemistry-analytics")
@SecurityRequirement(name = "bearer-key")
@Validated
public class BiochemistryAnalyticsController extends GenericAnalyticsController {

        private final BiochemistryAnalyticsService biochemistryAnalyticsService;

        private static final List<String> names = new AvailableBiochemistryAnalytics().availableBioAnalytics();

        public BiochemistryAnalyticsController(
                        BiochemistryAnalyticsService biochemistryAnalyticsService) {
                super(biochemistryAnalyticsService);
                this.biochemistryAnalyticsService = biochemistryAnalyticsService;
        }
        @Override
        @GetMapping()
        public ResponseEntity<CollectionModel<EntityModel<GenericValuesRecord>>> getAllAnalyticsHateoas(
                @PageableDefault(sort = "date",
                        direction = Sort.Direction.DESC) Pageable pageable) {
                List<GenericValuesRecord> resultsList = biochemistryAnalyticsService.getAllByNameIn(names,pageable);

                List<EntityModel<GenericValuesRecord>> resultModels = resultsList.stream()
                        .map(result -> EntityModel.of(result,
                                linkTo(getClass()).slash(result.id())
                                        .withSelfRel()))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(CollectionModel.of(resultModels,
                        linkTo(methodOn(getClass()).getAllAnalyticsHateoas(pageable))
                                .withSelfRel()));
        }
        @GetMapping("/results/names/date-range")
        public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsDateBetween(
                        @RequestParam("startDate") LocalDateTime startDate,
                        @RequestParam("endDate") LocalDateTime endDate) {
                List<GenericValuesRecord> resultsList =
                                biochemistryAnalyticsService.getAllByNameInAndDateBetween(
                                                names, startDate, endDate);
                return ResponseEntity.ok(resultsList);
        }


        @Override
        @GetMapping("/results/search/name/level")
        public ResponseEntity<List<GenericValuesRecord>> getAnalyticsByLevel(Pageable pageable,
                        String name, String level) {
                return ResponseEntity.ok(biochemistryAnalyticsService
                                .findAnalyticsByNameAndLevel(pageable, name, level));
        }

        @Override
        @GetMapping("/results/search/date-range")
        public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateRange(
                        @RequestParam String name, @RequestParam String level,
                        @RequestParam("startDate") LocalDateTime startDate,
                        @RequestParam("endDate") LocalDateTime endDate) {
                return ResponseEntity.ok(
                                biochemistryAnalyticsService.findAllAnalyticsByNameAndLevelAndDate(
                                                name, level, startDate, endDate));
        }

        @Override
        @GetMapping("/results/mean-standard-deviation")
        public ResponseEntity<MeanAndStdDeviationRecord> getMeanAndStandardDeviation(
                        @RequestParam String name, @RequestParam String level,
                        @RequestParam("startDate") LocalDateTime startDate,
                        @RequestParam("endDate") LocalDateTime endDate) {
                return ResponseEntity.ok(biochemistryAnalyticsService
                                .calculateMeanAndStandardDeviation(name, level, startDate, endDate));
        }
}
