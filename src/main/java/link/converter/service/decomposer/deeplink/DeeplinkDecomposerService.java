package link.converter.service.decomposer.deeplink;

import link.converter.service.dto.LinkStructureDto;
import link.converter.service.decomposer.LinkDecomposerService;

import java.net.URI;

/**
 * Service required to decompose Deeplink into structural components
 */
public interface DeeplinkDecomposerService extends LinkDecomposerService {

    /**
     * Decomposes Deeplink onto structural elements
     *
     * @param deeplink - incoming Deeplink to be decomposed
     * @return - given Deeplink structure
     */
    LinkStructureDto decompose(URI deeplink);

}
