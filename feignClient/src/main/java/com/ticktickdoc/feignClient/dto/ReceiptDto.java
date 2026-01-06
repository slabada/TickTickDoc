package com.ticktickdoc.feignClient.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDto {

    private CustomerDto customer;
    private List<ItemsDto> items;
}
