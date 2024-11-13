package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.service.AnalyticsHelperService;
import leonardo.labutilities.qualitylabpro.service.BiochemistryAnalyticsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("biochemistry-analytics")
@SecurityRequirement(name = "bearer-key")
@RequestMapping("biochemistry-analytics")
public class BiochemistryAnalyticsController extends GenericAnalyticsController {

    private final BiochemistryAnalyticsService biochemistryAnalyticsService;

    public BiochemistryAnalyticsController(BiochemistryAnalyticsService biochemistryAnalyticsService) {
        super(biochemistryAnalyticsService);
        this.biochemistryAnalyticsService = biochemistryAnalyticsService;
    }

    @Override
    @GetMapping("/results/search/level")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getAnalyticsByLevel(Pageable pageable, String name, String level) {
        return ResponseEntity.ok(biochemistryAnalyticsService
                .findAllAnalyticsByNameAndLevel(pageable, name, level));
    }

    @Override
    @GetMapping("/results/search/date-range")
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllAnalyticsByDateRange(
            @RequestParam String name,
            @RequestParam String level,
            @RequestParam String dateStart,
            @RequestParam String dateEnd) {
        return ResponseEntity.ok(biochemistryAnalyticsService
                .findAllAnalyticsByNameAndLevelAndDate(name, level, dateStart, dateEnd));
    }
}
