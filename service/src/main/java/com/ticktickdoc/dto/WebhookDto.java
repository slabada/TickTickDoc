package com.ticktickdoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WebhookDto {
    private String type;
    private String event;
    private WebhookObjectDto object;
}
