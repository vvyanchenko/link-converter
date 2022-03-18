package link.converter.service.mapper.strategy.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.service.mapper.strategy.LinkMappingStrategy;
import org.springframework.stereotype.Component;

/**
 * Class implementing Deeplink-to-URL mapping strategy for HOME page type
 */
@Component
public class DeeplinkToUrlHomePageMappingStrategy implements LinkMappingStrategy {

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
        return LinkStructureDto.builder()
                .setPageType(deeplink.getPageType())
                .setLinkType(LinkType.URL)
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
        return PageType.OTHER;
    }

}
