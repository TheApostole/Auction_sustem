package coursework.webdevelopment.auction_system_cw_3.service;
import coursework.webdevelopment.auction_system_cw_3.exceptions.LotNotFoundException;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import coursework.webdevelopment.auction_system_cw_3.dto.*;
import coursework.webdevelopment.auction_system_cw_3.repository.LotRepository;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service

public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(LotServiceImpl.class);

    @Override
    public FullLot getFullInformation(Integer id) {
        LOGGER.info("Вызван метод получения информации о первом ставившем на лот - {}", id);
        Tuple tuple = lotRepository.getFullLot(id).orElseThrow(LotNotFoundException::new);
        return new FullLot(
                tuple.get("id", Integer.class),
                Status.valueOf(tuple.get("status", String.class)),
                tuple.get("title", String.class),
                tuple.get("description", String.class),
                tuple.get("startPrice", Integer.class),
                tuple.get("bidPrice", Integer.class),
                tuple.get("currentPrice", Long.class).intValue(),
                new BidDTO(
                        tuple.get("bidderName", String.class),
                        tuple.get("bidDate", Instant.class).atOffset(ZonedDateTime.now().getOffset()))
        );
    }

    @Override
    public void startBidding(Integer id) {
        LOGGER.info("Вызван метод для начала торгов по лоту - {}", id);
        regulatesTheStatus(id, Status.STARTED);
    }

    @Override
    public void stopBidding(Integer id) {
        LOGGER.info("Вызван метод для остановки торгов по лоту - {}", id);
        regulatesTheStatus(id, Status.STOPPED);
    }

    @Override
    public LotDTO createNewLot(CreateLot createLot) {
        LOGGER.info("Вызван метод для создания лота");
        return toLotDTO(lotRepository.save(toModel(createLot)));
    }

    @Override
    public List<Lot> getAllLotsByStatusFilterAndPageNumber(Status status, Integer indexPage) {
        LOGGER.info("Вызван метод для получения всех лотов, основываясь на фильтре статуса и номере страницы");
        Pageable pageable = PageRequest.of(indexPage, 10);
        return Optional.ofNullable(status)
                .map(stat -> lotRepository.findAllByStatus(stat, pageable))
                .orElseGet(() -> lotRepository.findAll(pageable)).stream()
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public byte[] exportAllLotsToCSVFile() {
        LOGGER.info("Вызван метод для экспортирования всех лотов в файл CSV");
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("id", "status", "title", "lastBidder", "currentPrice")
                .build();
        try (CSVPrinter printer = new CSVPrinter(stringWriter, csvFormat)) {
            lotRepository.getInformationAboutTheLot().forEach(csvLot -> {
                try {
                    printer.printRecord(
                            csvLot.get("id", Integer.class),
                            Status.valueOf(csvLot.get("status", String.class)),
                            csvLot.get("title", String.class),
                            csvLot.get("lastBidder", String.class),
                            csvLot.get("currentPrice", Long.class).intValue());
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            });
        }
        return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
    }
    private void regulatesTheStatus (Integer id, Status status) {
        Lot lot = lotRepository.findById(id).orElseThrow(LotNotFoundException::new);
        lot.setStatus(status);
        lotRepository.save(lot);
    }

    private Lot toModel(CreateLot createLot) {
        Lot lot = new Lot();
        lot.setStatus(Status.CREATED);
        lot.setTitle(createLot.getTitle());
        lot.setDescription(createLot.getDescription());
        lot.setStartPrice(createLot.getStartPrice());
        lot.setBidPrice(createLot.getBidPrice());
        return lot;
    }

    private static LotDTO toLotDTO(Lot lot) {
        LotDTO lotDTO = new LotDTO();
        lotDTO.setStatus(lot.getStatus());
        lotDTO.setTitle(lot.getTitle());
        lotDTO.setDescription(lot.getDescription());
        lotDTO.setStartPrice(lot.getStartPrice());
        lotDTO.setBidPrice(lot.getBidPrice());
        return lotDTO;
    }

}
