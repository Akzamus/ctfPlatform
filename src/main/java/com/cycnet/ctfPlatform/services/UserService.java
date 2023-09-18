package com.cycnet.ctfPlatform.services;

import com.cycnet.ctfPlatform.dto.user.UserRequestDto;
import com.cycnet.ctfPlatform.dto.user.UserResponseDto;
import com.cycnet.ctfPlatform.models.User;

public interface UserService extends CrudService<User, UserRequestDto, UserResponseDto> {

    void throwExceptionIfUserExists(String email);

}
