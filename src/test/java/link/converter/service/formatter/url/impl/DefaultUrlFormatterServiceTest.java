package link.converter.service.formatter.url.impl;

import link.converter.config.UrlConfigProperties;
import link.converter.domain.enumeration.PathVariable;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.exception.MalformedLinkException;
import link.converter.service.dto.LinkStructureDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUrlFormatterServiceTest {

    private static final String DEFAULT_URL_HOSTNAME = "hostname";
    private static final String DEFAULT_PROTOCOL = "https";

    @Mock
    private UrlConfigProperties urlConfigProperties;

    @InjectMocks
    private DefaultUrlFormatterService testee;

    @BeforeEach
    private void setUp() {
        when(urlConfigProperties.getHostname()).thenReturn(DEFAULT_URL_HOSTNAME);
        when(urlConfigProperties.getProtocol()).thenReturn(DEFAULT_PROTOCOL);
    }

    @Test
    void format_WhenUrlWithTurkishLettersPassed_ThenUrlStringWithEncodedTurkishLettersReturned() {
        Map<PathVariable, String> pathVariables = new EnumMap<>(PathVariable.class);
        pathVariables.put(PathVariable.SEARCH, "sr");
        Map<QueryParameter, String> queryParameters = new EnumMap<>(QueryParameter.class);
        queryParameters.put(QueryParameter.QUERY, "çğöşüÇĞÖŞÜ");
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);
        when(urlStructure.getQueryParameters()).thenReturn(queryParameters);
        when(urlStructure.getPathVariables()).thenReturn(pathVariables);

        String urlString = testee.format(urlStructure);

        assertThat(urlString).isEqualTo("https://hostname/sr?q=%C3%A7%C4%9F%C3%B6%C5%9F%C3%BC%C3%87%C4%9E%C3%96%C5%9E%C3%9C");
    }

    @Test
    void format_WhenHomePageTypeUrlPassed_ThenHomePageUrlStringReturned() {
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        String urlString = testee.format(urlStructure);

        assertThat(urlString).isEqualTo("https://hostname");
    }

    @Test
    void format_WhenSearchPageTypeUrlPassed_ThenSearchPageUrlStringReturned() {
        Map<PathVariable, String> pathVariables = new EnumMap<>(PathVariable.class);
        pathVariables.put(PathVariable.SEARCH, "sr");

        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getPathVariables()).thenReturn(pathVariables);

        String urlString = testee.format(urlStructure);

        assertThat(urlString).isEqualTo("https://hostname/sr");
    }

    @Test
    void format_WhenProductPageTypeUrlPassed_ThenProductPageUrlStringReturned() {
        Map<PathVariable, String> pathVariables = new EnumMap<>(PathVariable.class);
        pathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, "çğöĞÖŞÜ");
        pathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, "product-p-çğöşŞÜ");
        Map<QueryParameter, String> queryParameters = new EnumMap<>(QueryParameter.class);
        queryParameters.put(QueryParameter.BOUTIQUE_ID, "boutiqueId");
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);
        when(urlStructure.getQueryParameters()).thenReturn(queryParameters);
        when(urlStructure.getPathVariables()).thenReturn(pathVariables);

        String urlString = testee.format(urlStructure);

        assertThat(urlString).isEqualTo("https://hostname/%C3%A7%C4%9F%C3%B6%C4%9E%C3%96%C5%9E%C3%9C/product-p-%C3%A7%C4%9F%C3%B6%C5%9F%C5%9E%C3%9C?boutiqueId=boutiqueId");
    }

    @Test
    void format_WhenNoQueryParametersPresent_ThenUrlStringWithoutQueryParamsReturnedReturned() {
        Map<PathVariable, String> pathVariables = new EnumMap<>(PathVariable.class);
        pathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, "brandOrCategory");
        pathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, "product-p-şüÇĞÖŞÜ");
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);
        when(urlStructure.getQueryParameters()).thenReturn(Collections.emptyMap());
        when(urlStructure.getPathVariables()).thenReturn(pathVariables);

        String urlString = testee.format(urlStructure);

        assertThat(urlString).isEqualTo("https://hostname/brandOrCategory/product-p-%C5%9F%C3%BC%C3%87%C4%9E%C3%96%C5%9E%C3%9C");
    }

    @Test
    void format_WhenMalformedUrl_ThenMalformedLinkExceptionThrown() {
        Map<PathVariable, String> pathVariables = new EnumMap<>(PathVariable.class);
        pathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, "brandOrCategory");
        pathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, "product-p-çğüÖŞÜ");
        Map<QueryParameter, String> queryParameters = new EnumMap<>(QueryParameter.class);
        queryParameters.put(QueryParameter.BOUTIQUE_ID, "öşüÇŞÜ");
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);
        String invalidProtocol = ":::";

        when(urlStructure.getQueryParameters()).thenReturn(queryParameters);
        when(urlStructure.getPathVariables()).thenReturn(pathVariables);

        when(urlConfigProperties.getProtocol()).thenReturn(invalidProtocol);

        String errorMessage =
                "Can't format URL [:::://hostname/brandOrCategory/product-p-%C3%A7%C4%9F%C3%BC%C3%96%C5%9E%C3%9C?boutiqueId=%C3%B6%C5%9F%C3%BC%C3%87%C5%9E%C3%9C] - invalid URL syntax";
        assertThatThrownBy(() -> testee.format(urlStructure))
                .isInstanceOf(MalformedLinkException.class)
                .hasCauseExactlyInstanceOf(URISyntaxException.class)
                .hasMessage(errorMessage);
    }

}