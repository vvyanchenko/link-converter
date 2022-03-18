package link.converter.service.formatter.deeplink.impl;

import link.converter.config.DeeplinkConfigProperties;
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
import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultDeeplinkFormatterServiceTest {

    private static final String DEFAULT_DEEPLINK_SCHEME = "scheme";

    @Mock
    private DeeplinkConfigProperties deeplinkConfigProperties;

    @InjectMocks
    private DefaultDeeplinkFormatterService testee;

    @BeforeEach
    private void setUp() {
        lenient().when(deeplinkConfigProperties.getScheme()).thenReturn(DEFAULT_DEEPLINK_SCHEME);
    }

    @Test
    void format_WhenUrlWithTurkishLettersPassed_ThenUrlStringWithEncodedTurkishLettersReturned() {
        Map<QueryParameter, String> queryParameters = new EnumMap<>(QueryParameter.class);
        queryParameters.put(QueryParameter.PAGE, "Search");
        queryParameters.put(QueryParameter.QUERY, "çğöşüÇĞÖŞÜ");
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);
        when(urlStructure.getQueryParameters()).thenReturn(queryParameters);

        String urlString = testee.format(urlStructure);

        assertThat(urlString).isEqualTo("scheme://?Page=Search&Query=%C3%A7%C4%9F%C3%B6%C5%9F%C3%BC%C3%87%C4%9E%C3%96%C5%9E%C3%9C");
    }

    @Test
    void format_WhenValidDeeplinkPassed_ThenDeeplinkStringReturned() {
        Map<QueryParameter, String> queryParameters = new EnumMap<>(QueryParameter.class);
        queryParameters.put(QueryParameter.PAGE, "Product");
        queryParameters.put(QueryParameter.CONTENT_ID, "çöüÇĞ");
        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);
        when(deeplinkStructure.getQueryParameters()).thenReturn(queryParameters);

        String urlString = testee.format(deeplinkStructure);

        assertThat(urlString).isEqualTo("scheme://?Page=Product&ContentId=%C3%A7%C3%B6%C3%BC%C3%87%C4%9E");
    }

    @Test
    void format_WhenMalformedUrl_ThenMalformedLinkExceptionThrown() {
        Map<QueryParameter, String> queryParameters = new EnumMap<>(QueryParameter.class);
        queryParameters.put(QueryParameter.PAGE, "Product");
        queryParameters.put(QueryParameter.CONTENT_ID, "çğÇĞÖŞ");
        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);
        String invalidScheme = ":::";

        when(deeplinkStructure.getQueryParameters()).thenReturn(queryParameters);

        when(deeplinkConfigProperties.getScheme()).thenReturn(invalidScheme);

        String errorMessage = "Can't format deeplink [:::://?Page=Product&ContentId=%C3%A7%C4%9F%C3%87%C4%9E%C3%96%C5%9E] - invalid URI syntax";
        assertThatThrownBy(() -> testee.format(deeplinkStructure))
                .isInstanceOf(MalformedLinkException.class)
                .hasCauseExactlyInstanceOf(URISyntaxException.class)
                .hasMessage(errorMessage);
    }

}