package coursework.webdevelopment.auction_system_cw_3.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor

public class BidDTO {

    private String bidderName;
    private OffsetDateTime bidDate;

    public BidDTO() {
    }
}
