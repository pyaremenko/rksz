package com.project.network.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.project.network.dto.GoodDto;

public interface GoodDao {
    List<GoodDto> getAllGoods() throws SQLException;

    int writeOffAddProducts(GoodDto goodDto, int amount) throws SQLException;

    List<GoodDto> getGoodsByGroupId(final int groupId) throws SQLException;

    Optional<GoodDto> getGoodById(final int id) throws SQLException;

    Optional<GoodDto> getGoodByName(final String name) throws SQLException;

    int insertNewGood(final GoodDto goodDto) throws SQLException;

    int updateGoodInfoById(final GoodDto goodDto) throws SQLException;

    int deleteGoodById(final int id) throws SQLException;
}
