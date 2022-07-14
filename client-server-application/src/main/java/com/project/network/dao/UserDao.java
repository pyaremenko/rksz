package com.project.network.dao;

import java.sql.SQLException;
import java.util.Optional;

import com.project.network.dto.UserDto;

public interface UserDao {
    Optional<UserDto> getUserByLogin(final String login) throws SQLException;

    int insertNewUser(final UserDto userDto) throws SQLException;

    int updateUserByLogin(final UserDto userDto) throws SQLException;
}
