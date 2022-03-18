package link.converter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Dto representing response of conversion of any link type to deeplink
 */
@Getter
@AllArgsConstructor
public class DeeplinkResponseDto implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String deeplink;

}