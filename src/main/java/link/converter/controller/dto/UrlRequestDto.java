package link.converter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.net.URL;

/**
 * Dto representing request for conversion of url to any other link type
 */
@Getter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDto {

    @NotNull
    private URL url;

}
