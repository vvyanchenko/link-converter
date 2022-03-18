package link.converter.service.decomposer.url;

import link.converter.service.dto.LinkStructureDto;
import link.converter.service.decomposer.LinkDecomposerService;

import java.net.URL;

/**
 * Service required to decompose URL into structural components
 */
public interface UrlDecomposerService extends LinkDecomposerService {

    /**
     * Decomposes URL onto structural elements
     *
     * @param url - incoming URL to be decomposed
     * @return - given URL structure
     */
    LinkStructureDto decompose(URL url);

}
