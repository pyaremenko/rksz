package com.project.network.dao.implementation;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.project.network.executor.SQLExecutor;
import com.project.network.executor.implementation.SQLiteExecutor;
import com.project.network.dao.GoodDao;
import com.project.network.dao.RowMapper;
import com.project.network.dao.mapper.GoodDtoMapper;
import com.project.network.dto.GoodDto;

public class DefaultGoodDao implements GoodDao {

    private SQLExecutor executor = new SQLiteExecutor();

    private static final String GET_ALL_GOODS = "SELECT * FROM `good`\n" +
            "INNER JOIN `group` ON `group`.`group_id` = `good`.`group_id`";
    private static final String GET_GOODS_BY_GROUP_ID = "SELECT * FROM `good`\n" +
            "INNER JOIN `group` ON `group`.`group_id` = `good`.`group_id`\n" +
            "WHERE `good`.`group_id` = ?";
    private static final String GET_GOOD_BY_ID = "SELECT * FROM `good`\n" +
            "INNER JOIN `group` ON `group`.`group_id` = `good`.`group_id`\n" +
            "WHERE `good`.`good_id` = ?";
    private static final String GET_GOOD_BY_NAME = "SELECT * FROM `good`\n" +
            "INNER JOIN `group` ON `group`.`group_id` = `good`.`group_id`\n" +
            "WHERE `good`.`good_name` = ?";
    private static final String INSERT_NEW_GOOD = "INSERT INTO `good`\n" +
            "(`good_id`," +
            "`good_name`," +
            "`group_id`," +
            "`good_description`," +
            "`manufacturer`," +
            "`number`," +
            "`price`)\n" +
            "VALUES (NULL, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_GOOD_INFO_BY_ID = "UPDATE `good`\n" +
            "SET `good_name` = ?," +
            "`group_id` = ?," +
            "`good_description` = ?," +
            "`manufacturer` = ?," +
            "`number` = ?," +
            "`price` = ?\n" +
            "WHERE `good`.`good_id` = ?";
    private static final String DELETE_GOOD_BY_ID = "DELETE FROM `good` WHERE `good`.`good_id` = ?";

    @Override
    public List<GoodDto> getAllGoods() throws SQLException {
        RowMapper<GoodDto> mapper = new GoodDtoMapper();
        return executor.executeQuery(GET_ALL_GOODS, mapper);
    }

    @Override
    public int writeOffAddProducts(final GoodDto goodDto, final int amount) throws SQLException {
        final int finalAmount = goodDto.getNumber() + amount;
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(goodDto.getName());
        parameterList.add(goodDto.getGroup().getId());
        parameterList.add(goodDto.getDescription());
        parameterList.add(goodDto.getManufacturer());
        parameterList.add(finalAmount);
        parameterList.add(goodDto.getPrice());
        parameterList.add(goodDto.getId());
        return executor.update(UPDATE_GOOD_INFO_BY_ID, parameterList);
    }

    @Override
    public List<GoodDto> getGoodsByGroupId(final int groupId) throws SQLException {
        RowMapper<GoodDto> mapper = new GoodDtoMapper();
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(groupId);
        return executor.executeQuery(GET_GOODS_BY_GROUP_ID, parameterList, mapper);
    }

    @Override
    public Optional<GoodDto> getGoodById(final int id) throws SQLException {
        RowMapper<GoodDto> mapper = new GoodDtoMapper();
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(id);
        List<GoodDto> list = executor.executeQuery(GET_GOOD_BY_ID, parameterList, mapper);
        return list.stream().findFirst();
    }

    @Override
    public Optional<GoodDto> getGoodByName(final String name) throws SQLException {
        RowMapper<GoodDto> mapper = new GoodDtoMapper();
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(name);
        List<GoodDto> list = executor.executeQuery(GET_GOOD_BY_NAME, parameterList, mapper);
        return list.stream().findFirst();
    }

    @Override
    public int insertNewGood(final GoodDto goodDto) throws SQLException {
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(goodDto.getName());
        parameterList.add(goodDto.getGroup().getId());
        parameterList.add(goodDto.getDescription());
        parameterList.add(goodDto.getManufacturer());
        parameterList.add(goodDto.getNumber());
        parameterList.add(goodDto.getPrice());
        return executor.update(INSERT_NEW_GOOD, parameterList);
    }

    @Override
    public int updateGoodInfoById(final GoodDto goodDto) throws SQLException {
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(goodDto.getName());
        parameterList.add(goodDto.getGroup().getId());
        parameterList.add(goodDto.getDescription());
        parameterList.add(goodDto.getManufacturer());
        parameterList.add(goodDto.getNumber());
        parameterList.add(goodDto.getPrice());
        parameterList.add(goodDto.getId());
        return executor.update(UPDATE_GOOD_INFO_BY_ID, parameterList);
    }

    @Override
    public int deleteGoodById(final int id) throws SQLException {
        List<Object> parameterList = new LinkedList<>();
        parameterList.add(id);
        return executor.update(DELETE_GOOD_BY_ID, parameterList);
    }
}
