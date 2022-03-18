package link.converter.service.mapper;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;

/**
 * Service required to map link structure of the given source type to the link structure of the given target type
 */
public interface LinkMapperService {

    /**
     * Maps link structure of the source type to the link structure of the target type
     *
     * @param source     - source link structure
     * @param targetType - target link type
     * @return link structure of the given target type
     */
    LinkStructureDto map(LinkStructureDto source, LinkType targetType);

}
