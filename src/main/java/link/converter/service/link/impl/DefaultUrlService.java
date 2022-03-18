package link.converter.service.link.impl;

import link.converter.config.DeeplinkConfigProperties;
import link.converter.config.UrlConfigProperties;
import link.converter.controller.dto.DeeplinkResponseDto;
import link.converter.domain.enumeration.PageType;
import link.converter.service.dto.LinkStructureDto;
import link.converter.controller.dto.UrlRequestDto;
import link.converter.domain.entity.UrlToDeeplink;
import link.converter.domain.enumeration.LinkType;
import link.converter.repository.LinkRepository;
import link.converter.service.decomposer.url.UrlDecomposerService;
import link.converter.service.formatter.deeplink.DeeplinkFormatterService;
import link.converter.service.link.UrlService;
import link.converter.service.mapper.LinkMapperService;
import link.converter.service.validation.url.UrlValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;

import static java.util.Objects.nonNull;

/**
 * Service required to convert URL to other link types
 * Caches link pairs in the cache
 */
@Slf4j
@Service
@AllArgsConstructor
public class DefaultUrlService implements UrlService {

    private final LinkRepository linkRepository;
    private final DeeplinkFormatterService deeplinkFormatterService;
    private final UrlDecomposerService urlDecomposerService;
    private final UrlValidationService urlValidationService;
    private final LinkMapperService linkMapperService;
    private final UrlConfigProperties urlConfigProperties;
    private final DeeplinkConfigProperties deeplinkConfigProperties;

    /**
     * Converts URL to deeplink
     *
     * @param urlDto - incoming URL to be converted to deeplink
     * @return - Deeplink response
     */
    @Override
    @Transactional
    @Cacheable(value = "${trendyol.cache.url-to-deeplink}", key = "#urlDto.url")
    public DeeplinkResponseDto convertToDeeplink(UrlRequestDto urlDto) {
        LOGGER.debug("Received url=[{}] to be converted to deeplink", urlDto.getUrl());
        URL url = urlDto.getUrl();
        if (isHomePage(url)) {
            return new DeeplinkResponseDto(deeplinkConfigProperties.getHomepage());
        }
        UrlToDeeplink urlToDeeplink = linkRepository.findByUrl(url.toString());
        if (nonNull(urlToDeeplink)) {
            return new DeeplinkResponseDto(urlToDeeplink.getDeeplink());
        }
        return convertToDeeplink(url);
    }

    private DeeplinkResponseDto convertToDeeplink(URL url) {
        LinkStructureDto urlStructure = urlDecomposerService.decompose(url);
        urlValidationService.assertUrlValid(urlStructure);
        LinkStructureDto deeplinkStructure = linkMapperService.map(urlStructure, LinkType.DEEPLINK);
        String deeplinkString = deeplinkFormatterService.format(deeplinkStructure);
        saveLink(url.toString(), deeplinkString);
        return new DeeplinkResponseDto(deeplinkString);
    }

    private boolean isHomePage(URL url) {
        return urlConfigProperties.getHomepage().equals(url.toString());
    }

    private void saveLink(String url, String deeplink) {
        UrlToDeeplink urlToDeeplink = UrlToDeeplink.builder()
                .setDeeplink(deeplink)
                .setUrl(url)
                .build();
        linkRepository.save(urlToDeeplink);
        LOGGER.info("Successfully converted url=[{}] to deeplink=[{}]", url, deeplink);
    }

}
