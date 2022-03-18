package link.converter.service.validation.url.impl;

import link.converter.config.UrlConfigProperties;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.exception.LinkValidationException;
import link.converter.service.dto.LinkStructureDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUrlValidationServiceTest {

    @Mock
    private UrlConfigProperties urlConfigProperties;

    @InjectMocks
    private DefaultUrlValidationService testee;

    @Test
    void assertUrlValid_WhenValidUrlProvided_ThenMethodExecutedSuccessfully() {
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getHost()).thenReturn("host");
        when(urlStructure.getScheme()).thenReturn("protocol");
        when(urlConfigProperties.getHostname()).thenReturn("host");
        when(urlConfigProperties.getProtocol()).thenReturn("protocol");

        assertThatNoException()
                .isThrownBy(() -> testee.assertUrlValid(urlStructure));
    }

    @Test
    void assertUrlValid_WhenUrlWithInvalidHostnameProvided_ThenLinkValidationExceptionThrown() {
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getHost()).thenReturn("invalid_host");
        when(urlConfigProperties.getHostname()).thenReturn("valid_host");

        assertThatThrownBy(() -> testee.assertUrlValid(urlStructure))
                .isInstanceOf(LinkValidationException.class)
                .hasMessage("Provided URL host [invalid_host] does not match with the required one [valid_host]");
    }

    @Test
    void assertUrlValid_WhenUrlWithInvalidProtocolProvided_ThenLinkValidationExceptionThrown() {
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getHost()).thenReturn("host");
        when(urlStructure.getScheme()).thenReturn("invalid_protocol");
        when(urlConfigProperties.getHostname()).thenReturn("host");
        when(urlConfigProperties.getProtocol()).thenReturn("valid_protocol");

        assertThatThrownBy(() -> testee.assertUrlValid(urlStructure))
                .isInstanceOf(LinkValidationException.class)
                .hasMessage("Provided URL protocol [invalid_protocol] does not match with the required one [valid_protocol]");
    }

    @Test
    void assertUrlValid_WhenProductPageUrlContentIdPathVariableMissing_ThenLinkValidationExceptionThrown() {
        Map<PathVariable, String> pathVariables = new HashMap<>();
        pathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, "productName-p-");
        LinkStructureDto urlStructure = mock(LinkStructureDto.class);

        when(urlStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(urlStructure.getPathVariables()).thenReturn(pathVariables);
        when(urlStructure.getHost()).thenReturn("host");
        when(urlStructure.getScheme()).thenReturn("protocol");
        when(urlConfigProperties.getHostname()).thenReturn("host");
        when(urlConfigProperties.getProtocol()).thenReturn("protocol");

        assertThatThrownBy(() -> testee.assertUrlValid(urlStructure))
                .isInstanceOf(LinkValidationException.class)
                .hasMessage("Product detail page must contain contentId path variable");
    }

}