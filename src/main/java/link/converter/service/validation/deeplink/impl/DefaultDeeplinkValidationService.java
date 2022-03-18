package link.converter.service.validation.deeplink.impl;

import link.converter.config.DeeplinkConfigProperties;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.exception.LinkValidationException;
import link.converter.service.dto.LinkStructureDto;
import link.converter.service.validation.deeplink.DeeplinkValidationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service required to validate incoming Deeplink structure e.g. scheme
 */
@Service
@AllArgsConstructor
public class DefaultDeeplinkValidationService implements DeeplinkValidationService {

    private static final String INVALID_DEEPLINK_SCHEME_ERROR_MESSAGE =
            "Provided deeplink scheme [%s] does not match with the required one [%s]";
    private static final String MISSING_CONTENT_ID_QUERY_PARAM_ERROR_MESSAGE =
            "Product detail page must contain ContentId query string";

    private final DeeplinkConfigProperties deeplinkConfigProperties;

    /**
     * Checks if the given Deeplink structure valid, e.g.
     * - scheme corresponds to the application defined scheme
     * - Deeplink contains ContentId query param in case page type is Product
     *
     * @param deeplinkStructure - Deeplink structure to be validated
     */
    @Override
    public void assertDeeplinkValid(LinkStructureDto deeplinkStructure) {
        String validScheme = deeplinkConfigProperties.getScheme();
        String originalDeeplinkScheme = deeplinkStructure.getScheme();
        if (!validScheme.equals(originalDeeplinkScheme)) {
            throw new LinkValidationException(
                    String.format(INVALID_DEEPLINK_SCHEME_ERROR_MESSAGE, originalDeeplinkScheme, validScheme));
        }
        assertProductPageQueryParamsValid(deeplinkStructure, deeplinkStructure.getQueryParameters());
    }

    private void assertProductPageQueryParamsValid(LinkStructureDto deeplinkStructure, Map<QueryParameter, String> queryParameters) {
        if (PageType.PRODUCT == deeplinkStructure.getPageType() &&
                !queryParameters.containsKey(QueryParameter.CONTENT_ID)) {
            throw new LinkValidationException(MISSING_CONTENT_ID_QUERY_PARAM_ERROR_MESSAGE);
        }
    }

}
