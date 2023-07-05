package coursework.webdevelopment.auction_system_cw_3.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import coursework.webdevelopment.auction_system_cw_3.dto.FullLot;
import coursework.webdevelopment.auction_system_cw_3.dto.Status;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot, Integer> {

    @Query(value = """
        SELECT l.id as id,
               l.status as status,
               l.title as title,
               l.description as description,
               l.start_price as startPrice,
               l.bid_price as bidPrice,
               l.start_price + l.bid_price * (SELECT count(b.id) FROM bids b
                                                WHERE b.lot_id = :lotId) as currentPrice,
               e.bidder_name as bidderName,
               e.bid_date as bidDate
        FROM lots l,(SELECT b.bidderName, b.bidDate FROM bids b
                                                    WHERE b.lot_id = :lot_id
                                                    ORDER BY b.bidDate DESC LIMIT 1) e
        WHERE l.id = :b.lotId
""", nativeQuery = true)

    FullLot getFullInformationOnLot(@Param("lot_id") Integer lotId);

    Page<Lot> findAllByStatus (Status status, Pageable pageable);

    @Query(value = """
        SELECT l.id as id,
               l.status as status,
               l.title as title,
               l.description as description,
               l.start_price as startPrice,
               l.bid_price as bidPrice,
               l.start_price + l.bid_price * (SELECT count(b.id) FROM bids b as currentPrice,
               e.bidder_name as bidderName,
               e.bid_date as bidDate
        FROM lots l,(SELECT b.bidderName, b.bidDate FROM bids b) e
""", nativeQuery = true)

   Optional<FullLot> getFullInformation();

}
