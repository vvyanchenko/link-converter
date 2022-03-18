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

import static link.converter.constants.LinkConstants.DEFAULT_URL_PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlToDeeplinkProductPageMappingStrategyTest {

    @InjectMocks
    private UrlToDeeplinkProductPageMappingStrategy testee;

    @Test
    void map_WhenUrlStructureWithPageTypeProductPassed_ThenCorrespondingQueryParamsAdded() {
        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.PAGE, "Product");
        expectedQueryParams.put(QueryParameter.CONTENT_ID, "cId");
        expectedQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");
        expectedQueryParams.put(QueryParameter.MERCHANT_ID, "mId");

        Map<PathVariable, String> pathVariables = new LinkedHashMap<>();
        pathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, DEFAULT_URL_PRODUCT_NAME + "-p-cId");

        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(urlStructure.getQueryParameters()).thenReturn(expectedQueryParams);
        when(urlStructure.getPathVariables()).thenReturn(pathVariables);

        LinkStructureDto deeplinkStructure = testee.map(urlStructure);

        assertThat(deeplinkStructure.getLinkType()).isEqualTo(LinkType.DEEPLINK);
        assertThat(deeplinkStructure.getPageType()).isEqualTo(PageType.PRODUCT);
        assertThat(deeplinkStructure.getPathVariables()).isEmpty();
        assertThat(deeplinkStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
        assertThat(deeplinkStructure.getScheme()).isNull();
        assertThat(deeplinkStructure.getHost()).isNull();
    }

    @Test
    void map_WhenOptionalQueryParamAbsent_ThenOptionalQueryParamOmitted() {
        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.PAGE, "Product");
        expectedQueryParams.put(QueryParameter.CONTENT_ID, "cId");
        expectedQueryParams.put(QueryParameter.MERCHANT_ID, "mId");

        Map<PathVariable, String> pathVariables = new LinkedHashMap<>();
        pathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, DEFAULT_URL_PRODUCT_NAME + "-p-cId");

        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(urlStructure.getQueryParameters()).thenReturn(expectedQueryParams);
        when(urlStructure.getPathVariables()).thenReturn(pathVariables);

        LinkStructureDto deeplinkStructure = testee.map(urlStructure);

        assertThat(deeplinkStructure.getLinkType()).isEqualTo(LinkType.DEEPLINK);
        assertThat(deeplinkStructure.getPageType()).isEqualTo(PageType.PRODUCT);
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
        assertThat(testee.getPageType()).isEqualTo(PageType.PRODUCT);
    }

}