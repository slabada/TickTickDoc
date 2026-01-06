package com.ticktickdoc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class WebhookObjectDto {
    private String id;
    private String status;
    private Map<String, Object> metadata;
}
