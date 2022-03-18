package link.converter.service.mapper.strategy.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeeplinkToUrlHomePageMappingStrategyTest {

    @InjectMocks
    private DeeplinkToUrlHomePageMappingStrategy testee;

    @Test
    void map_WhenDeeplinkStructureWithPageTypeHomePassed_ThenNoQueryParamsAdded() {
        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);

        when(deeplinkStructure.getPageType()).thenReturn(PageType.OTHER);

        LinkStructureDto urlStructure = testee.map(deeplinkStructure);

        assertThat(urlStructure.getLinkType()).isEqualTo(LinkType.URL);
        assertThat(urlStructure.getPageType()).isEqualTo(PageType.OTHER);
        assertThat(urlStructure.getPathVariables()).isEmpty();
        assertThat(urlStructure.getQueryParameters()).isEmpty();
        assertThat(urlStructure.getScheme()).isNull();
        assertThat(urlStructure.getHost()).isNull();
    }

    @Test
    void getSourceLinkType_WhenInvoked_ThenSourceLinkTypeReturned() {
        assertThat(testee.getSourceLinkType()).isEqualTo(LinkType.DEEPLINK);
    }

    @Test
    void getTargetLinkType_WhenInvoked_ThenTargetLinkTypeReturned() {
        assertThat(testee.getTargetLinkType()).isEqualTo(LinkType.URL);
    }

    @Test
    void getPageType_WhenInvoked_ThenPageTypeReturned() {
        assertThat(testee.getPageType()).isEqualTo(PageType.OTHER);
    }

}