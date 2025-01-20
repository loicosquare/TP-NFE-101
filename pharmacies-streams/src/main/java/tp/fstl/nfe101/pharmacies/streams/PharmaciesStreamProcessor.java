package tp.fstl.nfe101.pharmacies.streams;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.Properties;

public class PharmaciesStreamProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PharmaciesStreamProcessor.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "pharmacies-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> sourceStream = builder.stream("pharmacies.raw");

        ObjectMapper objectMapper = new ObjectMapper();

        // Grouper par ville
        KTable<String, Long> groupedByCity = sourceStream
                .peek((key, value) -> logger.info("Received value for city grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String city = jsonNode.get("city").asText();
                        logger.info("Parsed city: {}", city);
                        return city;
                    } catch (Exception e) {
                        logger.error("Error parsing city name from value {}", value, e);
                        return "ERROR";
                    }
                })
                .mapValues(city -> {
                    String result = Optional.ofNullable(city).orElse("Unknown");
                    logger.info("City after optional handling: {}", result);
                    return result;
                })
                .groupBy((key, city) -> {
                    logger.info("Grouping by city: {}", city);
                    return city;
                })
                .count(Materialized.as("city-counts"));

        groupedByCity.toStream()
                .peek((key, value) -> logger.info("City: {} Count: {}", key, value))
                .map((city, count) -> {
                    String result = city + ":" + count;
                    logger.info("Mapped city and count: {}", result);
                    return new KeyValue<>(city, result);
                })
                .to("pharmacies.grouped.by.city", Produced.with(Serdes.String(), Serdes.String()));

        // Grouper par année d'autorisation
        KTable<String, Long> groupedByAuthorizationYear = sourceStream
                .peek((key, value) -> logger.info("Received value for authorization year grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String authorizationDate = jsonNode.get("authorizationDate").asText();
                        String year = authorizationDate != null && authorizationDate.length() >= 4 ? authorizationDate.substring(0, 4) : "Unknown";
                        logger.info("Parsed authorization year: {}", year);
                        return year;
                    } catch (Exception e) {
                        logger.error("Error parsing authorizationDate from value {}", value, e);
                        return "Unknown";
                    }
                })
                .groupBy((key, year) -> {
                    logger.info("Grouping by authorization year: {}", year);
                    return year;
                })
                .count(Materialized.as("pharmacies-grouped-by-authorization-year"));

        groupedByAuthorizationYear.toStream()
                .peek((key, value) -> logger.info("Authorization Year: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.authorization.year", Produced.with(Serdes.String(), Serdes.Long()));

        // Grouper par année d'ouverture
        KTable<String, Long> groupedByOpeningYear = sourceStream
                .peek((key, value) -> logger.info("Received value for opening year grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String openingDate = jsonNode.get("openingDate").asText();
                        String year = openingDate != null && openingDate.length() >= 4 ? openingDate.substring(0, 4) : "Unknown";
                        logger.info("Parsed opening year: {}", year);
                        return year;
                    } catch (Exception e) {
                        logger.error("Error parsing openingDate from value {}", value, e);
                        return "Unknown";
                    }
                })
                .groupBy((key, year) -> {
                    logger.info("Grouping by opening year: {}", year);
                    return year;
                })
                .count(Materialized.as("pharmacies-grouped-by-opening-year"));

        groupedByOpeningYear.toStream()
                .peek((key, value) -> logger.info("Opening Year: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.opening.year", Produced.with(Serdes.String(), Serdes.Long()));

        // Grouper par type de rue
        KTable<String, Long> groupedByStreetType = sourceStream
                .peek((key, value) -> logger.info("Received value for street type grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String streetType = jsonNode.get("streetType").asText();
                        String result = streetType != null ? streetType : "Unknown";
                        logger.info("Parsed street type: {}", result);
                        return result;
                    } catch (Exception e) {
                        logger.error("Error parsing streetType from value {}", value, e);
                        return "Unknown";
                    }
                })
                .groupBy((key, streetType) -> {
                    logger.info("Grouping by street type: {}", streetType);
                    return streetType;
                })
                .count(Materialized.as("pharmacies-grouped-by-street-type"));

        groupedByStreetType.toStream()
                .peek((key, value) -> logger.info("Street Type: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.street.type", Produced.with(Serdes.String(), Serdes.Long()));

        // Grouper par ville, code postal et type de rue
        KTable<String, Long> groupedByCityPostalCodeStreetType = sourceStream
                .peek((key, value) -> logger.info("Received value for city, postal code, and street type grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String city = jsonNode.get("city").asText();
                        String postalCode = jsonNode.get("postalCode").asText();
                        String streetType = jsonNode.get("streetType").asText();
                        String compositeKey = city + "|" + postalCode + "|" + streetType;
                        logger.info("Parsed composite key: {}", compositeKey);
                        return compositeKey;
                    } catch (Exception e) {
                        logger.error("Error parsing city, postalCode, and streetType from value {}", value, e);
                        return "Unknown|Unknown|Unknown";
                    }
                })
                .groupBy((key, compositeKey) -> {
                    logger.info("Grouping by composite key: {}", compositeKey);
                    return compositeKey;
                })
                .count(Materialized.as("pharmacies-grouped-by-city-postalcode-streettype"));

        groupedByCityPostalCodeStreetType.toStream()
                .peek((key, value) -> logger.info("City|PostalCode|StreetType: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.city.postalcode.streettype", Produced.with(Serdes.String(), Serdes.Long()));

        // Grouper par ville, code postal et date d'ouverture
        KTable<String, Long> groupedByCityPostalCodeOpeningDate = sourceStream
                .peek((key, value) -> logger.info("Received value for city, postal code, and opening date grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String city = jsonNode.get("city").asText();
                        String postalCode = jsonNode.get("postalCode").asText();
                        String openingDate = jsonNode.get("openingDate").asText();
                        String compositeKey = city + "|" + postalCode + "|" + openingDate;
                        logger.info("Parsed composite key: {}", compositeKey);
                        return compositeKey;
                    } catch (Exception e) {
                        logger.error("Error parsing city, postalCode, and openingDate from value {}", value, e);
                        return "Unknown|Unknown|Unknown";
                    }
                })
                .groupBy((key, compositeKey) -> {
                    logger.info("Grouping by composite key: {}", compositeKey);
                    return compositeKey;
                })
                .count(Materialized.as("pharmacies-grouped-by-city-postalcode-openingdate"));

        groupedByCityPostalCodeOpeningDate.toStream()
                .peek((key, value) -> logger.info("City|PostalCode|OpeningDate: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.city.postalcode.openingdate", Produced.with(Serdes.String(), Serdes.Long()));

        // Grouper par ville, code postal et année d'ouverture
        KTable<String, Long> groupedByCityPostalCodeOpeningYear = sourceStream
                .peek((key, value) -> logger.info("Received value for city, postal code, and opening year grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String city = jsonNode.get("city").asText();
                        String postalCode = jsonNode.get("postalCode").asText();
                        String openingYear = jsonNode.get("openingDate").asText().substring(0, 4);
                        String compositeKey = city + "|" + postalCode + "|" + openingYear;
                        logger.info("Parsed composite key: {}", compositeKey);
                        return compositeKey;
                    } catch (Exception e) {
                        logger.error("Error parsing city, postalCode, and openingYear from value {}", value, e);
                        return "Unknown|Unknown|Unknown";
                    }
                })
                .groupBy((key, compositeKey) -> {
                    logger.info("Grouping by composite key: {}", compositeKey);
                    return compositeKey;
                })
                .count(Materialized.as("pharmacies-grouped-by-city-postalcode-openingyear"));

        groupedByCityPostalCodeOpeningYear.toStream()
                .peek((key, value) -> logger.info("City|PostalCode|OpeningYear: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.city.postalcode.openingyear", Produced.with(Serdes.String(), Serdes.Long()));

        // Grouper par ville, code postal, type de rue et année d'autorisation
        KTable<String, Long> groupedByCityPostalCodeStreetTypeAuthorizationYear = sourceStream
                .peek((key, value) -> logger.info("Received value for city, postal code, street type, and authorization year grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String city = jsonNode.get("city").asText();
                        String postalCode = jsonNode.get("postalCode").asText();
                        String streetType = jsonNode.get("streetType").asText();
                        String authorizationYear = jsonNode.get("authorizationDate").asText().substring(0, 4);
                        String compositeKey = city + "|" + postalCode + "|" + streetType + "|" + authorizationYear;
                        logger.info("Parsed composite key: {}", compositeKey);
                        return compositeKey;
                    } catch (Exception e) {
                        logger.error("Error parsing city, postalCode, streetType, and authorizationYear from value {}", value, e);
                        return "Unknown|Unknown|Unknown|Unknown";
                    }
                })
                .groupBy((key, compositeKey) -> {
                    logger.info("Grouping by composite key: {}", compositeKey);
                    return compositeKey;
                })
                .count(Materialized.as("pharmacies-grouped-by-city-postalcode-streettype-authorizationyear"));

        groupedByCityPostalCodeStreetTypeAuthorizationYear.toStream()
                .peek((key, value) -> logger.info("City|PostalCode|StreetType|AuthorizationYear: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.city.postalcode.streettype.authorizationyear", Produced.with(Serdes.String(), Serdes.Long()));

        // Grouper par ville, type de rue, date d'ouverture et année d'autorisation
        KTable<String, Long> groupedByCityStreetTypeOpeningDateAuthorizationYear = sourceStream
                .peek((key, value) -> logger.info("Received value for city, street type, opening date, and authorization year grouping: {}", value))
                .mapValues(value -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value);
                        String city = jsonNode.get("city").asText();
                        String streetType = jsonNode.get("streetType").asText();
                        String openingDate = jsonNode.get("openingDate").asText().substring(0, 4);
                        String authorizationYear = jsonNode.get("authorizationDate").asText().substring(0, 4);
                        String compositeKey = city + "|" + streetType + "|" + openingDate + "|" + authorizationYear;
                        logger.info("Parsed composite key: {}", compositeKey);
                        return compositeKey;
                    } catch (Exception e) {
                        logger.error("Error parsing city, streetType, openingDate, and authorizationYear from value {}", value, e);
                        return "Unknown|Unknown|Unknown|Unknown";
                    }
                })
                .groupBy((key, compositeKey) -> {
                    logger.info("Grouping by composite key: {}", compositeKey);
                    return compositeKey;
                })
                .count(Materialized.as("pharmacies-grouped-by-city-streettype-openingdate-authorizationyear"));

        groupedByCityStreetTypeOpeningDateAuthorizationYear.toStream()
                .peek((key, value) -> logger.info("City|StreetType|OpeningDate|AuthorizationYear: {} Count: {}", key, value))
                .to("pharmacies.grouped.by.city.streettype.openingdate.authorizationyear", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
