package com.ticktickdoc.feignClient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDto {

    private String description;
    private AmountDto amount;
    @JsonProperty("vat_code")
    private Integer vatCode;
    private Integer quantity;
}
