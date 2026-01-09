package com.ticktickdoc.feignClient.domain;

import com.ticktickdoc.feignClient.enums.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmountDomain {

    private String value;
    private CurrencyEnum currency;
}
