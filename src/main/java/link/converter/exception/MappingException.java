package link.converter.exception;

/**
 * Class representing exception that can arise during link structure mapping process
 */
public class MappingException extends RuntimeException{

    public MappingException(String message) {
        super(message);
    }

}
