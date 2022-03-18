package link.converter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Dto representing basic information about exception thrown by application
 */
@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private String errorCode;
    private String errorMessage;
    private HttpStatus statusCode;

}
