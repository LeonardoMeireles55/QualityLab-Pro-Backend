package leonardo.labutilities.qualitylabpro.controllers.analytics;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.time.LocalDateTime;
import java.util.List;
import leonardo.labutilities.qualitylabpro.constants.AvailableBiochemistryAnalytics;
import leonardo.labutilities.qualitylabpro.dtos.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dtos.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.services.analytics.BiochemistryAnalyticsService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("biochemistry-analytics")
@RequestMapping("biochemistry-analytics")
@SecurityRequirement(name = "bearer-key")
@Validated
public class BiochemistryAnalyticsController extends GenericAnalyticsController {

    private final BiochemistryAnalyticsService biochemistryAnalyticsService;

    public BiochemistryAnalyticsController(
        BiochemistryAnalyticsService biochemistryAnalyticsService
    ) {
        super(biochemistryAnalyticsService);
        this.biochemistryAnalyticsService = biochemistryAnalyticsService;
    }

    @GetMapping("/results/names/date-range")
    public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsDateBetween(
        @RequestParam("startDate") LocalDateTime startDate,
        @RequestParam("endDate") LocalDateTime endDate
    ) {
        AvailableBiochemistryAnalytics names = new AvailableBiochemistryAnalytics();
        List<GenericValuesRecord> resultsList =
            biochemistryAnalyticsService.getAllByNameInAndDateBetween(
                names.availableBioAnalytics(),
                startDate,
                endDate
            );
        return ResponseEntity.ok(resultsList);
    }

    @Override
    @GetMapping("/results/search/name/level")
    public ResponseEntity<List<GenericValuesRecord>> getAnalyticsByLevel(
        Pageable pageable,
        String name,
        String level
    ) {
        return ResponseEntity.ok(
            biochemistryAnalyticsService.findAllAnalyticsByNameAndLevel(pageable, name, level)
        );
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
            biochemistryAnalyticsService.findAllAnalyticsByNameAndLevelAndDate(
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
            biochemistryAnalyticsService.generateMeanAndStandardDeviation(
                name,
                level,
                startDate,
                endDate
            )
        );
    }
}
