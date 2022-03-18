package link.converter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.net.URI;

/**
 * Dto representing request for conversion of deeplink to any other link type
 */
@Getter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class DeeplinkRequestDto {

    @NotNull
    private URI deeplink;

}