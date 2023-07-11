package coursework.webdevelopment.auction_system_cw_3.service;
import coursework.webdevelopment.auction_system_cw_3.exceptions.LotStartsException;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import coursework.webdevelopment.auction_system_cw_3.dto.BidDTO;
import coursework.webdevelopment.auction_system_cw_3.dto.Status;
import coursework.webdevelopment.auction_system_cw_3.exceptions.LotNotFoundException;
import coursework.webdevelopment.auction_system_cw_3.model.Bid;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
import coursework.webdevelopment.auction_system_cw_3.repository.BidRepository;
import coursework.webdevelopment.auction_system_cw_3.repository.LotRepository;
import java.time.Instant;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Service

public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final LotRepository lotRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BidServiceImpl.class);

    @Override
    public BidDTO getInformationAboutTheFirstBidder(Integer id) {
        LOGGER.info("Вызван метод получения информации о первом ставившем на лот - {}", id);
        Tuple tuple = bidRepository.findsTheFirstBidderOnTheLot(id).orElseThrow(LotNotFoundException::new);
        return new BidDTO(
                tuple.get("bidderName", String.class),
                tuple.get("bidDate", Instant.class).atOffset(ZonedDateTime.now().getOffset())
        );
    }

    @Override
    public BidDTO returnsTheNameOfThePersonWhoBetOnThisLotTheMostNumberOfTimes(Integer id) {
        LOGGER.info("Вызван метод для получения имени ставившего на лот - {} наибольшее количество раз", id);
        Tuple tuple = bidRepository.findTheMostFrequentBidder(id).orElseThrow(LotNotFoundException::new);
        return new BidDTO(
                tuple.get("bidderName", String.class),
                tuple.get("bidDate", Instant.class).atOffset(ZonedDateTime.now().getOffset())
        );
    }

    @Override
    public void placeBet(Integer id, BidDTO bidDTO) {
        LOGGER.info("Вызван метод для ставки по лоту - {}", id);
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        if (lot.getStatus() == Status.CREATED || lot.getStatus() == Status.STOPPED) {
            throw new LotStartsException();
        }
        Bid bid = new Bid();
        bid.setBidderName(bidDTO.getBidderName());
        bid.setLotId(lot);
        bid.setBidDate(bidDTO.getBidDate());
        bidRepository.save(bid);
    }

    private static BidDTO toDto(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setBidDate(bid.getBidDate());
        return bidDTO;
    }

}
