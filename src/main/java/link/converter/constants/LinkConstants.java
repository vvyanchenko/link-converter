package link.converter.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Class providing basic link constants
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LinkConstants {

    public static final String PATH_SEPARATOR = "/";
    public static final String QUERY_PARAMS_SEPARATOR = "&";
    public static final String QUERY_PARAM_NAME_VALUE_SEPARATOR = "=";
    public static final String PRODUCT_NAME_AND_CONTENT_ID_SEPARATOR = "-p-";
    public static final String DEFAULT_URL_BRAND_NAME_OR_CATEGORY = "brand";
    public static final String DEFAULT_URL_PRODUCT_NAME = "name";
    public static final String PRODUCT_NAME_AND_CONTENT_ID_PATTERN = "%s-p-%s";
    public static final int MAX_PATH_VARIABLES_NUMBER = 2;
    public static final int ZERO_ELEMENT_ARRAY_LENGTH = 0;
    public static final int ONE_ELEMENT_ARRAY_LENGTH = 1;
    public static final int TWO_ELEMENT_ARRAY_LENGTH = 2;
    public static final int FIRST_ARRAY_ELEMENT = 0;
    public static final int SECOND_ARRAY_ELEMENT = 1;

}
