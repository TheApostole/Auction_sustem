package coursework.webdevelopment.auction_system_cw_3.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import coursework.webdevelopment.auction_system_cw_3.dto.BidDTO;
import coursework.webdevelopment.auction_system_cw_3.model.Bid;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Integer> {

    Optional<BidDTO> findFirstByLot_IdOrderByBidDateAsc(Integer id);

    @Query(value = """
            SELECT new coursework.webdevelopment.auction_system_cw_3.dto.BidDTO(b.bidderName, b.bidDate)
            FROM Bid b WHERE b.bidderName = (SELECT b.bidderName FROM Bid b GROUP BY b.bidderName ORDER BY count(b.bidderName))
            ORDER BY b.bidDate DESC LIMIT 1
            """, nativeQuery = true)
    Optional<BidDTO> findTheMostFrequentBidder (@Param("lot_id") int id);

}