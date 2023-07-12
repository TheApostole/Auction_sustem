package coursework.webdevelopment.auction_system_cw_3.service;
import coursework.webdevelopment.auction_system_cw_3.dto.BidDTO;
import coursework.webdevelopment.auction_system_cw_3.dto.CreateBid;

public interface BidService {

    BidDTO getInformationAboutTheFirstBidder(Integer id);

    BidDTO returnsTheNameOfThePersonWhoBetOnThisLotTheMostNumberOfTimes(Integer id);

    void placeBet(Integer id, CreateBid createBid);

}
