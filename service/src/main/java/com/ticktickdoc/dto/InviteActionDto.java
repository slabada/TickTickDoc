package com.ticktickdoc.dto;

import com.ticktickdoc.enums.InventStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteActionDto {

    private String actionId;
    private InventStatusEnum inventStatus;
}
