package link.converter.service.link.impl;

import link.converter.config.DeeplinkConfigProperties;
import link.converter.config.UrlConfigProperties;
import link.converter.controller.dto.DeeplinkRequestDto;
import link.converter.controller.dto.UrlResponseDto;
import link.converter.domain.entity.UrlToDeeplink;
import link.converter.domain.enumeration.LinkType;
import link.converter.repository.LinkRepository;
import link.converter.service.decomposer.deeplink.DeeplinkDecomposerService;
import link.converter.service.dto.LinkStructureDto;
import link.converter.service.formatter.url.UrlFormatterService;
import link.converter.service.mapper.LinkMapperService;
import link.converter.service.validation.deeplink.DeeplinkValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DefaultDeeplinkServiceTest {

    @Mock
    private LinkRepository linkRepository;
    @Mock
    private UrlFormatterService urlFormatterService;
    @Mock
    private DeeplinkDecomposerService deeplinkDecomposerService;
    @Mock
    private DeeplinkValidationService deeplinkValidationService;
    @Mock
    private LinkMapperService linkMapperService;
    @Mock
    private UrlConfigProperties urlConfigProperties;
    @Mock
    private DeeplinkConfigProperties deeplinkConfigProperties;

    @Captor
    private ArgumentCaptor<UrlToDeeplink> captor;

    @InjectMocks
    private DefaultDeeplinkService testee;

    @Test
    void convertToUrl_WhenNoDeeplinkFoundInDb_ThenDeeplinkConvertedToUrlAndSavedToDb() throws Exception {
        String urlString = "https://host:8888/path";
        String deeplinkString = "ty://?path";
        URI deeplink = new URI(deeplinkString);
        DeeplinkRequestDto deeplinkDto = mock(DeeplinkRequestDto.class);
        when(deeplinkDto.getDeeplink()).thenReturn(deeplink);
        LinkStructureDto urlStructureDto = mock(LinkStructureDto.class);
        LinkStructureDto deeplinkStructureDto = mock(LinkStructureDto.class);

        when(deeplinkConfigProperties.getHomepage()).thenReturn("ty://home");
        when(linkRepository.findByDeeplink(deeplinkString)).thenReturn(null);
        when(deeplinkDecomposerService.decompose(deeplink)).thenReturn(deeplinkStructureDto);
        when(linkMapperService.map(deeplinkStructureDto, LinkType.URL)).thenReturn(urlStructureDto);
        when(urlFormatterService.format(urlStructureDto)).thenReturn(urlString);
        when(linkRepository.save(captor.capture())).thenReturn(null);

        UrlResponseDto urlResponseDto = testee.convertToUrl(deeplinkDto);

        assertThat(urlResponseDto.getUrl()).isEqualTo(urlString);

        UrlToDeeplink actualUrlToDeeplink = captor.getValue();
        assertThat(actualUrlToDeeplink.getDeeplink()).isEqualTo(deeplinkString);
        assertThat(actualUrlToDeeplink.getUrl()).isEqualTo(urlString);

        verify(deeplinkValidationService, only()).assertDeeplinkValid(deeplinkStructureDto);
    }

    @Test
    void convertToUrl_WhenDeeplinkFoundInDb_ThenDeeplinkFromDbReturned() throws Exception {
        String urlString = "https://host:8888/path";
        String deeplinkString = "https://host:8888/path";
        UrlToDeeplink expectedUrlToDeeplink = UrlToDeeplink.builder()
                .setUrl(urlString)
                .setDeeplink(deeplinkString)
                .build();
        URI deeplink = new URI(deeplinkString);
        DeeplinkRequestDto deeplinkDto = new DeeplinkRequestDto(deeplink);

        when(deeplinkConfigProperties.getHomepage()).thenReturn("ty://home");
        when(linkRepository.findByDeeplink(deeplink.toString())).thenReturn(expectedUrlToDeeplink);

        UrlResponseDto urlResponseDto = testee.convertToUrl(deeplinkDto);

        assertThat(urlResponseDto.getUrl()).isEqualTo(urlString);

        verifyNoInteractions(deeplinkDecomposerService, deeplinkValidationService, linkMapperService, urlFormatterService);
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    void convertToDeeplink_WhenUrlInvalid_ThenNoUrlSaved() throws Exception {
        String deeplinkString = "ty://?path";
        URI deeplink = new URI(deeplinkString);
        DeeplinkRequestDto deeplinkDto = mock(DeeplinkRequestDto.class);
        when(deeplinkDto.getDeeplink()).thenReturn(deeplink);
        LinkStructureDto deeplinkStructureDto = mock(LinkStructureDto.class);

        when(deeplinkConfigProperties.getHomepage()).thenReturn("ty://home");
        when(linkRepository.findByDeeplink(deeplinkString)).thenReturn(null);
        when(deeplinkDecomposerService.decompose(deeplink)).thenReturn(deeplinkStructureDto);
        doThrow(new RuntimeException()).when(deeplinkValidationService).assertDeeplinkValid(deeplinkStructureDto);

        assertThatThrownBy(() -> testee.convertToUrl(deeplinkDto))
                .isInstanceOf(RuntimeException.class);

        verifyNoInteractions(linkMapperService, urlFormatterService);
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    void convertToUrl_WhenDeeplinkIsHomePage_ThenUrlHomepageFromPropertiesReturned() throws Exception {
        String urlString = "https://home";
        String deeplinkString = "ty://home";
        URI deeplink = new URI(deeplinkString);
        DeeplinkRequestDto deeplinkDto = mock(DeeplinkRequestDto.class);
        when(deeplinkDto.getDeeplink()).thenReturn(deeplink);

        when(deeplinkConfigProperties.getHomepage()).thenReturn(deeplinkString);
        when(urlConfigProperties.getHomepage()).thenReturn(urlString);

        UrlResponseDto urlResponseDto = testee.convertToUrl(deeplinkDto);

        assertThat(urlResponseDto.getUrl()).isEqualTo(urlString);

        verifyNoInteractions(linkRepository, deeplinkDecomposerService, deeplinkValidationService, linkMapperService, urlFormatterService);
    }

}