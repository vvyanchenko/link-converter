package link.converter.exception;

/**
 * Class representing exception that can arise during link formatting process
 */
public class MalformedLinkException extends RuntimeException {

    public MalformedLinkException(String message, Throwable cause) {
        super(message, cause);
    }

}
