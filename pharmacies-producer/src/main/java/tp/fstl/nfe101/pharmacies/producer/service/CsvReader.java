package tp.fstl.nfe101.pharmacies.producer.service;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;

import tp.fstl.nfe101.pharmacies.producer.exception.CsvParsingException;
import tp.fstl.nfe101.pharmacies.producer.model.PharmacieCsv;

@Service
public class CsvReader {

    private static final Logger log = LoggerFactory.getLogger(CsvReader.class);
    private final String dataFolder;

    public CsvReader(@Value("${pharmacies.data-folder}")String dataFolder) {
        this.dataFolder = dataFolder;
    }

    public List<PharmacieCsv> readPharmaciesFromFile(String fileName) {
        String filePath = dataFolder + File.separator + fileName;
        log.debug("Parsing file: {}", filePath);
        try {
            return new CsvToBeanBuilder<PharmacieCsv>(new FileReader(filePath))
                .withType(PharmacieCsv.class)
                .withSeparator(';')
                .build()
                .parse();
        } catch (Exception e) {
            log.error("Could not parse file: {}", filePath, e);
            throw new CsvParsingException("Could not parse Csv file: " + fileName + ". Cause: " + e.getMessage(), e);
        }
    }
}
