package coursework.webdevelopment.auction_system_cw_3.util;
import lombok.AllArgsConstructor;
import coursework.webdevelopment.auction_system_cw_3.dto.CreateLot;
import coursework.webdevelopment.auction_system_cw_3.dto.LotDTO;
import coursework.webdevelopment.auction_system_cw_3.dto.Status;
import coursework.webdevelopment.auction_system_cw_3.exceptions.LotNotFoundException;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
import coursework.webdevelopment.auction_system_cw_3.repository.LotRepository;

@AllArgsConstructor

public class UtilitiesMethods {

    private final LotRepository lotRepository;

    public void regulatesTheStatus (Integer id, Status status) {
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        lot.setStatus(status);
        lotRepository.save(lot);
    }

    public Lot toModel(CreateLot createLot) {
        Lot lot = new Lot();
        lot.setStatus(Status.CREATED);
        lot.setTitle(createLot.getTitle());
        lot.setDescription(createLot.getDescription());
        lot.setStartPrice(createLot.getStartPrice());
        lot.setBindPrice(createLot.getBidPrice());
        return lot;
    }

    public LotDTO toLotDTO (Lot lot) {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setStatus(lot.getStatus());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setStartPrice(lot.getStartPrice());
        lotDTO.setBidPrice(lotDTO.getBidPrice());
        return lotDTO;
    }

}
