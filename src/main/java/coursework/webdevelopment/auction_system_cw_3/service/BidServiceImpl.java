package coursework.webdevelopment.auction_system_cw_3.service;
import coursework.webdevelopment.auction_system_cw_3.exceptions.LotStartsException;
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

@AllArgsConstructor
@Service

public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final LotRepository lotRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BidServiceImpl.class);

    @Override
    public BidDTO getInformationAboutTheFirstBidder(Integer id) {
        LOGGER.info("Вызван метод получения информации о первом ставившем на лот - {}", id);
        return bidRepository.findFirstByLotIdOrderByBidDateAsc(id).orElseThrow(LotNotFoundException::new);
    }

    @Override
    public BidDTO returnsTheNameOfThePersonWhoBetOnThisLotTheMostNumberOfTimes(Integer id) {
        LOGGER.info("Вызван метод для получения имени ставившего на лот - {} наибольшее количество раз", id);
        return bidRepository.findTheMostFrequentBidder(id).orElseThrow(LotNotFoundException::new);
    }

    @Override
    public void placeBet(Integer id, BidDTO bidDTO) {
        LOGGER.info("Вызван метод для ставки по лоту - {}", id);
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        if (lot.getStatus() == Status.CREATED || lot.getStatus() == Status.STOPPED) {
            throw new LotStartsException();
        }
        bidRepository.save(new Bid(bidDTO.getBidderName()));
    }

}
