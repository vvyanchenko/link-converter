package link.converter.service.mapper.strategy.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.service.mapper.strategy.LinkMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class implementing Deeplink-to-URL mapping strategy for SEARCH page type
 */
@Component
public class DeeplinkToUrlSearchPageMappingStrategy implements LinkMappingStrategy {

    /**
     * Maps given Deeplink structure to the corresponding URL structure. Mapped fields:
     * - pageType
     * - linkType
     * - pathVariables
     * - queryParameters
     *
     * @param deeplink - incoming Deeplink structure mapping source
     * @return URL structure mapping target
     */
    @Override
    public LinkStructureDto map(LinkStructureDto deeplink) {
        Map<QueryParameter, String> urlQueryParameters = new TreeMap<>();
        urlQueryParameters.put(QueryParameter.QUERY, deeplink.getQueryParameters().get(QueryParameter.QUERY));
        Map<PathVariable, String> urlPathVariables = new TreeMap<>();
        urlPathVariables.put(PathVariable.SEARCH, PathVariable.SEARCH.getValue());
        return LinkStructureDto.builder()
                .setPageType(deeplink.getPageType())
                .setLinkType(LinkType.URL)
                .setQueryParameters(urlQueryParameters)
                .setPathVariables(urlPathVariables)
                .build();
    }

    /**
     * @return - source link type
     */
    @Override
    public LinkType getSourceLinkType() {
        return LinkType.DEEPLINK;
    }

    /**
     * @return - target link type
     */
    @Override
    public LinkType getTargetLinkType() {
        return LinkType.URL;
    }

    /**
     * @return - links page type
     */
    @Override
    public PageType getPageType() {
        return PageType.SEARCH;
    }

}
