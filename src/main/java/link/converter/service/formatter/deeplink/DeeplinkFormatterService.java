package link.converter.service.formatter.deeplink;

import link.converter.service.dto.LinkStructureDto;

/**
 * Service required to format given Deeplink structure to a corresponding string
 */
public interface DeeplinkFormatterService {

    /**
     * Formats given Deeplink structure to the corresponding string
     *
     * @param deeplink - Deeplink structure to be formatted
     * @return - Deeplink string
     */
    String format(LinkStructureDto deeplink);

}
