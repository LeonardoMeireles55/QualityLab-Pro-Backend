package leonardo.labutilities.qualitylabpro.controllers;


import leonardo.labutilities.qualitylabpro.domain.entities.HematologyAnalytics;
import leonardo.labutilities.qualitylabpro.records.genericAnalytics.ValuesOfHematologyRecord;
import leonardo.labutilities.qualitylabpro.services.HematologyService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/hematology")
public class HematologyController {

    public HematologyController(HematologyService hematologyService) {
        this.hematologyService = hematologyService;
    }

    private final HematologyService hematologyService;

    @PostMapping
    @Transactional
    @RequestMapping(value = "/values", method = RequestMethod.POST)
    public ResponseEntity<List<HematologyAnalytics>> sendValues(@RequestBody List<ValuesOfHematologyRecord> values) {
        var response = hematologyService.saveHematology(values);
        return ResponseEntity.ok().body(response.toList());
    }
}
