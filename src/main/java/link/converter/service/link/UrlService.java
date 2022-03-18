package link.converter.service.link;

import link.converter.controller.dto.DeeplinkResponseDto;
import link.converter.controller.dto.UrlRequestDto;

/**
 * Service required to convert URL to other link types
 */
public interface UrlService {

    /**
     * Converts URL to deeplink
     *
     * @param urlDto - incoming URL to be converted to deeplink
     * @return - Deeplink response
     */
    DeeplinkResponseDto convertToDeeplink(UrlRequestDto urlDto);

}
