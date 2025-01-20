package tp.fstl.nfe101.pharmacies.db.consumer.exception;

/**
 * Exception levée lorsqu'une erreur survient lors de la récupération ou de la sauvegarde d'une ville.
 */
public class CityProcessingException extends RuntimeException {
    public CityProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
