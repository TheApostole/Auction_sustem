package coursework.webdevelopment.auction_system_cw_3.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import coursework.webdevelopment.auction_system_cw_3.model.Bid;

@Getter
@Setter
@AllArgsConstructor

public class FullLot {

    private Integer id;
    private Status status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private Integer currentPrice;
    private Bid lastBid;

    public FullLot() {
    }

}
