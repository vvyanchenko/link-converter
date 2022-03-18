package link.converter.service.validation.url.impl;

import link.converter.config.UrlConfigProperties;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.exception.LinkValidationException;
import link.converter.service.dto.LinkStructureDto;
import link.converter.service.validation.url.UrlValidationService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static link.converter.constants.LinkConstants.*;

/**
 * Service required to validate incoming URL structure e.g. protocol, host
 */
@Service
@AllArgsConstructor
public class DefaultUrlValidationService implements UrlValidationService {

    private static final String INVALID_URL_HOST_ERROR_MESSAGE =
            "Provided URL host [%s] does not match with the required one [%s]";
    private static final String INVALID_URL_PROTOCOL_ERROR_MESSAGE =
            "Provided URL protocol [%s] does not match with the required one [%s]";
    private static final String MISSING_CONTENT_ID_PATH_VARIABLE_ERROR_MESSAGE =
            "Product detail page must contain contentId path variable";

    private final UrlConfigProperties urlConfigProperties;

    /**
     * Checks if the given URL structure valid, e.g.
     * - hostname corresponds to the application defined hostname
     * - protocol corresponds to the application defined protocol
     *
     * @param url - URL structure to be validated
     */
    @Override
    public void assertUrlValid(LinkStructureDto url) {
        String validHostName = urlConfigProperties.getHostname();
        String originalUrlHostName = url.getHost();
        if (!validHostName.equals(originalUrlHostName)) {
            throw new LinkValidationException(
                    String.format(INVALID_URL_HOST_ERROR_MESSAGE, originalUrlHostName, validHostName));
        }
        String validProtocol = urlConfigProperties.getProtocol();
        String originalUrlProtocol = url.getScheme();
        if (!validProtocol.equals(originalUrlProtocol)) {
            throw new LinkValidationException(
                    String.format(INVALID_URL_PROTOCOL_ERROR_MESSAGE, originalUrlProtocol, validProtocol));
        }
        assertProductPagePathVariablesValid(url);
    }

    private void assertProductPagePathVariablesValid(LinkStructureDto urlStructure) {
        if (PageType.PRODUCT == urlStructure.getPageType()) {
            assertContentIdPresent(urlStructure.getPathVariables());
        }
    }

    private void assertContentIdPresent(Map<PathVariable, String> pathVariables) {
        String productNameAndContentId = pathVariables.get(PathVariable.PRODUCT_NAME_AND_CONTENT_ID);
        Optional<String> contentIdOpt = getContentId(productNameAndContentId);
        if (contentIdOpt.isEmpty()) {
            throw new LinkValidationException(MISSING_CONTENT_ID_PATH_VARIABLE_ERROR_MESSAGE);
        }
    }

    private Optional<String> getContentId(String productNameAndContentId) {
        String[] productNameAndContentIdArray = productNameAndContentId.split(PRODUCT_NAME_AND_CONTENT_ID_SEPARATOR);
        if (productNameAndContentIdArray.length != TWO_ELEMENT_ARRAY_LENGTH) {
            return Optional.empty();
        }
        String contentId = productNameAndContentIdArray[FIRST_ARRAY_ELEMENT];
        return StringUtils.isBlank(contentId) ? Optional.empty() : Optional.of(contentId);
    }

}
