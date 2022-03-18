package link.converter.service.decomposer;

import link.converter.domain.enumeration.QueryParameter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static link.converter.constants.LinkConstants.*;

/**
 * Service required to decompose link into structural components
 */
public interface LinkDecomposerService {

    /**
     * Parses query parameters from a string
     *
     * @param queryParametersString - incoming string to be parsed
     * @return - map of query parameters
     */
    default Map<QueryParameter, String> getQueryParameters(String queryParametersString) {
        String[] queryParametersRaw = Optional.ofNullable(queryParametersString)
                .map(query -> query.split(QUERY_PARAMS_SEPARATOR))
                .orElse(new String[ZERO_ELEMENT_ARRAY_LENGTH]);
        if (queryParametersRaw.length == ZERO_ELEMENT_ARRAY_LENGTH) {
            return Collections.emptyMap();
        }
        return Arrays.stream(queryParametersRaw)
                .flatMap(queryParameterRaw -> parseQueryParameter(queryParameterRaw).stream())
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (x, y) -> y, TreeMap::new));
    }

    private Optional<Pair<QueryParameter, String>> parseQueryParameter(String queryParameterRaw) {
        String[] queryParameterNameAndValue = queryParameterRaw.split(QUERY_PARAM_NAME_VALUE_SEPARATOR);
        if (queryParameterNameAndValue.length != TWO_ELEMENT_ARRAY_LENGTH) {
            return Optional.empty();
        }
        QueryParameter queryParameterName = QueryParameter.of(queryParameterNameAndValue[FIRST_ARRAY_ELEMENT]);
        Pair<QueryParameter, String> queryParameter =
                Pair.of(queryParameterName, queryParameterNameAndValue[SECOND_ARRAY_ELEMENT]);
        return Optional.of(queryParameter);
    }

}
