package com.ticktickdoc.feignClient.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CurrencyEnum {

    RUB("RUB");

    private final String value;
}
