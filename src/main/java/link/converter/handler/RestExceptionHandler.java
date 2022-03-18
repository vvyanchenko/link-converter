package link.converter.handler;

import link.converter.controller.dto.ErrorResponseDto;
import link.converter.exception.LinkValidationException;
import link.converter.exception.MalformedLinkException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class required to globally handle application-specific custom exceptions as well as any other system exception
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String UNACCEPTABLE_LINK_FORMAT_ERROR_CODE = "unacceptable_link_format";
    private static final String SERVER_ERROR_ERROR_CODE = "server_error";
    private static final String SERVER_ERROR_ERROR_MESSAGE = "Server error occurred";

    /**
     * Handles application-specific custom exceptions
     *
     * @param ex - exception to be handled
     * @param request - web request to inject exception info to
     * @return response entity with detailed information of the given exception
     */
    @ExceptionHandler(value = {
            IllegalStateException.class,
            MalformedLinkException.class,
            LinkValidationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponseDto handleBadRequest(RuntimeException ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return new ErrorResponseDto(UNACCEPTABLE_LINK_FORMAT_ERROR_CODE, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any system exception as the default handler
     *
     * @param ex - exception to be handled
     * @param request - web request to inject exception info to
     * @return response entity with detailed information of the given exception
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponseDto handleDefault(Exception ex, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return new ErrorResponseDto(
                SERVER_ERROR_ERROR_CODE, SERVER_ERROR_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
