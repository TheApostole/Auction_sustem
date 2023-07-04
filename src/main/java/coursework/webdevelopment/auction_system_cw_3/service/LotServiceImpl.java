package coursework.webdevelopment.auction_system_cw_3.service;
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
import coursework.webdevelopment.auction_system_cw_3.util.UtilitiesMethods;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service

public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(LotServiceImpl.class);
    private final UtilitiesMethods utilitiesMethods;

    @Override
    public FullLot getFullInformation(Integer id) {
        LOGGER.info("Вызван метод получения информации о первом ставившем на лот - {}", id);
        return lotRepository.getFullInformationOnLot(id);
    }

    @Override
    public void startBidding(Integer id) {
        LOGGER.info("Вызван метод для начала торгов по лоту - {}", id);
        utilitiesMethods.regulatesTheStatus(id, Status.STARTED);
    }

    @Override
    public void stopBidding(Integer id) {
        LOGGER.info("Вызван метод для остановки торгов по лоту - {}", id);
        utilitiesMethods.regulatesTheStatus(id, Status.STOPPED);
    }

    @Override
    public LotDTO createNewLot(CreateLot createLot) {
        LOGGER.info("Вызван метод для создания лота");
        return utilitiesMethods.toLotDTO(lotRepository.save(utilitiesMethods.toModel(createLot)));
    }

    @Override
    public List<LotDTO> getAllLotsByStatusFilterAndPageNumber(Status status, Integer indexPage) {
        LOGGER.info("Вызван метод для получения всех лотов, основываясь на фильтре статуса и номере страницы");
        Pageable pageable = PageRequest.of(indexPage, 10);
        return Optional.ofNullable(status)
                .map(stat -> lotRepository.findAllByStatus(stat, pageable))
                .orElseGet(() -> lotRepository.findAll(pageable)).stream()
                .map(utilitiesMethods::toLotDTO)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public byte[] exportAllLotsToCSVFile() {
        LOGGER.info("Вызван метод для экспортирования всех лотов в файл CSV");
        List<FullLot> lotList = lotRepository.getFullInformation().stream().toList();
        StringWriter stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("id", "title", "status", "lastBidder", "currentPrice")
                .build();
        try (CSVPrinter printer = new CSVPrinter(stringWriter, csvFormat)) {
            lotList.forEach(lot -> {
                try {
                    printer.printRecord(lot.getId(),lot.getStatus(),
                            lot.getTitle(),lot.getLastBid(),lot.getCurrentPrice());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return Files.readAllBytes(Paths.get(String.valueOf(stringWriter)));
    }

}
