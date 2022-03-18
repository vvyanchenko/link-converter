package link.converter.domain.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Enum representing application-specific web page types
 */
@Getter
@AllArgsConstructor
public enum PageType {

    PRODUCT("Product"),
    SEARCH("Search"),
    OTHER("Home");

    private final String name;

    /**
     * Parses PageType from incoming string value
     *
     * @param value - enum string value
     * @return - enum constant
     */
    public static PageType of(String value) {
        return Arrays.stream(values())
                .filter(pageType -> pageType.getName().equals(value))
                .findFirst()
                .orElse(OTHER);
    }

}
