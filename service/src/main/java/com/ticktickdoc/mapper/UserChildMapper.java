package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.domain.UserChildDomain;
import com.ticktickdoc.dto.RequestIdDto;
import com.ticktickdoc.dto.ResponseIdDto;
import com.ticktickdoc.model.entity.UserChildModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserChildMapper {

    RequestIdDomain toDomain(RequestIdDto dto);

    List<RequestIdDomain> toDomain(List<RequestIdDto> dto);

    ResponseIdDto toDto(ResponseIdDomain domain);

    List<ResponseIdDto> toDto(List<ResponseIdDomain> domains);

    UserChildDomain toDomain(UserChildModel model);
}
