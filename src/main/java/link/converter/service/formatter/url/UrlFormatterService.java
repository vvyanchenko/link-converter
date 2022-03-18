package link.converter.service.formatter.url;

import link.converter.service.dto.LinkStructureDto;

/**
 * Service required to format given url structure to a corresponding string
 */
public interface UrlFormatterService {

    /**
     * Formats given URL structure to the corresponding string
     *
     * @param url - URL structure to be formatted
     * @return - URL string
     */
    String format(LinkStructureDto url);

}
