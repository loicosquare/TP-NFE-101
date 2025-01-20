package tp.fstl.nfe101.pharmacies.db.consumer.exception;

/**
 * Exception levée lorsqu'une ressource n'est pas trouvée.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
