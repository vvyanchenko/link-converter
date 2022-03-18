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

import static link.converter.constants.LinkConstants.DEFAULT_URL_BRAND_NAME_OR_CATEGORY;
import static link.converter.constants.LinkConstants.DEFAULT_URL_PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeeplinkToUrlProductPageMappingStrategyTest {

    @InjectMocks
    private DeeplinkToUrlProductPageMappingStrategy testee;

    @Test
    void map_WhenDeeplinkStructureWithPageTypeProductPassed_ThenCorrespondingQueryParamsAdded() {
        Map<QueryParameter, String> deeplinkQueryParams = new LinkedHashMap<>();
        deeplinkQueryParams.put(QueryParameter.CONTENT_ID, "cId");
        deeplinkQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");
        deeplinkQueryParams.put(QueryParameter.MERCHANT_ID, "mId");

        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");
        expectedQueryParams.put(QueryParameter.MERCHANT_ID, "mId");

        Map<PathVariable, String> expectedPathVariables = new LinkedHashMap<>();
        expectedPathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, DEFAULT_URL_BRAND_NAME_OR_CATEGORY);
        expectedPathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, DEFAULT_URL_PRODUCT_NAME + "-p-cId");

        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);

        when(deeplinkStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(deeplinkStructure.getQueryParameters()).thenReturn(deeplinkQueryParams);

        LinkStructureDto urlStructure = testee.map(deeplinkStructure);

        assertThat(urlStructure.getLinkType()).isEqualTo(LinkType.URL);
        assertThat(urlStructure.getPageType()).isEqualTo(PageType.PRODUCT);
        assertThat(urlStructure.getPathVariables()).containsExactlyEntriesOf(expectedPathVariables);
        assertThat(urlStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
        assertThat(urlStructure.getScheme()).isNull();
        assertThat(urlStructure.getHost()).isNull();
    }

    @Test
    void map_WhenOptionalQueryParamAbsent_ThenOptionalQueryParamOmitted() {
        Map<QueryParameter, String> deeplinkQueryParams = new LinkedHashMap<>();
        deeplinkQueryParams.put(QueryParameter.CONTENT_ID, "cId");
        deeplinkQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");

        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");

        Map<PathVariable, String> expectedPathVariables = new LinkedHashMap<>();
        expectedPathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, DEFAULT_URL_BRAND_NAME_OR_CATEGORY);
        expectedPathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, DEFAULT_URL_PRODUCT_NAME + "-p-cId");

        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);

        when(deeplinkStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(deeplinkStructure.getQueryParameters()).thenReturn(deeplinkQueryParams);

        LinkStructureDto urlStructure = testee.map(deeplinkStructure);

        assertThat(urlStructure.getLinkType()).isEqualTo(LinkType.URL);
        assertThat(urlStructure.getPageType()).isEqualTo(PageType.PRODUCT);
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
        assertThat(testee.getPageType()).isEqualTo(PageType.PRODUCT);
    }

}