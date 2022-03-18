package link.converter.service.mapper.strategy.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.service.mapper.strategy.LinkMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.nonNull;
import static link.converter.constants.LinkConstants.PRODUCT_NAME_AND_CONTENT_ID_SEPARATOR;

/**
 * Class implementing URL-to-Deeplink mapping strategy for PRODUCT page type
 */
@Component
public class UrlToDeeplinkProductPageMappingStrategy implements LinkMappingStrategy {

    private static final int CONTENT_ID_INDEX = 1;

    /**
     * Maps given URL structure to the corresponding Deeplink structure. Mapped fields:
     * - pageType
     * - linkType
     * - pathVariables
     * - queryParameters
     *
     * @param url - incoming URL structure mapping source
     * @return Deeplink structure mapping target
     */
    @Override
    public LinkStructureDto map(LinkStructureDto url) {
        Map<QueryParameter, String> deeplinkQueryParameters = getQueryParameters(url);
        return getDeeplinkStructure(url, deeplinkQueryParameters);
    }

    /**
     * @return - source link type
     */
    @Override
    public LinkType getSourceLinkType() {
        return LinkType.URL;
    }

    /**
     * @return - target link type
     */
    @Override
    public LinkType getTargetLinkType() {
        return LinkType.DEEPLINK;
    }

    /**
     * @return - links page type
     */
    @Override
    public PageType getPageType() {
        return PageType.PRODUCT;
    }

    private Map<QueryParameter, String> getQueryParameters(LinkStructureDto url) {
        Map<QueryParameter, String> deeplinkQueryParameters = new TreeMap<>();
        deeplinkQueryParameters.put(QueryParameter.PAGE, url.getPageType().getName());
        Map<QueryParameter, String> optionalQueryParameters =
                getOptionalProductPageQueryParameters(url.getQueryParameters());
        deeplinkQueryParameters.putAll(optionalQueryParameters);
        deeplinkQueryParameters.put(QueryParameter.CONTENT_ID, getContentId(url));
        return deeplinkQueryParameters;
    }

    private Map<QueryParameter, String> getOptionalProductPageQueryParameters(
            Map<QueryParameter, String> queryParameters) {
        Map<QueryParameter, String> deeplinkQueryParameters = new EnumMap<>(QueryParameter.class);
        String boutiqueId = queryParameters.get(QueryParameter.BOUTIQUE_ID);
        if (nonNull(boutiqueId)) {
            deeplinkQueryParameters.put(QueryParameter.BOUTIQUE_ID, boutiqueId);
        }
        String merchantId = queryParameters.get(QueryParameter.MERCHANT_ID);
        if (nonNull(merchantId)) {
            deeplinkQueryParameters.put(QueryParameter.MERCHANT_ID, merchantId);
        }
        return deeplinkQueryParameters;
    }

    private String getContentId(LinkStructureDto url) {
        String productNameAndContentIdString = url.getPathVariables().get(PathVariable.PRODUCT_NAME_AND_CONTENT_ID);
        String[] productNameAndContentId = productNameAndContentIdString.split(PRODUCT_NAME_AND_CONTENT_ID_SEPARATOR);
        return productNameAndContentId[CONTENT_ID_INDEX];
    }

    private LinkStructureDto getDeeplinkStructure(LinkStructureDto url, Map<QueryParameter, String> deeplinkQueryParameters) {
        return LinkStructureDto.builder()
                .setPageType(url.getPageType())
                .setLinkType(LinkType.DEEPLINK)
                .setQueryParameters(deeplinkQueryParameters)
                .build();
    }

}
