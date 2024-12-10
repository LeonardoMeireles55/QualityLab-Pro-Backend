package leonardo.labutilities.qualitylabpro.controller.analytics;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import leonardo.labutilities.qualitylabpro.constants.AvailableHematologyAnalytics;
import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.service.analytics.HematologyAnalyticsService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController("hematology-analytics")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/hematology-analytics")
public class HematologyAnalyticsController extends GenericAnalyticsController {

    private final HematologyAnalyticsService hematologyAnalyticsService;

    public HematologyAnalyticsController(HematologyAnalyticsService hematologyAnalyticsService) {
        super(hematologyAnalyticsService);
        this.hematologyAnalyticsService = hematologyAnalyticsService;
    }

    @Override
    @GetMapping("/results/search/name/level")
    public ResponseEntity<List<GenericValuesRecord>>
    getAnalyticsByLevel(Pageable pageable, String name, String level) {
        return ResponseEntity.ok(hematologyAnalyticsService
                .findAllAnalyticsByNameAndLevel(pageable, name, level));
    }

    @GetMapping("/results/names/date-range")
    public ResponseEntity<List<GenericValuesRecord>>
    getAllAnalyticsDateBetween(@RequestParam LocalDateTime startDate,
                               @RequestParam LocalDateTime endDate) {

        AvailableHematologyAnalytics names = new AvailableHematologyAnalytics();
       List<GenericValuesRecord> resultsList = hematologyAnalyticsService
                .getAllByNameInAndDateBetween(names.availableHematologyAnalytics(), startDate, endDate);
       return ResponseEntity.ok(resultsList);
    }

    @Override
    @GetMapping("/results/search/date-range")
    public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam LocalDateTime dateStart,
            @RequestParam LocalDateTime dateEnd) {
        return ResponseEntity.ok(hematologyAnalyticsService
                .findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd));
    }
    @Override
    @GetMapping("/results/mean-standard-deviation")
    public ResponseEntity<MeanAndStandardDeviationRecord> getMeanAndStandardDeviation(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam LocalDateTime dateStart,
            @RequestParam LocalDateTime dateEnd)
    {
        return ResponseEntity
                .ok(hematologyAnalyticsService.generateMeanAndStandardDeviation(name, level, dateStart, dateEnd));
    }
}
