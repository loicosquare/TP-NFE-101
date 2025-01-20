package tp.fstl.nfe101.pharmacies.producer.exception;

/**
 * Exception générée lorsqu'une erreur se produit lors de la lecture d'un fichier CSV.
 */
public class CsvParsingException extends RuntimeException {
    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
