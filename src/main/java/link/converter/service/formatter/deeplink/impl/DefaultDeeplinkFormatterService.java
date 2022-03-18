package link.converter.service.formatter.deeplink.impl;

import link.converter.config.DeeplinkConfigProperties;
import link.converter.exception.MalformedLinkException;
import link.converter.service.dto.LinkStructureDto;
import link.converter.service.formatter.deeplink.DeeplinkFormatterService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static link.converter.constants.LinkConstants.QUERY_PARAMS_SEPARATOR;
import static link.converter.constants.LinkConstants.QUERY_PARAM_NAME_VALUE_SEPARATOR;

/**
 * Service required to format given Deeplink structure to a corresponding string
 */
@Service
@AllArgsConstructor
public class DefaultDeeplinkFormatterService implements DeeplinkFormatterService {

    private static final String INVALID_DEEPLINK_SYNTAX_ERROR_MESSAGE =
            "Can't format deeplink [%s] - invalid URI syntax";

    private final DeeplinkConfigProperties deeplinkConfigProperties;

    /**
     * Formats given Deeplink structure to the corresponding string
     *
     * @param deeplink - Deeplink structure to be formatted
     * @return - Deeplink string
     */
    @Override
    public String format(LinkStructureDto deeplink) {
        String queryString = getQueryString(deeplink);
        return getEncodedDeeplink(queryString);
    }

    private String getQueryString(LinkStructureDto deeplink) {
        String queryString = deeplink.getQueryParameters().entrySet().stream()
                .map(nameToValue -> composeQueryParameterString(
                        nameToValue.getKey().getDeeplinkName(), nameToValue.getValue()))
                .collect(Collectors.joining(QUERY_PARAMS_SEPARATOR));
        return URLDecoder.decode(queryString, StandardCharsets.UTF_8);
    }

    private String composeQueryParameterString(String paramName, String paramValue) {
        return String.join(QUERY_PARAM_NAME_VALUE_SEPARATOR, paramName, paramValue);
    }

    private String getEncodedDeeplink(String queryString) {
        URIBuilder builder = new URIBuilder()
                .setScheme(deeplinkConfigProperties.getScheme())
                .setHost(StringUtils.EMPTY)
                .setCustomQuery(queryString);
        try {
            return builder.build().toASCIIString();
        } catch (URISyntaxException ex) {
            throw new MalformedLinkException(
                    String.format(INVALID_DEEPLINK_SYNTAX_ERROR_MESSAGE, builder), ex);
        }
    }

}
