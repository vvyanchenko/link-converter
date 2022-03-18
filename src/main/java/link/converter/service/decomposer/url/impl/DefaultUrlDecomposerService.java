package link.converter.service.decomposer.url.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.domain.enumeration.QueryParameter;
import link.converter.service.decomposer.url.UrlDecomposerService;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.nonNull;
import static link.converter.constants.LinkConstants.*;

/**
 * Service required to decompose URL into structural components. e.g. path variables and query parameters
 */
@Service
public class DefaultUrlDecomposerService implements UrlDecomposerService {

    /**
     * Extracts the following structural elements from url:
     * - page type
     * - link type
     * - path variables
     * - query parameters
     *
     * @param url - incoming URL to be decomposed
     * @return - given URL structure
     */
    @Override
    public LinkStructureDto decompose(URL url) {
        Map<PathVariable, String> pathVariables = getPathVariables(url);
        Map<QueryParameter, String> queryParameters = getQueryParameters(url.getQuery());
        PageType pageType = getUrlPageType(pathVariables);
        return getUrlStructure(url, pageType, pathVariables, queryParameters);
    }

    private Map<PathVariable, String> getPathVariables(URL url) {
        String path = url.getPath();
        String pathTrimmed = trimLeadingPathSeparator(path);
        String[] pathVariablesRaw = pathTrimmed.split(PATH_SEPARATOR);
        if (pathVariablesRaw.length == ZERO_ELEMENT_ARRAY_LENGTH) {
            return Collections.emptyMap();
        }
        if (pathVariablesRaw.length == ONE_ELEMENT_ARRAY_LENGTH) {
            return pathVariablesRaw[FIRST_ARRAY_ELEMENT].contains(PathVariable.SEARCH.getValue())
                    ? Map.of(PathVariable.SEARCH, pathVariablesRaw[FIRST_ARRAY_ELEMENT]) : Collections.emptyMap();
        }
        if (pathVariablesRaw.length == TWO_ELEMENT_ARRAY_LENGTH
                && !pathVariablesRaw[SECOND_ARRAY_ELEMENT].contains(PRODUCT_NAME_AND_CONTENT_ID_SEPARATOR)) {
            return Collections.emptyMap();
        }
        if (pathVariablesRaw.length > MAX_PATH_VARIABLES_NUMBER) {
            return Collections.emptyMap();
        }
        Map<PathVariable, String> pathVariables = new TreeMap<>();
        pathVariables.put(PathVariable.BRAND_OR_CATEGORY_NAME, pathVariablesRaw[FIRST_ARRAY_ELEMENT]);
        pathVariables.put(PathVariable.PRODUCT_NAME_AND_CONTENT_ID, pathVariablesRaw[SECOND_ARRAY_ELEMENT]);
        return pathVariables;
    }

    private String trimLeadingPathSeparator(String path) {
        return path.startsWith(PATH_SEPARATOR) && path.length() > ONE_ELEMENT_ARRAY_LENGTH
                ? path.substring(SECOND_ARRAY_ELEMENT) : path;
    }

    private PageType getUrlPageType(Map<PathVariable, String> pathVariables) {
        String searchPathVariable = pathVariables.get(PathVariable.SEARCH);
        if (nonNull(searchPathVariable)) {
            return PageType.SEARCH;
        }
        String brandOrCategoryNamePathVariable = pathVariables.get(PathVariable.BRAND_OR_CATEGORY_NAME);
        if (nonNull(brandOrCategoryNamePathVariable)) {
            return PageType.PRODUCT;
        }
        return PageType.OTHER;
    }

    private LinkStructureDto getUrlStructure(URL url,
                                             PageType pageType,
                                             Map<PathVariable, String> pathVariables,
                                             Map<QueryParameter, String> queryParameters) {
        return LinkStructureDto.builder()
                .setLinkType(LinkType.URL)
                .setPageType(pageType)
                .setScheme(url.getProtocol())
                .setHost(url.getHost())
                .setPathVariables(pathVariables)
                .setQueryParameters(queryParameters)
                .build();
    }

}
