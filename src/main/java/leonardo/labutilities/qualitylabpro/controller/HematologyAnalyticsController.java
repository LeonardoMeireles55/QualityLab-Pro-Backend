package leonardo.labutilities.qualitylabpro.controller;


import leonardo.labutilities.qualitylabpro.dto.analytics.GenericValuesRecord;
import leonardo.labutilities.qualitylabpro.dto.analytics.MeanAndStandardDeviationRecord;
import leonardo.labutilities.qualitylabpro.service.HematologyAnalyticsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("hematology-analytics")
@RequestMapping("/hematology-analytics")
public class HematologyAnalyticsController extends GenericAnalyticsController {

    private final HematologyAnalyticsService hematologyAnalyticsService;

    public HematologyAnalyticsController(HematologyAnalyticsService hematologyAnalyticsService) {
        super(hematologyAnalyticsService);
        this.hematologyAnalyticsService = hematologyAnalyticsService;
    }

    @Override
    @GetMapping("/results/search/level")
    public ResponseEntity<List<GenericValuesRecord>>
    getAnalyticsByLevel(Pageable pageable, String name, String level) {
        return ResponseEntity.ok(hematologyAnalyticsService
                .findAllAnalyticsByNameAndLevel(pageable, name, level));
    }

    @Override
    @GetMapping("/results/search/date-range")
    public ResponseEntity<List<GenericValuesRecord>> getAllAnalyticsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(hematologyAnalyticsService
                .findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd));
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
                .ok(hematologyAnalyticsService.generateMeanAndStandardDeviation(name, level, dateStart, dateEnd));
    }
}
