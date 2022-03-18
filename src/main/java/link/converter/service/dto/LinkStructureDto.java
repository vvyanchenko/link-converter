package link.converter.service.dto;

import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.PathVariable;
import link.converter.domain.enumeration.QueryParameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

/**
 * Dto representing basic structural elements of any link
 */
@Getter
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class LinkStructureDto {

    private final PageType pageType;
    private final LinkType linkType;
    private final String scheme;
    private final String host;
    @Builder.Default
    private final Map<PathVariable, String> pathVariables = new EnumMap<>(PathVariable.class);
    @Builder.Default
    private final Map<QueryParameter, String> queryParameters = new EnumMap<>(QueryParameter.class);

}
