package com.project.network.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.network.dto.UserDto;
import com.project.network.dao.RowMapper;

public class UserDtoMapper implements RowMapper<UserDto> {

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @Override
    public UserDto map(ResultSet rs) throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setLogin(rs.getString(LOGIN));
        userDto.setPassword(rs.getString(PASSWORD));
        return userDto;
    }
    
}
