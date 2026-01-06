package com.ticktickdoc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubscriptionEnum {

    SUBSCRIPTION_ACTIVE("SUBSCRIPTION_ACTIVE");

    private final String value;
}
