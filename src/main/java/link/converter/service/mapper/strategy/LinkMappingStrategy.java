package link.converter.service.mapper.strategy;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;

/**
 * Strategy for mapping link of the given page type from a source link type to a target link type
 */
public interface LinkMappingStrategy {

    /**
     * Maps link of source link type to a target link type
     *
     * @param link - source link structure
     * @return - target link structure
     */
    LinkStructureDto map(LinkStructureDto link);

    /**
     * @return - source link type
     */
    LinkType getSourceLinkType();

    /**
     * @return - target link type
     */
    LinkType getTargetLinkType();

    /**
     * @return - links page type
     */
    PageType getPageType();

}
