package leonardo.labutilities.qualitylabpro.controller;


import leonardo.labutilities.qualitylabpro.domain.entities.HematologyAnalytics;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfHematologyRecord;
import leonardo.labutilities.qualitylabpro.services.HematologyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/hematology")
public class HematologyController {
    private final HematologyService hematologyService;

    @PostMapping
    @Transactional
    @RequestMapping(value = "/values", method = RequestMethod.POST)
    public ResponseEntity<List<HematologyAnalytics>> sendValues(@RequestBody List<ValuesOfHematologyRecord> values) {
        var response = hematologyService.saveHematology(values);
        return ResponseEntity.ok().body(response.toList());
    }
}
