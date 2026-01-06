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
public class ConfirmationDomain {

    private String type;

    @JsonProperty("return_url")
    private String returnUrl;

    @JsonProperty("confirmation_url")
    private String confirmationUrl;
}
