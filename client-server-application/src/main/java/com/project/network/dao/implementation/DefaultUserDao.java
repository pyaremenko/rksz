package com.project.network.dao.implementation;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.project.network.dto.UserDto;
import com.project.network.executor.SQLExecutor;
import com.project.network.executor.implementation.SQLiteExecutor;
import com.project.network.dao.RowMapper;
import com.project.network.dao.UserDao;
import com.project.network.dao.mapper.UserDtoMapper;

public class DefaultUserDao implements UserDao {

    private SQLExecutor executor = new SQLiteExecutor();

    private static final String GET_USER_BY_LOGIN = "SELECT * FROM `user` WHERE `login` = ?";
    private static final String INSERT_NEW_USER = "INSERT INTO `user` (`login`, `password`) VALUES (?, ?)";
    private static final String UPDATE_USER_BY_LOGIN = "UPDATE `user` SET `password` = ? WHERE `login` = ?";

    @Override
    public Optional<UserDto> getUserByLogin(final String login) throws SQLException {
        RowMapper<UserDto> mapper = new UserDtoMapper();
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(login);
        List<UserDto> list = executor.executeQuery(GET_USER_BY_LOGIN, parameterList, mapper);
        return list.stream().findFirst();
    }

    @Override
    public int insertNewUser(UserDto userDto) throws SQLException {
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(userDto.getLogin());
        parameterList.add(userDto.getPassword());
        return executor.update(INSERT_NEW_USER, parameterList);
    }

    @Override
    public int updateUserByLogin(UserDto userDto) throws SQLException {
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(userDto.getPassword());
        parameterList.add(userDto.getLogin());
        return executor.update(UPDATE_USER_BY_LOGIN, parameterList);
    }

}
