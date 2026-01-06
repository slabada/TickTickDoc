package com.ticktickdoc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDomain {

    private String description;
    private AmountDomain amount;
    @JsonProperty("vat_code")
    private Integer vatCode;
    private Integer quantity;
}
