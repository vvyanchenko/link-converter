package link.converter.service.mapper.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.service.mapper.impl.DefaultLinkMapperService.MappingStrategyKey;
import link.converter.service.mapper.strategy.LinkMappingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultLinkMapperServiceTest {

    @Mock
    private Map<MappingStrategyKey, LinkMappingStrategy> sourceToTargetMappingStrategies;

    @Captor
    private ArgumentCaptor<MappingStrategyKey> mappingStrategyKeyArgumentCaptor;

    @InjectMocks
    private DefaultLinkMapperService testee;

    @Test
    void map_WhenMappingStrategyPresent_ThenMappedFromSourceToTargetLinkType() {
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);
        LinkStructureDto expectedDeeplinkStructure = mock(LinkStructureDto.class);

        LinkMappingStrategy urlToDeeplinkStrategy = mock(LinkMappingStrategy.class);
        when(urlToDeeplinkStrategy.map(urlStructure)).thenReturn(expectedDeeplinkStructure);

        when(urlStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(urlStructure.getLinkType()).thenReturn(LinkType.URL);
        when(sourceToTargetMappingStrategies.get(mappingStrategyKeyArgumentCaptor.capture())).thenReturn(urlToDeeplinkStrategy);

        LinkStructureDto actualDeeplinkStructure = testee.map(urlStructure, LinkType.DEEPLINK);

        assertThat(actualDeeplinkStructure).isEqualTo(expectedDeeplinkStructure);

        List<MappingStrategyKey> mappingStrategies = mappingStrategyKeyArgumentCaptor.getAllValues();
        assertThat(mappingStrategies).hasSize(1);
        MappingStrategyKey mappingStrategyKey = mappingStrategies.get(0);
        assertThat(mappingStrategyKey.getSourceLinkType()).isEqualTo(LinkType.URL);
        assertThat(mappingStrategyKey.getTargetLinkType()).isEqualTo(LinkType.DEEPLINK);
        assertThat(mappingStrategyKey.getPageType()).isEqualTo(PageType.PRODUCT);
    }

    @Test
    void map_WhenNoMappingStrategyPresent_ThenRuntimeExceptionThrown() {
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(urlStructure.getLinkType()).thenReturn(LinkType.URL);

        assertThatThrownBy(() -> testee.map(urlStructure, LinkType.DEEPLINK))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Can't find strategy to map link of pageType=[PRODUCT] and sourceType=[URL] to targetType=[DEEPLINK]");
    }

}