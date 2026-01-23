package com.ticktickdoc.mapper;

import com.ticktickdoc.domain.UserDomain;
import com.ticktickdoc.dto.RegistrationDto;
import com.ticktickdoc.dto.UserDto;
import com.ticktickdoc.dto.UserUpdateDto;
import com.ticktickdoc.model.entity.UserModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses =  {SubscriptionMapper.class})
public interface UserMapper {

    UserDomain toDomain(UserModel userModel);

    @Mapping(target = "telegram", ignore = true)
    @Mapping(target = "notificationType", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserDomain toDomain(RegistrationDto dto);

    UserModel toModel(UserDomain userDomain);

    UserDto toDto(UserDomain userDomain);

    @Mapping(target = "password", ignore = true)
    UserDomain toDomain(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserDomain toDomain(UserUpdateDto Dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserDomain userDomain, @MappingTarget UserModel model);
}
