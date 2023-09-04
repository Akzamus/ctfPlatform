package com.cycnet.ctfPlatform.mappers;

import com.cycnet.ctfPlatform.dto.user.UserRequestDto;
import com.cycnet.ctfPlatform.dto.user.UserResponseDto;
import com.cycnet.ctfPlatform.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserRequestDto, UserResponseDto> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "student", ignore = true)
    User toEntity(UserRequestDto request);

}
