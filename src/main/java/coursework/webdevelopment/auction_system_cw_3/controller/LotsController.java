package coursework.webdevelopment.auction_system_cw_3.controller;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import coursework.webdevelopment.auction_system_cw_3.dto.*;
import coursework.webdevelopment.auction_system_cw_3.service.BidService;
import coursework.webdevelopment.auction_system_cw_3.service.LotService;
import java.util.List;

@RestController
@RequestMapping("/lot")
@Getter
@AllArgsConstructor

public class LotsController {

    private final LotService lotService;
    private final BidService bidService;

    @GetMapping("/{id}/first")
    public BidDTO getInformationAboutTheFirstBidder(@PathVariable Integer id) {
        return bidService.getInformationAboutTheFirstBidder(id);
    }

    @GetMapping("/{id}/frequent")
    public BidDTO returnsTheNameOfThePersonWhoBetOnThisLotTheMostNumberOfTimes(@PathVariable Integer id) {
        return bidService.returnsTheNameOfThePersonWhoBetOnThisLotTheMostNumberOfTimes(id);
    }

    @GetMapping("/{id}")
    public FullLot getFullInformation(@PathVariable Integer id) {
        return lotService.getFullInformation(id);
    }

    @PostMapping("/{id}/start")
    public void startBidding(@PathVariable Integer id) {
        lotService.startBidding(id);
    }

    @PostMapping("/{id}/bid")
    public void placeBet(@PathVariable Integer id, @RequestBody @Valid BidDTO bidDTO) {
        bidService.placeBet(id, bidDTO);
    }

    @PostMapping("/{id}/stop")
    public void stopBidding(@PathVariable Integer id) {
        lotService.stopBidding(id);
    }

    @PostMapping
    public LotDTO createNewLot(@RequestBody @Valid CreateLot createLot) {
        return lotService.createNewLot(createLot);
    }

    @GetMapping
    public List<Lot> getAllLotsByStatusFilterAndPageNumber(@RequestParam(value = "status", required = false)Status status,
                                                           @RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        return lotService.getAllLotsByStatusFilterAndPageNumber(status, page);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAllLotsToCSVFile() {
        byte [] result = lotService.exportAllLotsToCSVFile();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "text/cvs")
                .body(result);
    }

}
