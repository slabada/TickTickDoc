package com.ticktickdoc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {

    SUCCEEDED("succeeded");

    private final String value;
}
