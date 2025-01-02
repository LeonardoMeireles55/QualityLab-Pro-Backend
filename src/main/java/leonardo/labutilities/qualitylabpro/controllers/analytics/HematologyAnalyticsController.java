package leonardo.labutilities.qualitylabpro.controllers.analytics;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import java.util.List;
import leonardo.labutilities.qualitylabpro.constants.AvailableHematologyAnalytics;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.services.analytics.HematologyAnalyticsService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("hematology-analytics")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/hematology-analytics")
@Validated
public class HematologyAnalyticsController extends GenericAnalyticsController {

    private final HematologyAnalyticsService hematologyAnalyticsService;

    public HematologyAnalyticsController(HematologyAnalyticsService hematologyAnalyticsService) {
        super(hematologyAnalyticsService);
        this.hematologyAnalyticsService = hematologyAnalyticsService;
    }

    @Override
    @GetMapping("/results/search/name/level")
    public ResponseEntity<List<GenericValuesRecord>> getAnalyticsByLevel(
        Pageable pageable,
        String name,
        String level
    ) {
        return ResponseEntity.ok(
            hematologyAnalyticsService.findAllAnalyticsByNameAndLevel(pageable, name, level)
        );
    }

    @GetMapping("/results/names/date-range")
    public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsDateBetween(
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    ) {
        AvailableHematologyAnalytics names = new AvailableHematologyAnalytics();
        List<GenericValuesRecord> resultsList =
            hematologyAnalyticsService.getAllByNameInAndDateBetween(
                names.availableHematologyAnalytics(),
                startDate,
                endDate
            );
        return ResponseEntity.ok(resultsList);
    }

    @Override
    @GetMapping("/results/search/date-range")
    public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateRange(
        @RequestParam String name,
        @RequestParam String level,
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    ) {
        return ResponseEntity.ok(
            hematologyAnalyticsService.findAllAnalyticsByNameAndLevelAndDate(
                name,
                level,
                startDate,
                endDate
            )
        );
    }

    @Override
    @GetMapping("/results/mean-standard-deviation")
    public ResponseEntity<MeanAndStandardDeviationRecord> getMeanAndStandardDeviation(
        @RequestParam String name,
        @RequestParam String level,
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    ) {
        return ResponseEntity.ok(
            hematologyAnalyticsService.generateMeanAndStandardDeviation(
                name,
                level,
                startDate,
                endDate
            )
        );
    }
}
