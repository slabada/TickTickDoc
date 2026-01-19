package com.ticktickdoc.domain;

import com.ticktickdoc.enums.InventStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteActionDomain {

    private String actionId;
    private InventStatusEnum inventStatus;
}
