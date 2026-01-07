package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.RequestIdDomain;
import com.ticktickdoc.domain.ResponseIdDomain;
import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.dto.RegistrationDto;
import com.ticktickdoc.dto.RequestIdDto;
import com.ticktickdoc.dto.ResponseIdDto;
import com.ticktickdoc.dto.UserDto;
import com.ticktickdoc.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses =  {SubscriptionMapper.class})
public interface UserMapper {

    UserDomain toDomain(UserModel userModel);

    @Mapping(target = "linkSubsidiaryUser", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserDomain toDomain(RegistrationDto dto);

    UserModel toModel(UserDomain userDomain);

    UserDto toDto(UserDomain userDomain);

    @Mapping(target = "password", ignore = true)
    UserDomain toDomain(UserDto userDto);

    RequestIdDomain toDomain(RequestIdDto dto);

    List<RequestIdDomain> toDomain(List<RequestIdDto> dto);

    ResponseIdDto toModel(ResponseIdDomain domain);

    List<ResponseIdDto> toModel(List<ResponseIdDomain> domains);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(UserDomain userDomain, @MappingTarget UserModel model);
}
