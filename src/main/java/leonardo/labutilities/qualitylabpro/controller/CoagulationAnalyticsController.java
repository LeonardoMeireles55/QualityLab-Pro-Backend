package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import leonardo.labutilities.qualitylabpro.dto.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import leonardo.labutilities.qualitylabpro.service.CoagulationAnalyticsService;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/coagulation")
@Validated
@Slf4j
public class CoagulationAnalyticsController extends GenericAnalyticsController {
    private final CoagulationAnalyticsService coagulationAnalyticsService;

    public CoagulationAnalyticsController(CoagulationAnalyticsService coagulationAnalyticsService,
                                          AnalyticsService analyticsService) {
        super(coagulationAnalyticsService);
        this.coagulationAnalyticsService = coagulationAnalyticsService;
    }

    @Override
    @GetMapping("/results/search/date-range")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResultsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(coagulationAnalyticsService
                .getAllResultsByNameAndLevelAndDate(name, level, dateStart, dateEnd));
    }
}
