package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.service.CoagulationAnalyticsService;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("coagulation-analytics")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("coagulation-analytics")
public class CoagulationAnalyticsController extends GenericAnalyticsController {

    private final CoagulationAnalyticsService coagulationAnalyticsService;

    public CoagulationAnalyticsController(CoagulationAnalyticsService coagulationAnalyticsService) {
        super(coagulationAnalyticsService);
        this.coagulationAnalyticsService = coagulationAnalyticsService;
    }

    @Override
    @GetMapping("/results/search/level")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getAnalyticsByLevel(Pageable pageable, String name, String level) {
        return ResponseEntity.ok(coagulationAnalyticsService
                .findAllAnalyticsByNameAndLevel(pageable, name, level));
    }

    @Override
    @GetMapping("/results/search/date-range")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(coagulationAnalyticsService
                .findAllAnalyticsByNameAndLevelAndDate
                        (name, level, dateStart, dateEnd));
    }

    @Override
    @GetMapping("/results/mean-standard-deviation")
    public ResponseEntity<MeanAndStandardDeviationRecord> getMeanAndStandardDeviation(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd)
    {
        return ResponseEntity
                .ok(coagulationAnalyticsService.generateMeanAndStandardDeviation(name, level, dateStart, dateEnd));
    }
}
