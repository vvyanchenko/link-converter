package link.converter.service.validation.url;

import link.converter.service.dto.LinkStructureDto;

/**
 * Service required to validate incoming URL structure
 */
public interface UrlValidationService {

    /**
     * Checks if the given URL structure valid, e.g.
     *
     * @param url - URL structure to be validated
     */
    void assertUrlValid(LinkStructureDto url);

}
