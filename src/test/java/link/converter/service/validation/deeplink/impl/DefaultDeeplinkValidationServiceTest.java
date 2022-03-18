package link.converter.service.validation.deeplink.impl;

import link.converter.config.DeeplinkConfigProperties;
import link.converter.domain.enumeration.PageType;
import link.converter.exception.LinkValidationException;
import link.converter.service.dto.LinkStructureDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultDeeplinkValidationServiceTest {

    @Mock
    private DeeplinkConfigProperties deeplinkConfigProperties;

    @InjectMocks
    private DefaultDeeplinkValidationService testee;

    @Test
    void assertDeeplinkValid_WhenValidDeeplinkProvided_ThenMethodExecutedSuccessfully() {
        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);

        when(deeplinkStructure.getScheme()).thenReturn("scheme");
        when(deeplinkConfigProperties.getScheme()).thenReturn("scheme");

        assertThatNoException()
                .isThrownBy(() -> testee.assertDeeplinkValid(deeplinkStructure));
    }

    @Test
    void assertDeeplinkValid_WhenDeeplinkWithInvalidHostnameProvided_ThenLinkValidationExceptionThrown() {
        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);

        when(deeplinkStructure.getScheme()).thenReturn("invalid_scheme");
        when(deeplinkConfigProperties.getScheme()).thenReturn("valid_scheme");

        assertThatThrownBy(() -> testee.assertDeeplinkValid(deeplinkStructure))
                .isInstanceOf(LinkValidationException.class)
                .hasMessage("Provided deeplink scheme [invalid_scheme] does not match with the required one [valid_scheme]");
    }

    @Test
    void assertDeeplinkValid_WhenPageTypeProductAndNoContentIdQueryParamPresent_ThenLinkValidationExceptionThrown() {
        LinkStructureDto deeplinkStructure = mock(LinkStructureDto.class);

        when(deeplinkStructure.getScheme()).thenReturn("scheme");
        when(deeplinkStructure.getPageType()).thenReturn(PageType.PRODUCT);
        when(deeplinkConfigProperties.getScheme()).thenReturn("scheme");

        assertThatThrownBy(() -> testee.assertDeeplinkValid(deeplinkStructure))
                .isInstanceOf(LinkValidationException.class)
                .hasMessage("Product detail page must contain ContentId query string");
    }

}