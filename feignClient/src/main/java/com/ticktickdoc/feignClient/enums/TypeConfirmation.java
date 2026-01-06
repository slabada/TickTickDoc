package com.ticktickdoc.feignClient.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeConfirmation {

    REDIRECT("redirect"),;

    private final String value;
}
