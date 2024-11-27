package leonardo.labutilities.qualitylabpro.controller;


import leonardo.labutilities.qualitylabpro.model.HematologyAnalytics;
import leonardo.labutilities.qualitylabpro.dto.analytics.ValuesOfHematologyRecord;
import leonardo.labutilities.qualitylabpro.service.HematologyAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("hematology-analytics")
@RequestMapping("/hematology-analytics")
public class HematologyAnalyticsController {

    public HematologyAnalyticsController(HematologyAnalyticsService hematologyAnalyticsService) {
        this.hematologyAnalyticsService = hematologyAnalyticsService;
    }

    private final HematologyAnalyticsService hematologyAnalyticsService;

    @PostMapping
    @Transactional
    @RequestMapping(value = "/values", method = RequestMethod.POST)
    public ResponseEntity<List<HematologyAnalytics>> sendValues(@RequestBody List<ValuesOfHematologyRecord> values) {
        var response = hematologyAnalyticsService.saveHematology(values);
        return ResponseEntity.ok().body(response.toList());
    }
}
