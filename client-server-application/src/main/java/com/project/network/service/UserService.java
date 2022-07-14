package com.project.network.service;

import com.project.network.dto.JwtTokenDto;
import com.project.network.dto.UserDto;

public interface UserService {
    JwtTokenDto loginUserAndReturnJwt(final UserDto credentials);

    UserDto createNewUser(final UserDto userDto);

    boolean validateUserJwt(final JwtTokenDto jwtTokenDto);
}
