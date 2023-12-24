package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import leonardo.labutilities.qualitylabpro.domain.entitys.Lot;
import leonardo.labutilities.qualitylabpro.record.lot.ValueOfLotRecord;
import leonardo.labutilities.qualitylabpro.services.LotService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/lotValues")
public class LotController {
    private final LotService lotService;

    @PostMapping
    @Transactional
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Lot addLot(@RequestBody ValueOfLotRecord valueOfLotRecord) {

        return lotService.addLots(valueOfLotRecord);
    }

}
