package link.converter.service.mapper.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.exception.MappingException;
import link.converter.service.mapper.LinkMapperService;
import link.converter.service.mapper.strategy.LinkMappingStrategy;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service required to map link structure of the given source type to the link structure of the given target type
 */
@Service
@AllArgsConstructor
public class DefaultLinkMapperService implements LinkMapperService {

    private static final String NO_STRATEGY_FOUND_ERROR_MESSAGE =
            "Can't find strategy to map link of pageType=[%s] and sourceType=[%s] to targetType=[%s]";

    private final List<LinkMappingStrategy> mappingStrategies;
    private Map<MappingStrategyKey, LinkMappingStrategy> sourceToTargetMappingStrategies;

    @PostConstruct
    private void constructMappingStrategies() {
        for (LinkMappingStrategy mappingStrategy : mappingStrategies) {
            MappingStrategyKey mappingKey = new MappingStrategyKey(mappingStrategy);
            sourceToTargetMappingStrategies.putIfAbsent(mappingKey, mappingStrategy);
        }
    }

    /**
     * Maps link structure of the source type to the link structure of the target type
     * Delegates mapping to corresponding underlying mapping strategy
     *
     * @param source     - source link structure
     * @param targetType - target link type
     * @return link structure of the given target type
     */
    @Override
    public LinkStructureDto map(LinkStructureDto source, LinkType targetType) {
        LinkType sourceType = source.getLinkType();
        PageType pageType = source.getPageType();
        MappingStrategyKey mappingKey = new MappingStrategyKey(sourceType, targetType, pageType);
        return Optional.ofNullable(sourceToTargetMappingStrategies.get(mappingKey))
                .map(strategy -> strategy.map(source))
                .orElseThrow(() -> new MappingException(
                        String.format(NO_STRATEGY_FOUND_ERROR_MESSAGE, pageType, sourceType, targetType)));
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    static class MappingStrategyKey {

        private final LinkType sourceLinkType;
        private final LinkType targetLinkType;
        private final PageType pageType;

        private MappingStrategyKey(LinkMappingStrategy mappingStrategy) {
            this.sourceLinkType = mappingStrategy.getSourceLinkType();
            this.targetLinkType = mappingStrategy.getTargetLinkType();
            this.pageType = mappingStrategy.getPageType();
        }

    }

}
