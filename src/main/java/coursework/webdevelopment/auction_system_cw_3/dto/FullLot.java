package coursework.webdevelopment.auction_system_cw_3.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FullLot {

    private Integer id;
    private Status status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private Integer currentPrice;
    private BidDTO lastBid;

    public FullLot() {
    }

    public FullLot(Integer id, Status status, String title, String description, Integer startPrice, Integer bidPrice, Integer currentPrice, BidDTO lastBid) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
        this.startPrice = startPrice;
        this.bidPrice = bidPrice;
        this.currentPrice = currentPrice;
        this.lastBid = lastBid;
    }

}
