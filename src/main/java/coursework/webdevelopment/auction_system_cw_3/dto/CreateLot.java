package coursework.webdevelopment.auction_system_cw_3.dto;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateLot {

    @NotBlank
    @Size(min = 3, max = 64)
    private String title;

    @NotBlank
    @Size(min = 1, max = 4096)
    private String description;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer startPrice;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer bidPrice;

}
