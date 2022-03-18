package link.converter.service.validation.deeplink;

import link.converter.service.dto.LinkStructureDto;

/**
 * Service required to validate incoming Deeplink structure
 */
public interface DeeplinkValidationService {

    /**
     * Checks if the given Deeplink structure valid
     *
     * @param deeplink - Deeplink structure to be validated
     */
    void assertDeeplinkValid(LinkStructureDto deeplink);

}
