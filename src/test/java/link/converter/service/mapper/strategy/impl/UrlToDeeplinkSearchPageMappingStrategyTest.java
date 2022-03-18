package link.converter.service.mapper.strategy.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.QueryParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlToDeeplinkSearchPageMappingStrategyTest {

    @InjectMocks
    private UrlToDeeplinkSearchPageMappingStrategy testee;

    @Test
    void map_WhenUrlStructureWithPageTypeSearchPassed_ThenQueryQueryParamAdded() {
        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.PAGE, "Search");
        expectedQueryParams.put(QueryParameter.QUERY, "query");

        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getPageType()).thenReturn(PageType.SEARCH);
        when(urlStructure.getQueryParameters()).thenReturn(expectedQueryParams);

        LinkStructureDto deeplinkStructure = testee.map(urlStructure);

        assertThat(deeplinkStructure.getLinkType()).isEqualTo(LinkType.DEEPLINK);
        assertThat(deeplinkStructure.getPageType()).isEqualTo(PageType.SEARCH);
        assertThat(deeplinkStructure.getPathVariables()).isEmpty();
        assertThat(deeplinkStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
        assertThat(deeplinkStructure.getScheme()).isNull();
        assertThat(deeplinkStructure.getHost()).isNull();
    }

    @Test
    void getSourceLinkType_WhenInvoked_ThenSourceLinkTypeReturned() {
        assertThat(testee.getSourceLinkType()).isEqualTo(LinkType.URL);
    }

    @Test
    void getTargetLinkType_WhenInvoked_ThenTargetLinkTypeReturned() {
        assertThat(testee.getTargetLinkType()).isEqualTo(LinkType.DEEPLINK);
    }

    @Test
    void getPageType_WhenInvoked_ThenPageTypeReturned() {
        assertThat(testee.getPageType()).isEqualTo(PageType.SEARCH);
    }

}