package link.converter.service.link.impl;

import link.converter.config.DeeplinkConfigProperties;
import link.converter.config.UrlConfigProperties;
import link.converter.controller.dto.DeeplinkRequestDto;
import link.converter.controller.dto.DeeplinkResponseDto;
import link.converter.service.dto.LinkStructureDto;
import link.converter.controller.dto.UrlResponseDto;
import link.converter.domain.entity.UrlToDeeplink;
import link.converter.domain.enumeration.LinkType;
import link.converter.repository.LinkRepository;
import link.converter.service.decomposer.deeplink.DeeplinkDecomposerService;
import link.converter.service.formatter.url.UrlFormatterService;
import link.converter.service.link.DeeplinkService;
import link.converter.service.mapper.LinkMapperService;
import link.converter.service.validation.deeplink.DeeplinkValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URL;

import static java.util.Objects.nonNull;

/**
 * Service required to convert deeplink to other link types
 * Caches link pairs in the cache
 */
@Slf4j
@Service
@AllArgsConstructor
public class DefaultDeeplinkService implements DeeplinkService {

    private final LinkRepository linkRepository;
    private final UrlFormatterService urlFormatterService;
    private final DeeplinkDecomposerService deeplinkDecomposerService;
    private final DeeplinkValidationService deeplinkValidationService;
    private final LinkMapperService linkMapperService;
    private final UrlConfigProperties urlConfigProperties;
    private final DeeplinkConfigProperties deeplinkConfigProperties;

    /**
     * Converts Deeplink to URL
     *
     * @param deeplinkDto - incoming Deeplink to be converted to url
     * @return - URL response
     */
    @Override
    @Transactional
    @Cacheable(value = "${trendyol.cache.deeplink-to-url}", key = "#deeplinkDto.deeplink")
    public UrlResponseDto convertToUrl(DeeplinkRequestDto deeplinkDto) {
        LOGGER.debug("Received deeplink=[{}] to be converted to url", deeplinkDto.getDeeplink());
        URI deeplink = deeplinkDto.getDeeplink();
        if (isHomePage(deeplink)) {
            return new UrlResponseDto(urlConfigProperties.getHomepage());
        }
        UrlToDeeplink urlToDeeplink = linkRepository.findByDeeplink(deeplink.toString());
        if (nonNull(urlToDeeplink)) {
            return new UrlResponseDto(urlToDeeplink.getUrl());
        }
        return convertToUrl(deeplink);
    }

    private UrlResponseDto convertToUrl(URI deeplink) {
        LinkStructureDto deeplinkStructure = deeplinkDecomposerService.decompose(deeplink);
        deeplinkValidationService.assertDeeplinkValid(deeplinkStructure);
        LinkStructureDto urlStructure = linkMapperService.map(deeplinkStructure, LinkType.URL);
        String urlString = urlFormatterService.format(urlStructure);
        saveLink(deeplink.toString(), urlString);
        return new UrlResponseDto(urlString);
    }

    private boolean isHomePage(URI deeplink) {
        return deeplinkConfigProperties.getHomepage().equals(deeplink.toString());
    }

    private void saveLink(String deeplink, String url) {
        UrlToDeeplink urlToDeeplink = UrlToDeeplink.builder()
                .setDeeplink(deeplink)
                .setUrl(url)
                .build();
        linkRepository.save(urlToDeeplink);
        LOGGER.info("Successfully converted deeplink=[{}] to url=[{}]", deeplink, url);
    }

}
