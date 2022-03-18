package link.converter.service.formatter.url.impl;

import link.converter.config.UrlConfigProperties;
import link.converter.exception.MalformedLinkException;
import link.converter.service.dto.LinkStructureDto;
import link.converter.service.formatter.url.UrlFormatterService;
import lombok.AllArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service required to format given url structure to a corresponding string
 */
@Service
@AllArgsConstructor
public class DefaultUrlFormatterService implements UrlFormatterService {

    private static final String INVALID_URL_SYNTAX_ERROR_MESSAGE = "Can't format URL [%s] - invalid URL syntax";

    private final UrlConfigProperties urlConfigProperties;

    /**
     * Formats given URL structure to the corresponding string
     *
     * @param url - URL structure to be formatted
     * @return - URL string
     */
    @Override
    public String format(LinkStructureDto url) {
        List<String> pathVariables = getPathVariables(url);
        List<NameValuePair> queryParams = getQueryParams(url);
        return getEncodedUrl(pathVariables, queryParams);
    }

    private List<String> getPathVariables(LinkStructureDto url) {
        return new ArrayList<>(url.getPathVariables().values());
    }

    private List<NameValuePair> getQueryParams(LinkStructureDto url) {
        return url.getQueryParameters().entrySet().stream()
                .map(paramNameToValue -> new BasicNameValuePair(
                        paramNameToValue.getKey().getUrlName(), paramNameToValue.getValue()))
                .collect(Collectors.toList());
    }

    private String getEncodedUrl(List<String> pathVariables,
                                 List<NameValuePair> queryParams) {
        URIBuilder builder = new URIBuilder()
                .setScheme(urlConfigProperties.getProtocol())
                .setHost(urlConfigProperties.getHostname())
                .setPathSegments(pathVariables)
                .setParameters(queryParams);
        try {
            return builder.build().toASCIIString();
        } catch (URISyntaxException ex) {
            throw new MalformedLinkException(
                    String.format(INVALID_URL_SYNTAX_ERROR_MESSAGE, builder), ex);
        }
    }

}
