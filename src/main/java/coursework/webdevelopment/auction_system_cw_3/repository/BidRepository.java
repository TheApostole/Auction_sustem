package coursework.webdevelopment.auction_system_cw_3.repository;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import coursework.webdevelopment.auction_system_cw_3.model.Bid;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Integer> {

    @Query(value = """
            SELECT b.bidder_name AS bidderName,
                   b.bid_date AS bidDate
            FROM bids b WHERE b.lot_id = :lotId ORDER BY b.bid_date ASC LIMIT 1
            """,nativeQuery = true)
    Optional<Tuple> findsTheFirstBidderOnTheLot(@Param(value = "lotId") Integer lotId);

    @Query(value = """
            SELECT b.bidder_name AS bidderName,
                   b.bid_date AS bidDate
            FROM bids b WHERE b.bidder_name = (SELECT b.bidder_name FROM  bids b WHERE b.lot_id = :lotId GROUP BY  b.bidder_name ORDER BY count(b.bidder_name) DESC  LIMIT 1
            ) ORDER BY b.bid_date DESC LIMIT 1
            """,nativeQuery = true)
    Optional <Tuple> findTheMostFrequentBidder (@Param(value = "lotId") Integer lotId);

}