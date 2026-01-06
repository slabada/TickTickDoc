package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.dto.RegistrationDto;
import com.ticktickdoc.dto.UserDto;
import com.ticktickdoc.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(UserDomain userDomain, @MappingTarget UserModel model);
}
