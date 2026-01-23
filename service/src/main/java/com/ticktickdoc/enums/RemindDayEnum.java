package com.ticktickdoc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RemindDayEnum {
    ONE(1L),
    THREE(3L),
    SEVEN(7L),
    FOURTEEN(14L),
    THIRTY(30L);

    private final Long day;
}
