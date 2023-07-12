package coursework.webdevelopment.auction_system_cw_3.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CsvLot {

    private Integer id;
    private Status status;
    private String title;
    private String lastBidder;
    private Integer currentPrice;

    public CsvLot(Integer id,
                  Status status,
                  String title,
                  String lastBidder,
                  Integer currentPrice) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.lastBidder = lastBidder;
        this.currentPrice = currentPrice;
    }
}
