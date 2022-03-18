package link.converter.service.link.impl;

import link.converter.config.DeeplinkConfigProperties;
import link.converter.config.UrlConfigProperties;
import link.converter.controller.dto.DeeplinkRequestDto;
import link.converter.controller.dto.DeeplinkResponseDto;
import link.converter.controller.dto.UrlRequestDto;
import link.converter.controller.dto.UrlResponseDto;
import link.converter.domain.entity.UrlToDeeplink;
import link.converter.domain.enumeration.LinkType;
import link.converter.repository.LinkRepository;
import link.converter.service.decomposer.url.UrlDecomposerService;
import link.converter.service.dto.LinkStructureDto;
import link.converter.service.formatter.deeplink.DeeplinkFormatterService;
import link.converter.service.mapper.LinkMapperService;
import link.converter.service.validation.url.UrlValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUrlServiceTest {

    @Mock
    private LinkRepository linkRepository;
    @Mock
    private DeeplinkFormatterService deeplinkFormatterService;
    @Mock
    private UrlDecomposerService urlDecomposerService;
    @Mock
    private UrlValidationService urlValidationService;
    @Mock
    private LinkMapperService linkMapperService;
    @Mock
    private UrlConfigProperties urlConfigProperties;
    @Mock
    private DeeplinkConfigProperties deeplinkConfigProperties;

    @Captor
    private ArgumentCaptor<UrlToDeeplink> captor;

    @InjectMocks
    private DefaultUrlService testee;

    @Test
    void convertToDeeplink_WhenNoUrlFoundInDb_ThenUrlConvertedToDeeplinkAndSavedToDb() throws Exception {
        String urlString = "https://host:8888/path";
        URL url = new URL(urlString);
        UrlRequestDto urlDto = mock(UrlRequestDto.class);
        when(urlDto.getUrl()).thenReturn(url);
        LinkStructureDto urlStructureDto = mock(LinkStructureDto.class);
        LinkStructureDto deeplinkStructureDto = mock(LinkStructureDto.class);
        String deeplinkString = "ty://?path";

        when(urlConfigProperties.getHomepage()).thenReturn("https://home");
        when(linkRepository.findByUrl(urlString)).thenReturn(null);
        when(urlDecomposerService.decompose(url)).thenReturn(urlStructureDto);
        when(linkMapperService.map(urlStructureDto, LinkType.DEEPLINK)).thenReturn(deeplinkStructureDto);
        when(deeplinkFormatterService.format(deeplinkStructureDto)).thenReturn(deeplinkString);
        when(linkRepository.save(captor.capture())).thenReturn(null);

        DeeplinkResponseDto deeplinkResponseDto = testee.convertToDeeplink(urlDto);

        assertThat(deeplinkResponseDto.getDeeplink()).isEqualTo(deeplinkString);

        UrlToDeeplink actualUrlToDeeplink = captor.getValue();
        assertThat(actualUrlToDeeplink.getDeeplink()).isEqualTo(deeplinkString);
        assertThat(actualUrlToDeeplink.getUrl()).isEqualTo(urlString);

        verify(urlValidationService, only()).assertUrlValid(urlStructureDto);
    }

    @Test
    void convertToDeeplink_WhenUrlFoundInDb_ThenDeeplinkFromDbReturned() throws Exception {
        String urlString = "https://host:8888/path";
        String deeplinkString = "ty://?path";
        UrlToDeeplink expectedUrlToDeeplink = UrlToDeeplink.builder()
                .setUrl(urlString)
                .setDeeplink(deeplinkString)
                .build();
        URL url = new URL(urlString);
        UrlRequestDto urlDto = mock(UrlRequestDto.class);
        when(urlDto.getUrl()).thenReturn(url);

        when(urlConfigProperties.getHomepage()).thenReturn("https://home");
        when(linkRepository.findByUrl(url.toString())).thenReturn(expectedUrlToDeeplink);

        DeeplinkResponseDto deeplinkResponseDto = testee.convertToDeeplink(urlDto);

        assertThat(deeplinkResponseDto.getDeeplink()).isEqualTo(deeplinkString);

        verifyNoInteractions(urlDecomposerService, urlValidationService, linkMapperService, deeplinkFormatterService);
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    void convertToDeeplink_WhenUrlInvalid_ThenNoUrlSaved() throws Exception {
        String urlString = "https://host:8888/path";
        URL url = new URL(urlString);
        UrlRequestDto urlDto = mock(UrlRequestDto.class);
        when(urlDto.getUrl()).thenReturn(url);
        LinkStructureDto urlStructureDto = mock(LinkStructureDto.class);

        when(urlConfigProperties.getHomepage()).thenReturn("https://home");
        when(linkRepository.findByUrl(urlString)).thenReturn(null);
        when(urlDecomposerService.decompose(url)).thenReturn(urlStructureDto);
        doThrow(new RuntimeException()).when(urlValidationService).assertUrlValid(urlStructureDto);

        assertThatThrownBy(() -> testee.convertToDeeplink(urlDto))
                .isInstanceOf(RuntimeException.class);

        verifyNoInteractions(linkMapperService, deeplinkFormatterService);
        verifyNoMoreInteractions(linkRepository);
    }

    @Test
    void convertToDeeplink_WhenUrlIsHomePage_ThenDeeplinkHomepageFromPropertiesReturned() throws Exception {
        String urlString = "https://home";
        String deeplinkString = "ty://home";
        URL url = new URL(urlString);
        UrlRequestDto urlDto = mock(UrlRequestDto.class);
        when(urlDto.getUrl()).thenReturn(url);

        when(urlConfigProperties.getHomepage()).thenReturn(urlString);
        when(deeplinkConfigProperties.getHomepage()).thenReturn(deeplinkString);

        DeeplinkResponseDto deeplinkResponseDto = testee.convertToDeeplink(urlDto);

        assertThat(deeplinkResponseDto.getDeeplink()).isEqualTo(deeplinkString);

        verifyNoInteractions(linkRepository, urlDecomposerService, urlValidationService, linkMapperService, deeplinkFormatterService);
    }

}