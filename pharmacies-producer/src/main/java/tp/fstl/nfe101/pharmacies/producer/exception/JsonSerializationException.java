package tp.fstl.nfe101.pharmacies.producer.exception;

/**
 * Exception générée lorsqu'une erreur se produit lors de la sérialisation d'un objet en JSON.
 */
public class JsonSerializationException extends RuntimeException {
    public JsonSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
