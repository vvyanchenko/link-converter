package link.converter.controller;

import link.converter.controller.dto.DeeplinkResponseDto;
import link.converter.controller.dto.UrlRequestDto;
import link.converter.service.link.UrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Controller exposing API for conversion of links of different types
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(value = "/link/converter")
public class LinkConverterController {

    private final UrlService urlService;

    /**
     *  Converts URL to Deeplink
     *
     * @param url - URL to be converted
     * @return - resulting Deeplink
     */
    @PostMapping(value = "/url/to/deeplink", produces = {MediaType.APPLICATION_JSON_VALUE})
    public DeeplinkResponseDto convertUrlToDeeplink(@RequestBody @Valid @NotNull UrlRequestDto url) {
        return urlService.convertToDeeplink(url);
    }

}
