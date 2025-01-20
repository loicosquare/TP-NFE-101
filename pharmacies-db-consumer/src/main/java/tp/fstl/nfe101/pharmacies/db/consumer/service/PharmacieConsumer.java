package tp.fstl.nfe101.pharmacies.db.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tp.fstl.nfe101.pharmacies.db.consumer.dto.PharmacieJson;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.KafkaConsumptionException;
import tp.fstl.nfe101.pharmacies.db.consumer.exception.PharmacieSavingException;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PharmacieConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(PharmacieConsumer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String pharmaciesTopicName;
    private final KafkaConsumer<String, String> consumer;
    private final PharmacieService pharmacieService;


     public PharmacieConsumer(@Value("${pharmacies.kafka-server}") String kafkaServers,
                              @Value("${pharmacies.raw-kafka-topic}") String pharmaciesTopicName,
                              PharmacieService pharmacieService) {
        this.pharmaciesTopicName = pharmaciesTopicName;
        this.pharmacieService = pharmacieService;

        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "pharmacies-db-consumer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.toString());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        
        this.consumer = new KafkaConsumer<>(properties);
     }

    public void consume() {
        consumer.subscribe(List.of(pharmaciesTopicName));

        while (true) {
            try {
                // Récupération des enregistrements.
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                // Traitement des enregistrements reçus.
                for (ConsumerRecord<String, String> record : records) {
                    log.debug("Received record with key: {} from partition: {} at offset: {}", record.key(), record.partition(), record.offset());
                    try {
                        // Désérialisation du JSON en objet Java et sauvegarde en base de données.
                        PharmacieJson jsonPharmacie = objectMapper.readValue(record.value(), PharmacieJson.class);
                        pharmacieService.save(jsonPharmacie);
                    } catch (Exception e) {
                        // En cas d'erreur, on log l'erreur et on arrête la consommation.
                        log.error("Error while processing record with key: {} at partition/offset: {}/{}. Stopping consumption",
                                record.key(), record.partition(), record.offset(), e);
                        throw new PharmacieSavingException("Error while processing record", e);
                    }
                }
                log.info("Processed {} records", records.count());
            } catch (Exception e) {
                log.error("Error while consuming records", e);
                throw new KafkaConsumptionException("Error during Kafka consumption", e);
            }
        }
    }
}
