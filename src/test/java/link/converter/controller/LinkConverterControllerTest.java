package link.converter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.converter.controller.dto.DeeplinkResponseDto;
import link.converter.controller.dto.ErrorResponseDto;
import link.converter.controller.dto.UrlRequestDto;
import link.converter.exception.LinkValidationException;
import link.converter.exception.MalformedLinkException;
import link.converter.exception.MappingException;
import link.converter.service.link.UrlService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LinkConverterController.class)
class LinkConverterControllerTest {

    private static final String BASE_PATH = "/link/converter";
    private static final String URL_TO_DEEPLINK_PATH = BASE_PATH + "/url/to/deeplink";
    private static final String BAD_REQUEST_ERROR_CODE = "server_error";
    private static final String BAD_REQUEST_ERROR_MESSAGE = "Can't parse provided link";
    private static final String INTERNAL_SERVER_ERROR_ERROR_MESSAGE = "Server error occurred";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UrlService urlService;

    @ParameterizedTest
    @MethodSource("getUrls")
    void convertUrlToDeeplink_WhenRequestedWithUrl_ThenDeeplinkReturned(String url) throws Exception {
        String requestBody = getUrlRequestBody(url);
        DeeplinkResponseDto deeplinkResponse = new DeeplinkResponseDto("deeplink");
        when(urlService.convertToDeeplink(
                argThat(urlDto -> url.equals(urlDto.getUrl().toString())))).thenReturn(deeplinkResponse);
        String responseBody = mapper.writeValueAsString(deeplinkResponse);

        mockMvc.perform(post(URL_TO_DEEPLINK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @ParameterizedTest
    @MethodSource("getExceptions")
    void convertUrlToDeeplink_WhenExceptionThrown_ThenResponseStatusBadRequestReturned(RuntimeException ex) throws Exception {
        String requestBody = getUrlRequestBody("https://host:8888/path");
        String errorCode = "unacceptable_link_format";
        when(urlService.convertToDeeplink(any())).thenThrow(ex);
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorCode, BAD_REQUEST_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
        String errorResponseString = mapper.writeValueAsString(errorResponse);

        mockMvc.perform(post(URL_TO_DEEPLINK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(errorResponseString));
    }

    @Test
    void convertUrlToDeeplink_WhenMappingExceptionThrown_ThenResponseStatusInternalServerErrorReturned() throws Exception {
        String requestBody = getUrlRequestBody("https://host:8888/path");
        when(urlService.convertToDeeplink(any())).thenThrow(new MappingException(INTERNAL_SERVER_ERROR_ERROR_MESSAGE));
        ErrorResponseDto errorResponse = new ErrorResponseDto(BAD_REQUEST_ERROR_CODE, INTERNAL_SERVER_ERROR_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        String errorResponseString = mapper.writeValueAsString(errorResponse);

        mockMvc.perform(post(URL_TO_DEEPLINK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(errorResponseString));
    }

    private static Stream<Arguments> getUrls() {
        return Stream.of(
                Arguments.of("https://www.trendyol.com/casio/saat-p-1925865?boutiqueId=439892&merchantId=105064"),
                Arguments.of("https://www.trendyol.com/casio/erkek-kol-saati-p-1925865"),
                Arguments.of("https://www.trendyol.com/casio/erkek-kol-saati-p-1925865?boutiqueId=439892"),
                Arguments.of("https://www.trendyol.com/casio/erkek-kol-saati-p-1925865?merchantId=105064"),
                Arguments.of("https://www.trendyol.com/sr?q=elbise"),
                Arguments.of("https://www.trendyol.com/sr?q=%C3%BCt%C3%BC"),
                Arguments.of("https://www.trendyol.com/Hesabim/Favoriler"),
                Arguments.of("https://www.trendyol.com/Hesabim#/Siparisleri")
        );
    }

    private static Stream<Arguments> getExceptions() {
        return Stream.of(
                Arguments.of(new MalformedLinkException(BAD_REQUEST_ERROR_MESSAGE, new RuntimeException())),
                Arguments.of(new LinkValidationException(BAD_REQUEST_ERROR_MESSAGE)),
                Arguments.of(new IllegalStateException(BAD_REQUEST_ERROR_MESSAGE))
        );
    }

    private String getUrlRequestBody(String url) throws Exception {
        URL requestUrl = new URL(url);
        UrlRequestDto request = new UrlRequestDto(requestUrl);
        return mapper.writeValueAsString(request);
    }

}