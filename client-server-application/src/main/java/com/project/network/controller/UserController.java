package com.project.network.controller;

import com.project.network.annotation.RequestBody;
import com.project.network.annotation.RequestMapping;
import com.project.network.annotation.RequestMethod;
import com.project.network.annotation.RestController;
import com.project.network.dto.JwtTokenDto;
import com.project.network.dto.UserDto;
import com.project.network.service.UserService;
import com.project.network.service.implementation.DefaultUserService;

@RestController
public class UserController {

    UserService userService = new DefaultUserService();

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public JwtTokenDto loginUser(@RequestBody UserDto credentials) {
        return userService.loginUserAndReturnJwt(credentials);
    }
}
