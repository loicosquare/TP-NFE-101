package tp.fstl.nfe101.pharmacies.producer.exception;

/**
 * Exception générée lorsqu'une erreur se produit lors de l'envoi d'un message à Kafka.
 */
public class KafkaSendException extends RuntimeException {
    public KafkaSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
