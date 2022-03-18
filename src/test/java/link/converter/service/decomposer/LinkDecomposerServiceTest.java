package link.converter.service.decomposer;

import link.converter.domain.enumeration.QueryParameter;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LinkDecomposerServiceTest {

    private final LinkDecomposerService testee = new LinkDecomposerServiceUnderTest();

    @Test
    void getQueryParameters_WhenValidQueryParamsStringPassed_ThenQueryParamsMapReturned() {
        String queryParamsString = "ContentId=Content&q=query&Page=page";
        Map<QueryParameter, String> expectedQueryParameters = new LinkedHashMap<>();
        expectedQueryParameters.put(QueryParameter.PAGE, "page");
        expectedQueryParameters.put(QueryParameter.QUERY, "query");
        expectedQueryParameters.put(QueryParameter.CONTENT_ID, "Content");

        Map<QueryParameter, String> actualQueryParameters = testee.getQueryParameters(queryParamsString);

        assertThat(actualQueryParameters)
                .containsExactlyEntriesOf(expectedQueryParameters);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void getQueryParameters_WhenEmptyQueryParamsStringPassed_ThenEmptyQueryParamsMapReturned(String queryParams) {
        Map<QueryParameter, String> actualQueryParameters = testee.getQueryParameters(queryParams);

        assertThat(actualQueryParameters).isEmpty();
    }

    @Test
    void getQueryParameters_WhenInvalidQueryParamFormatProvided_ThenEmptyQueryParamsMapReturned() {
        String queryParamsString = "ContentId=Content=q=query";

        Map<QueryParameter, String> actualQueryParameters = testee.getQueryParameters(queryParamsString);

        assertThat(actualQueryParameters).isEmpty();
    }

    @Test
    void getQueryParameters_WhenInvalidQueryParameterPassed_ThenRuntimeExceptionThrown() {
        String queryParamsString = "Content=ContentId";

        assertThatThrownBy(() -> testee.getQueryParameters(queryParamsString))
                .isInstanceOf(RuntimeException.class);
    }

    private static class LinkDecomposerServiceUnderTest implements LinkDecomposerService {

        @Override
        public Map<QueryParameter, String> getQueryParameters(String queryParametersString) {
            return LinkDecomposerService.super.getQueryParameters(queryParametersString);
        }

    }

}