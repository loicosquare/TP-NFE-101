package tp.fstl.nfe101.pharmacies.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tp.fstl.nfe101.pharmacies.producer.model.PharmacieCsv;

import java.util.List;

@Service
public class PharmacieManager {

    private static final Logger log = LoggerFactory.getLogger(PharmacieManager.class);

    private final CsvReader csvReader;
    private final KafkaSender kafkaSender;

    public PharmacieManager(CsvReader csvReader, KafkaSender kafkaSender) {
        this.csvReader = csvReader;
        this.kafkaSender = kafkaSender;
    }

    public void transfer(String csvFileName) {
        log.info("Transferring data from CSV file: {} to Kafka", csvFileName);

        List<PharmacieCsv> csvPharmacies = csvReader.readPharmaciesFromFile(csvFileName);
        log.info("Sending csv pharmacies to kafka");
        
        csvPharmacies
        .stream()
        .map(PharmacieMapper::toJson)
        .forEach(kafkaSender::sendPharmacie);
        log.info("Finished transferring file: {}", csvFileName);
    }
}
