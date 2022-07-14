package com.project.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project.network.dao.GroupDao;
import com.project.network.dao.GoodDao;
import com.project.network.dao.implementation.DefaultGroupDao;
import com.project.network.dao.implementation.DefaultGoodDao;
import com.project.network.dto.GroupDto;
import com.project.network.dto.GoodDto;

public class GoodDaoTest {
    private static final GroupDao groupDao = new DefaultGroupDao();
    private static final GoodDao goodDao = new DefaultGoodDao();

    private static final String testDatabaseName = "test.db";

    @Before
    public void createTestDatabase() throws SQLException, IOException {
        Application application = new Application();
        application.initializeDatabase(testDatabaseName);

        GroupDto groupDto = new GroupDto();
        String name = "Овочі";
        groupDto.setName(name);
        groupDao.insertNewGroup(groupDto);
        name = "Крупи";
        groupDto.setName(name);
        groupDto.setDescription("Гречка, вівсянка та ін.");
        groupDao.insertNewGroup(groupDto);

        GoodDto goodDto = new GoodDto();
        goodDto.setName("Огірок");
        goodDto.setGroup(new GroupDto(1, null, null));
        goodDto.setNumber(10);
        goodDto.setPrice(new BigDecimal("2.63"));
        goodDao.insertNewGood(goodDto);

        goodDto.setName("Томат");
        goodDao.insertNewGood(goodDto);

        goodDto.setName("Гречка");
        goodDto.setGroup(new GroupDto(2, null, null));
        goodDao.insertNewGood(goodDto);
    }

    @Test
    public void mustBeThreeRecordsInGood() throws SQLException {
        List<GoodDto> list = goodDao.getAllGoods();
        for (GoodDto i : list) {
            System.out.println(i.toString());
        }
        int actual = list.size();
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void getByGroupShouldReturnOnlyGoodsWithSameGroup() throws SQLException {
        Integer groupId = 1;
        List<GoodDto> list = goodDao.getGoodsByGroupId(groupId);
        assertEquals(2, list.size());
        for (GoodDto good : list) {
            assertEquals(good.getGroup().getId(), groupId);
        }
    }

    @Test
    public void testGetGoodById() throws SQLException {
        List<GoodDto> list = goodDao.getAllGoods();
        GoodDto expected = list.get(1);
        GoodDto actual = goodDao.getGoodById(expected.getId()).orElseThrow();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGoodByName() throws SQLException {
        List<GoodDto> list = goodDao.getAllGoods();
        GoodDto expected = list.get(1);
        GoodDto actual = goodDao.getGoodByName(expected.getName()).orElseThrow();
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateGood() throws SQLException {
        List<GoodDto> list = goodDao.getAllGoods();
        GoodDto beforeUpdate = list.get(1);
        GoodDto expected = beforeUpdate.withName("Перець");
        goodDao.updateGoodInfoById(expected);
        GoodDto actual = goodDao.getGoodById(beforeUpdate.getId()).orElseThrow();
        assertEquals(expected, actual);
    }

    @Test
    public void testDeleteGood() throws SQLException {
        List<GoodDto> beforeDelete = goodDao.getAllGoods();
        assertEquals(3, beforeDelete.size());
        goodDao.deleteGoodById(beforeDelete.get(1).getId());
        List<GoodDto> afterDelete = goodDao.getAllGoods();
        assertEquals(2, afterDelete.size());
    }

    @Test(expected = SQLException.class)
    public void throwIfInsertNameAlreadyExists() throws SQLException {
        List<GoodDto> all = goodDao.getAllGoods();
        goodDao.insertNewGood(all.get(0));
    }
    
    @Test
    public void noValuesIfGroupIdNotExists() throws SQLException {
        List<GoodDto> list = goodDao.getGoodsByGroupId(1000);
        assertEquals(0, list.size());
    }

    @Test
    public void noValueIfIdNotExists() throws SQLException {
        boolean condition = goodDao.getGoodById(1000).isPresent();
        assertFalse(condition);
    }

    @Test
    public void noValueIfNameNotExists() throws SQLException {
        boolean condition = goodDao.getGoodByName("HLSDFJLS").isPresent();
        assertFalse(condition);
    }

    @Test
    public void goodsDeleteIfGroupDelete() throws SQLException {
        int groupId = 1;
        List<GoodDto> beforeList = goodDao.getGoodsByGroupId(groupId);
        assertEquals(2, beforeList.size());
        groupDao.deleteGroupById(groupId); 
        List<GoodDto> afterList = goodDao.getGoodsByGroupId(groupId);
        assertEquals(0, afterList.size());
    }

    @After
    public void deleteTestDatabase() {
        File file = new File(testDatabaseName);
        file.delete();
    }
}
