package tp.fstl.nfe101.pharmacies.db.consumer.exception;

/**
 * Exception levée lorsqu'une erreur survient lors de la consommation d'un message Kafka.
 */
public class KafkaConsumptionException extends RuntimeException {
    public KafkaConsumptionException(String message) {
        super(message);
    }

    public KafkaConsumptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
