package link.converter.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing link path variables
 * Ordering of enum constants is important and used in business logic
 */
@Getter
@AllArgsConstructor
public enum PathVariable {

    SEARCH("sr"),
    BRAND_OR_CATEGORY_NAME(null),
    PRODUCT_NAME_AND_CONTENT_ID(null);

    private final String value;

}
