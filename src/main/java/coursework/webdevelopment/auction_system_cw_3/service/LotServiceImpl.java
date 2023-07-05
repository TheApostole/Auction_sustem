package coursework.webdevelopment.auction_system_cw_3.service;
import coursework.webdevelopment.auction_system_cw_3.exceptions.LotNotFoundException;
import coursework.webdevelopment.auction_system_cw_3.model.Lot;
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

    @Override
    public FullLot getFullInformation(Integer id) {
        LOGGER.info("Вызван метод получения информации о первом ставившем на лот - {}", id);
        return lotRepository.getFullInformationOnLot(id);

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
    public List<LotDTO> getAllLotsByStatusFilterAndPageNumber(Status status, Integer indexPage) {
        LOGGER.info("Вызван метод для получения всех лотов, основываясь на фильтре статуса и номере страницы");
        Pageable pageable = PageRequest.of(indexPage, 10);
        return Optional.ofNullable(status)
                .map(stat -> lotRepository.findAllByStatus(stat, pageable))
                .orElseGet(() -> lotRepository.findAll(pageable)).stream()
                .map(LotServiceImpl::toLotDTO)
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
        lotDTO.setBidPrice(lotDTO.getBidPrice());
        return lotDTO;
    }


}
