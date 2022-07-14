package com.project.network;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project.network.dto.GoodDto;
import com.project.network.dto.GroupDto;
import com.project.network.dto.RequestGoodDto;
import com.project.network.exception.ResponseErrorException;
import com.project.network.service.implementation.DefaultGoodService;
import com.project.network.service.implementation.DefaultGroupService;


public class GoodServiceTest
{
    private static DefaultGoodService goodService;

    private static final String testDatabaseName = "test.db";

    @Before
    public  void createTestDatabase() throws SQLException, IOException
    {
        Application application = new Application();
        application.initializeDatabase(testDatabaseName);

        DefaultGroupService groupService = new DefaultGroupService();
        GroupDto meat = new GroupDto();
        String name = "Meat";
        meat.setName(name);
        meat.setId(1);
        groupService.addGroup(meat);

        goodService = new DefaultGoodService();
        RequestGoodDto sausage = new RequestGoodDto(1 , "sausage" , "1" , "" , "" , 1 , BigDecimal.ONE);
        sausage.setGroupId(meat.getId().toString());

        RequestGoodDto beef = new RequestGoodDto(2 , "beef" , "1" , "" , "" , 1 , BigDecimal.ONE);
        beef.setGroupId(meat.getId().toString());

        goodService.createNewGood(sausage);
        goodService.createNewGood(beef);
    }

    @Test
    public void afterUpdateDataMustBeUpdated() throws SQLException
    {
        String afterName = "Sausage";
        int groupIndex = 0;
        List<GoodDto> beforeUpdate = goodService.getAllGoods();
        RequestGoodDto goodDto = new RequestGoodDto(1 , afterName , "1" , "" , "" , 1 , BigDecimal.ONE);
        assertThatNoException().isThrownBy(() -> goodService.updateGoodById(goodDto.getId().toString(), goodDto));
        List<GoodDto> afterUpdate = goodService.getAllGoods();
        assertNotEquals(beforeUpdate.get(groupIndex).getName() , afterUpdate.get(groupIndex).getName());
        assertEquals(afterName, afterUpdate.get(groupIndex).withName(afterName).getName());
    }

    @Test
    public void afterDeleteOneGroupsNumberMustBeSmaller() throws SQLException
    {

        List<GoodDto> beforeDelete = goodService.getAllGoods();
        assertEquals(2, beforeDelete.size());
        int deleteIndex = 1;

        assertThatNoException().isThrownBy(() -> goodService.deleteGoodById(beforeDelete.get(0).getId().toString()));

        List<GoodDto> afterDelete = goodService.getAllGoods();
        assertEquals(1, afterDelete.size());
        assertEquals(beforeDelete.get(deleteIndex).getId(), afterDelete.get(deleteIndex - 1).getId());
        assertEquals(beforeDelete.get(deleteIndex).getName(), afterDelete.get(deleteIndex - 1).getName());
        assertEquals(beforeDelete.get(deleteIndex).getDescription(), afterDelete.get(deleteIndex - 1).getDescription());
    }

    @Test
    public void mustReturnZeroIfUpdateIdNotExists() throws SQLException
    {
        RequestGoodDto goodDto = new RequestGoodDto(1000 , "name" , "1" , "" , "" , 1 , BigDecimal.ONE);
        assertThatExceptionOfType(ResponseErrorException.class).isThrownBy(
                () -> goodService.updateGoodById(goodDto.getId().toString(), goodDto));
    }

    @Test
    public void mustReturnZeroIfDeleteIdNotExists() throws SQLException
    {
        assertThatExceptionOfType(ResponseErrorException.class).isThrownBy(() -> goodService.deleteGoodById("1000"));
    }

    @Test
    public void throwIfInsertNameAlreadyExists()
    {
        assertThatExceptionOfType(ResponseErrorException.class).isThrownBy(() ->
        {
            List<GoodDto> all = goodService.getAllGoods();
            GoodDto goodDto = all.get(0);
            RequestGoodDto requestGoodDto = new RequestGoodDto();
            requestGoodDto.setId(goodDto.getId());
            requestGoodDto.setName(goodDto.getName());
            requestGoodDto.setDescription(goodDto.getDescription());
            requestGoodDto.setManufacturer(goodDto.getManufacturer());
            requestGoodDto.setNumber(goodDto.getNumber());
            requestGoodDto.setPrice(goodDto.getPrice());
            requestGoodDto.setGroupId(goodDto.getGroup().getId().toString());
            goodService.createNewGood(requestGoodDto);
        });
    }

    @After
    public void deleteTestDatabase()
    {
        File file = new File(testDatabaseName);
        file.delete();
    }
}
