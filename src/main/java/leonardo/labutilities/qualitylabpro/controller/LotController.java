package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import leonardo.labutilities.qualitylabpro.main.Lot;
import leonardo.labutilities.qualitylabpro.records.lot.ValueOfLotDTO;
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
    public Lot addLot(@RequestBody ValueOfLotDTO valueOfLotDTO) {

        return lotService.addLots(valueOfLotDTO);
    }

}
