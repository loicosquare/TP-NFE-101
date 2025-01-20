package tp.fstl.nfe101.pharmacies.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tp.fstl.nfe101.pharmacies.producer.exception.JsonSerializationException;
import tp.fstl.nfe101.pharmacies.producer.exception.KafkaSendException;
import tp.fstl.nfe101.pharmacies.producer.model.PharmacieJson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class KafkaSender {

    private static final Logger log = LoggerFactory.getLogger(KafkaSender.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String pharmaciesTopicName;
    private final boolean syncSending;
    private final KafkaProducer<String, String> kafkaProducer;

    public KafkaSender(@Value("${pharmacies.kafka-server}") String kafkaServers,
                       @Value("${pharmacies.raw-kafka-topic}") String pharmaciesTopicName,
                       @Value("${pharmacies.sync-sending:false}") boolean syncSending) {
        this.pharmaciesTopicName = pharmaciesTopicName;
        this.syncSending = syncSending;

        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        this.kafkaProducer = new KafkaProducer<>(properties);
    }

    public void sendPharmacie(PharmacieJson pharmacie) {
        String json;
        try {
            log.debug("Serializing java object to json");
            json = objectMapper.writeValueAsString(pharmacie);
            log.trace("Resultant Json: {}", json);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize pharmacie object {} to json", pharmacie, e);
            throw new JsonSerializationException("Error serializing json from object: " + pharmacie + ". Cause: " + e.getMessage(), e);
        }

        String key = "raw-pharmacie-" + pharmacie.getFinessET(); // Nous considérons que ceci représente l'id de la pharmacie.
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(pharmaciesTopicName, key, json);

        try {
            if (syncSending) {
                Future<RecordMetadata> sendingRecord = kafkaProducer.send(producerRecord);
                RecordMetadata sentRecord = sendingRecord.get();
                log.debug("Json was sent successfully to kafka topic: {}", sentRecord);
            } else {
                kafkaProducer.send(producerRecord, (sentRecord, error) -> {
                    if (error != null) {
                        log.error("Could not send json to kafka topic: {}", pharmaciesTopicName, error);
                    } else {
                        log.debug("Json was sent successfully to kafka topic: {}", sentRecord);
                    }
                });
            }
        } catch (Exception e) {
            log.error("Could not send json to kafka topic: {}", pharmaciesTopicName, e);
            throw new KafkaSendException("Could not send json to kafka topic: " + pharmaciesTopicName + ". Cause: " + e.getMessage(), e
            );
        }
    }

}
