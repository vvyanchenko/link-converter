package link.converter.service.decomposer.url.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.domain.enumeration.QueryParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultUrlDecomposerServiceTest {

    @InjectMocks
    private DefaultUrlDecomposerService testee;

    @Test
    void decompose_WhenValidUrlPassed_ThenDecomposedUrlReturned() throws Exception {
        URL url = new URL("https://hostname/brand/product-p-contentId?boutiqueId=bId&merchantId=mId");
        Map<PathVariable, String> expectedPathVariables = new LinkedHashMap<>();
        expectedPathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, "brand");
        expectedPathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, "product-p-contentId");
        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");
        expectedQueryParams.put(QueryParameter.MERCHANT_ID, "mId");

        LinkStructureDto urlStructure = testee.decompose(url);

        assertThat(urlStructure.getLinkType()).isEqualTo(LinkType.URL);
        assertThat(urlStructure.getPageType()).isEqualTo(PageType.PRODUCT);
        assertThat(urlStructure.getScheme()).isEqualTo("https");
        assertThat(urlStructure.getHost()).isEqualTo("hostname");
        assertThat(urlStructure.getPathVariables()).containsExactlyEntriesOf(expectedPathVariables);
        assertThat(urlStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
    }

    @ParameterizedTest
    @MethodSource("getUrls")
    void decompose_WhenNoPathVariablesPassed_ThenDecomposedUrlWithoutPathVariablesReturned(URL url) throws Exception {
        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");
        expectedQueryParams.put(QueryParameter.MERCHANT_ID, "mId");

        LinkStructureDto urlStructure = testee.decompose(url);

        assertThat(urlStructure.getLinkType()).isEqualTo(LinkType.URL);
        assertThat(urlStructure.getPageType()).isEqualTo(PageType.OTHER);
        assertThat(urlStructure.getScheme()).isEqualTo("https");
        assertThat(urlStructure.getHost()).isEqualTo("hostname");
        assertThat(urlStructure.getPathVariables()).isEmpty();
        assertThat(urlStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
    }

    @Test
    void decompose_WhenOnlySearchPathVariablePassed_ThenDecomposedUrlWithSearchPathVariablesReturned() throws Exception {
        URL url = new URL("https://hostname/sr?boutiqueId=bId&merchantId=mId");
        Map<PathVariable, String> expectedPathVariables = new LinkedHashMap<>();
        expectedPathVariables.put(PathVariable.SEARCH, "sr");
        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.BOUTIQUE_ID, "bId");
        expectedQueryParams.put(QueryParameter.MERCHANT_ID, "mId");

        LinkStructureDto urlStructure = testee.decompose(url);

        assertThat(urlStructure.getLinkType()).isEqualTo(LinkType.URL);
        assertThat(urlStructure.getPageType()).isEqualTo(PageType.SEARCH);
        assertThat(urlStructure.getScheme()).isEqualTo("https");
        assertThat(urlStructure.getHost()).isEqualTo("hostname");
        assertThat(urlStructure.getPathVariables()).containsExactlyEntriesOf(expectedPathVariables);
        assertThat(urlStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
    }

    private static Stream<Arguments> getUrls() throws Exception {
        return Stream.of(
                // no path variables
                Arguments.of(new URL("https://hostname?boutiqueId=bId&merchantId=mId")),
                // only one path variables and not search page type
                Arguments.of(new URL("https://hostname/brand?boutiqueId=bId&merchantId=mId")),
                // two path variables and no productNameAndContentId separator present
                Arguments.of(new URL("https://hostname/brand/product--contentId?boutiqueId=bId&merchantId=mId")),
                // more than two path variables
                Arguments.of(new URL("https://hostname/brand/product-p-contentId/unknown/?boutiqueId=bId&merchantId=mId")),
                // neither search nor brandOrCategoryName path variables present
                Arguments.of(new URL("https://hostname/unknown/?boutiqueId=bId&merchantId=mId"))
        );
    }

}