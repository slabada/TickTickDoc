package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.InviteActionDomain;
import com.ticktickdoc.domain.InviteRequestDomain;
import com.ticktickdoc.dto.InviteActionDto;
import com.ticktickdoc.dto.InviteRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageNotificationMapper {

    InviteRequestDomain toDomain(InviteRequestDto dto);

    InviteActionDomain toDomain(InviteActionDto dto);
}
