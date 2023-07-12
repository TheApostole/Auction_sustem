package coursework.webdevelopment.auction_system_cw_3.service;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
import org.springframework.stereotype.Service;
import coursework.webdevelopment.auction_system_cw_3.dto.CreateLot;
import coursework.webdevelopment.auction_system_cw_3.dto.FullLot;
import coursework.webdevelopment.auction_system_cw_3.dto.LotDTO;
import coursework.webdevelopment.auction_system_cw_3.dto.Status;
import java.util.List;

@Service
public interface LotService {

    FullLot getFullInformation(Integer id);

    void startBidding(Integer id);

    void stopBidding(Integer id);

    LotDTO createNewLot(CreateLot createLot);

    List<Lot> getAllLotsByStatusFilterAndPageNumber(Status status, Integer page);

    byte[] exportAllLotsToCSVFile();

}
