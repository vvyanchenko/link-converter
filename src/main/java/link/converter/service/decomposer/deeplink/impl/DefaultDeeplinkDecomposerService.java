package link.converter.service.decomposer.deeplink.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.service.decomposer.deeplink.DeeplinkDecomposerService;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;

/**
 * Service required to decompose Deeplink into structural components
 */
@Service
public class DefaultDeeplinkDecomposerService implements DeeplinkDecomposerService {

    /**
     * Extracts the following structural elements from Deeplink:
     * - page type
     * - link type
     * - query parameters
     *
     * @param deeplink - incoming Deeplink to be decomposed
     * @return - given Deeplink structure
     */
    @Override
    public LinkStructureDto decompose(URI deeplink) {
        Map<QueryParameter, String> queryParameters = getQueryParameters(deeplink.getQuery());
        PageType pageType = getDeeplinkPageType(queryParameters);
        return getDeeplinkStructure(deeplink, pageType, queryParameters);
    }

    private PageType getDeeplinkPageType(Map<QueryParameter, String> queryParameters) {
        String pageQueryParameterValue = queryParameters.get(QueryParameter.PAGE);
        return PageType.of(pageQueryParameterValue);
    }

    private LinkStructureDto getDeeplinkStructure(URI deeplink,
                                                  PageType pageType,
                                                  Map<QueryParameter, String> queryParameters) {
        return LinkStructureDto.builder()
                .setLinkType(LinkType.DEEPLINK)
                .setPageType(pageType)
                .setScheme(deeplink.getScheme())
                .setHost(deeplink.getHost())
                .setQueryParameters(queryParameters)
                .build();
    }

}
