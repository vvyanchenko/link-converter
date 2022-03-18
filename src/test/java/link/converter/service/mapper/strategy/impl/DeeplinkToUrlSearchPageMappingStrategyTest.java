package link.converter.service.mapper.strategy.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
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
class DeeplinkToUrlSearchPageMappingStrategyTest {

    @InjectMocks
    private DeeplinkToUrlSearchPageMappingStrategy testee;

    @Test
    void map_WhenDeeplinkStructureWithPageTypeSearchPassed_ThenQueryQueryParamAdded() {
        Map<PathVariable, String> expectedPathVariables = new LinkedHashMap<>();
        expectedPathVariables.put(PathVariable.SEARCH, "sr");

        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.QUERY, "query");

        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);

        when(deeplinkStructure.getPageType()).thenReturn(PageType.SEARCH);
        when(deeplinkStructure.getQueryParameters()).thenReturn(expectedQueryParams);

        LinkStructureDto urlStructure = testee.map(deeplinkStructure);

        assertThat(urlStructure.getLinkType()).isEqualTo(LinkType.URL);
        assertThat(urlStructure.getPageType()).isEqualTo(PageType.SEARCH);
        assertThat(urlStructure.getPathVariables()).containsExactlyEntriesOf(expectedPathVariables);
        assertThat(urlStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
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
        assertThat(testee.getPageType()).isEqualTo(PageType.SEARCH);
    }

}