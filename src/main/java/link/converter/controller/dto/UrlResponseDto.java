package link.converter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Dto representing response of conversion of any link type to url
 */
@Getter
@AllArgsConstructor
public class UrlResponseDto implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String url;

}
