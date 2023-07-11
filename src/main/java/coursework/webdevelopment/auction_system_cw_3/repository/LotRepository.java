package coursework.webdevelopment.auction_system_cw_3.repository;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import coursework.webdevelopment.auction_system_cw_3.dto.Status;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
import java.util.List;
import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot, Integer> {

    @Query(value = """
        SELECT l.id AS id,
               l.status AS status,
               l.title AS title,
               l.description AS description,
               l.start_price AS startPrice,
               l.bid_price AS bidPrice,
               l.start_price + l.bid_price *
               (SELECT count(b.id)
               FROM bids b
               WHERE b.lot_id = :lotId) AS currentPrice,
               e.bidder_name AS bidderName,
               e.bid_date AS bidDate
               FROM lots l
               LEFT JOIN (SELECT b.bidder_name, b.bid_date, b.lot_id
               FROM bids b
               ORDER BY b.bid_date DESC LIMIT 1) e ON e.lot_id = l.id
               WHERE l.id = :lotId
""", nativeQuery = true)
    Optional<Tuple> getFullLot(@Param("lotId") Integer lotId);

    Page<Lot> findAllByStatus (Status status, Pageable pageable);

    @Query(value = """
        SELECT l.id AS id,
               l.status AS status,
               l.title AS title,
               (SELECT b.bidder_name
               FROM bids b WHERE b.lot_id = l.id
               ORDER BY b.bid_date DESC LIMIT 1) AS lastBidder,
               (SELECT lc.start_price + lc.bid_price *
               (SELECT count(b.id)
               FROM bids b
               WHERE b.lot_id = lc.id)
               FROM lots lc
               WHERE lc.id = l.id) AS currentPrice
        FROM lots l
""", nativeQuery = true)
    List<Tuple> getInformationAboutTheLot();

}
