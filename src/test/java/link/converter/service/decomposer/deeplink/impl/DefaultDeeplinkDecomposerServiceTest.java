package link.converter.service.decomposer.deeplink.impl;

import link.converter.service.dto.LinkStructureDto;
import link.converter.domain.enumeration.LinkType;
import link.converter.domain.enumeration.PageType;
import link.converter.domain.enumeration.QueryParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultDeeplinkDecomposerServiceTest {

    @InjectMocks
    private DefaultDeeplinkDecomposerService testee;

    @Test
    void decompose_WhenValidDeeplinkUriPassed_ThenDecomposedDeeplinkReturned() throws Exception {
        URI deeplink = new URI("scheme://?Page=Product&ContentId=cId");
        Map<QueryParameter, String> expectedQueryParams = new LinkedHashMap<>();
        expectedQueryParams.put(QueryParameter.PAGE, "Product");
        expectedQueryParams.put(QueryParameter.CONTENT_ID, "cId");

        LinkStructureDto deeplinkStructure = testee.decompose(deeplink);

        assertThat(deeplinkStructure.getLinkType()).isEqualTo(LinkType.DEEPLINK);
        assertThat(deeplinkStructure.getPageType()).isEqualTo(PageType.PRODUCT);
        assertThat(deeplinkStructure.getScheme()).isEqualTo("scheme");
        assertThat(deeplinkStructure.getHost()).isNull();
        assertThat(deeplinkStructure.getPathVariables()).isEmpty();
        assertThat(deeplinkStructure.getQueryParameters()).containsExactlyEntriesOf(expectedQueryParams);
    }

}