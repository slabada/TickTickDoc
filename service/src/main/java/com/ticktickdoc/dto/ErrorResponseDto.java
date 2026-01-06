package com.ticktickdoc.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponseDto {

    private String message;
    private OffsetDateTime timestamp;
    private Integer code;

}
