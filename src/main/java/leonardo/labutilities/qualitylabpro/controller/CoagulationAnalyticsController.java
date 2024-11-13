package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.service.CoagulationAnalyticsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("coagulation-analytics")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("coagulation-analytics")
public class CoagulationAnalyticsController extends GenericAnalyticsController {

    private final CoagulationAnalyticsService coagulationAnalyticsService;

    public CoagulationAnalyticsController(CoagulationAnalyticsService coagulationAnalyticsService) {
        super(coagulationAnalyticsService);
        this.coagulationAnalyticsService = coagulationAnalyticsService;
    }

    @GetMapping("/results/search/level")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getAnalyticsByLevel(Pageable pageable, String name, String level) {
        return ResponseEntity.ok(coagulationAnalyticsService
                .findAllAnalyticsByNameAndLevel(pageable, name, level));
    }

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
}
