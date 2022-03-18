package link.converter.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static java.util.Objects.nonNull;

/**
 * Enum representing link query parameters
 * Ordering of enum constants is important and used in business logic
 */
@Getter
@AllArgsConstructor
public enum QueryParameter {
    
    PAGE(null, "Page"),
    QUERY("q", "Query"),
    CONTENT_ID(null, "ContentId"),
    BOUTIQUE_ID("boutiqueId", "CampaignId"),
    MERCHANT_ID("merchantId", "MerchantId");

    private static final String UNEXPECTED_QUERY_PARAMETER_ERROR_MESSAGE = "Unexpected query parameter was passed [%s]";

    private final String urlName;
    private final String deeplinkName;

    /**
     * Parses QueryParameter from incoming string value
     *
     * @param value - enum string value
     * @return - enum constant
     */
    public static QueryParameter of(String value) {
        return Arrays.stream(values())
                .filter(queryParam -> isQueryEqual(queryParam, value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        String.format(UNEXPECTED_QUERY_PARAMETER_ERROR_MESSAGE, value)));
    }

    private static boolean isQueryEqual(QueryParameter parameter, String parameterString) {
        return nonNull(parameter.getUrlName()) && parameter.getUrlName().equals(parameterString)
                || nonNull(parameter.getDeeplinkName()) && parameter.getDeeplinkName().equals(parameterString);
    }

}
