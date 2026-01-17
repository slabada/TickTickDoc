package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.dto.RequestIdDto;
import com.ticktickdoc.dto.ResponseIdDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChildMapper {

    RequestIdDomain toDomain(RequestIdDto dto);

    List<RequestIdDomain> toDomain(List<RequestIdDto> dto);

    ResponseIdDto toDto(ResponseIdDomain domain);

    List<ResponseIdDto> toDto(List<ResponseIdDomain> domains);

}
