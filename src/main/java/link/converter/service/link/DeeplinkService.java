package link.converter.service.link;

import link.converter.controller.dto.DeeplinkRequestDto;
import link.converter.controller.dto.UrlResponseDto;

/**
 * Service required to convert deeplink to other link types
 */
public interface DeeplinkService {

    /**
     * Converts Deeplink to URL
     *
     * @param deeplinkDto - incoming Deeplink to be converted to url
     * @return - URL response
     */
    UrlResponseDto convertToUrl(DeeplinkRequestDto deeplinkDto);

}
