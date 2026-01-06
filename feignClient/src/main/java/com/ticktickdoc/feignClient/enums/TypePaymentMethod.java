package com.ticktickdoc.feignClient.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypePaymentMethod {

    BANK_CARD("bank_card");

    private final String value;
}
