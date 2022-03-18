package link.converter.service.mapper.strategy.impl;

import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.service.dto.LinkStructureDto;
import link.converter.service.mapper.strategy.LinkMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.nonNull;
import static link.converter.constants.LinkConstants.*;

/**
 * Class implementing Deeplink-to-URL mapping strategy for PRODUCT page type
 */
@Component
public class DeeplinkToUrlProductPageMappingStrategy implements LinkMappingStrategy {

    /**
     * Maps given Deeplink structure to the corresponding URL structure. Mapped fields:
     * - pageType
     * - linkType
     * - pathVariables
     * - queryParameters
     *
     * @param deeplink - incoming Deeplink structure mapping source
     * @return URL structure mapping target
     */
    @Override
    public LinkStructureDto map(LinkStructureDto deeplink) {
        Map<PathVariable, String> pathVariables = mapPathVariables(deeplink);
        Map<QueryParameter, String> queryParameters = mapQueryParameters(deeplink);
        return getUrlStructure(deeplink, pathVariables, queryParameters);
    }

    /**
     * @return - source link type
     */
    @Override
    public LinkType getSourceLinkType() {
        return LinkType.DEEPLINK;
    }

    /**
     * @return - target link type
     */
    @Override
    public LinkType getTargetLinkType() {
        return LinkType.URL;
    }

    /**
     * @return - links page type
     */
    @Override
    public PageType getPageType() {
        return PageType.PRODUCT;
    }

    private Map<PathVariable, String> mapPathVariables(LinkStructureDto deeplink) {
        Map<PathVariable, String> urlPathVariables = new TreeMap<>();
        urlPathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, DEFAULT_URL_BRAND_NAME_OR_CATEGORY);
        String contentId = deeplink.getQueryParameters().get(QueryParameter.CONTENT_ID);
        String productNameAndContentId =
                String.format(PRODUCT_NAME_AND_CONTENT_ID_PATTERN, DEFAULT_URL_PRODUCT_NAME, contentId);
        urlPathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, productNameAndContentId);
        return urlPathVariables;
    }

    private Map<QueryParameter, String> mapQueryParameters(LinkStructureDto deeplink) {
        Map<QueryParameter, String> queryParameters = deeplink.getQueryParameters();
        Map<QueryParameter, String> deeplinkQueryParameters = new TreeMap<>();
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

    private LinkStructureDto getUrlStructure(LinkStructureDto deeplink,
                                             Map<PathVariable, String> pathVariables,
                                             Map<QueryParameter, String> queryParameters) {
        return LinkStructureDto.builder()
                .setPageType(deeplink.getPageType())
                .setLinkType(LinkType.URL)
                .setPathVariables(pathVariables)
                .setQueryParameters(queryParameters)
                .build();
    }

}
