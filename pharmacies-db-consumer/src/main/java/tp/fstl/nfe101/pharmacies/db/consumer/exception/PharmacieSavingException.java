package tp.fstl.nfe101.pharmacies.db.consumer.exception;

/**
 * Exception levée lorsqu'une erreur survient lors de la sauvegarde d'une pharmacie.
 */
public class PharmacieSavingException extends RuntimeException {
    public PharmacieSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}
