package link.converter.service.mapper.strategy.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.service.mapper.strategy.LinkMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class implementing URL-to-Deeplink mapping strategy for HOME page type
 */
@Component
public class UrlToDeeplinkHomePageMappingStrategy implements LinkMappingStrategy {

    /**
     * Maps given URL structure to the corresponding Deeplink structure. Mapped fields:
     * - pageType
     * - linkType
     * - pathVariables
     * - queryParameters
     *
     * @param url - incoming URL structure mapping source
     * @return Deeplink structure mapping target
     */
    @Override
    public LinkStructureDto map(LinkStructureDto url) {
        Map<QueryParameter, String> deeplinkQueryParameters = new TreeMap<>();
        deeplinkQueryParameters.put(QueryParameter.PAGE, url.getPageType().getName());
        return LinkStructureDto.builder()
                .setPageType(url.getPageType())
                .setLinkType(LinkType.DEEPLINK)
                .setQueryParameters(deeplinkQueryParameters)
                .build();
    }

    /**
     * @return - source link type
     */
    @Override
    public LinkType getSourceLinkType() {
        return LinkType.URL;
    }

    /**
     * @return - target link type
     */
    @Override
    public LinkType getTargetLinkType() {
        return LinkType.DEEPLINK;
    }

    /**
     * @return - links page type
     */
    @Override
    public PageType getPageType() {
        return PageType.OTHER;
    }

}
