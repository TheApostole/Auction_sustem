package coursework.webdevelopment.auction_system_cw_3.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import coursework.webdevelopment.auction_system_cw_3.dto.Status;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table (name = "lots")

public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_price")
    private int startPrice;

    @Column(name = "bid_price")
    private int bidPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public Lot() {
    }

}
