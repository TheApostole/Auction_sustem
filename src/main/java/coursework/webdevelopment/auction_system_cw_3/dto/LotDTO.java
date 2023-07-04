package coursework.webdevelopment.auction_system_cw_3.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class LotDTO {

    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    private Status status;

    public LotDTO() {
    }

}
