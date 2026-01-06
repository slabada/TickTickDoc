package com.ticktickdoc.feignClient.dto;

import com.ticktickdoc.feignClient.enums.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmountDto {

    private String value;
    private CurrencyEnum currency;
}
