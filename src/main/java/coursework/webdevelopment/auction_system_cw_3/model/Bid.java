package coursework.webdevelopment.auction_system_cw_3.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table (name = "bids")

public class Bid {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private int id;

   @Column(name = "bidder_name")
   private String bidderName;

   @Column(name = "bid_date")
   private OffsetDateTime bidDate;

   @ManyToOne
   @JoinColumn(name = "lot_id")
   private Lot lotId;

   public Bid() {
   }

}
