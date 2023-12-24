package leonardo.labutilities.qualitylabpro.services;

import leonardo.labutilities.qualitylabpro.domain.entitys.Lot;
import leonardo.labutilities.qualitylabpro.record.lot.ValueOfLotRecord;
import leonardo.labutilities.qualitylabpro.repository.LotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LotService {
    private final LotRepository lotRepository;

    public Lot addLots(ValueOfLotRecord valueOfLotRecord) {
        return lotRepository.save(new Lot(valueOfLotRecord));
    }
}
