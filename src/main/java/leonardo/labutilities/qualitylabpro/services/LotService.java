package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.main.Lot;
import leonardo.labutilities.qualitylabpro.records.lot.ValueOfLotDTO;
import leonardo.labutilities.qualitylabpro.repositories.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LotService {
    private final LotRepository lotRepository;

    public Lot addLots(ValueOfLotDTO valueOfLotDTO) {
        return lotRepository.save(new Lot(valueOfLotDTO));
    }
}
