package tp.fstl.nfe101.pharmacies.db.consumer.exception;

/**
 * Exception levée lorsqu'un code postal est invalide.
 */
public class InvalidPostalCodeException extends RuntimeException {
    public InvalidPostalCodeException(String message) {
        super(message);
    }
}
