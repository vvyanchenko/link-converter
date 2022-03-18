package link.converter.exception;

/**
 * Class representing exception that can arise during link validation process
 */
public class LinkValidationException extends RuntimeException {

    public LinkValidationException(String message) {
        super(message);
    }

}
