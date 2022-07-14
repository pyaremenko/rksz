package com.project.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project.network.dao.GroupDao;
import com.project.network.dao.implementation.DefaultGroupDao;
import com.project.network.dto.GroupDto;

public class GroupDaoTest {

    private static final GroupDao groupDao = new DefaultGroupDao();

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
    }

    @Test
    public void getAllGroupsMustReturnTwoRecords() throws SQLException {
        List<GroupDto> allGroups = groupDao.getAllGroups();
        assertEquals(2, allGroups.size());
    }

    @Test
    public void afterUpdateDataMustBeUpdated() throws SQLException {
        String afterName = "М\'ясо";
        int groupIndex = 0;
        List<GroupDto> beforeUpdate = groupDao.getAllGroups();
        GroupDto groupDto = beforeUpdate.get(groupIndex).withName(afterName);
        int rowCount = groupDao.updateGroupInfo(groupDto);
        assertEquals(1, rowCount);
        List<GroupDto> afterUpdate = groupDao.getAllGroups();
        assertNotEquals(beforeUpdate.get(groupIndex), afterUpdate.get(groupIndex));
        assertEquals(afterName, afterUpdate.get(groupIndex).getName());
    }

    @Test
    public void afterDeleteOneGroupsNumberMustBeSmaller() throws SQLException {

        List<GroupDto> beforeDelete = groupDao.getAllGroups();
        assertEquals(2, beforeDelete.size());
        int deleteIndex = 1;

        int rowCount = groupDao.deleteGroupById(beforeDelete.get(0).getId());
        assertEquals(1, rowCount);

        List<GroupDto> afterDelete = groupDao.getAllGroups();
        assertEquals(1, afterDelete.size());
        assertEquals(beforeDelete.get(deleteIndex).getId(), afterDelete.get(deleteIndex - 1).getId());
        assertEquals(beforeDelete.get(deleteIndex).getName(), afterDelete.get(deleteIndex - 1).getName());
        assertEquals(beforeDelete.get(deleteIndex).getDescription(), afterDelete.get(deleteIndex - 1).getDescription());
    }

    @Test
    public void groupReturnedByIdMustBeSimilarToGroupFromListWithSameId() throws SQLException {
        List<GroupDto> allGroups = groupDao.getAllGroups();
        assertEquals(2, allGroups.size());
        GroupDto expected = allGroups.get(1);
        GroupDto actual = groupDao.getGroupById(expected.getId()).orElseThrow();
        assertEquals(expected, actual);
    }

    @Test
    public void groupReturnedByNameMustBeSimilarToGroupFromListWithSameName() throws SQLException {
        List<GroupDto> allGroups = groupDao.getAllGroups();
        assertEquals(2, allGroups.size());
        GroupDto expected = allGroups.get(1);
        GroupDto actual = groupDao.getGroupByName(expected.getName()).orElseThrow();
        assertEquals(expected, actual);
    }

    @Test
    public void mustReturnZeroIfUpdateIdNotExists() throws SQLException {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(1000);
        groupDto.setName("name");
        int rowCount = groupDao.updateGroupInfo(groupDto);
        assertEquals(0, rowCount);
    }

    @Test
    public void mustReturnZeroIfDeleteIdNotExists() throws SQLException {
        int rowCount = groupDao.deleteGroupById(1000);
        assertEquals(0, rowCount);
    }

    @Test(expected = SQLException.class)
    public void throwIfInsertNameAlreadyExists() throws SQLException {
        List<GroupDto> all = groupDao.getAllGroups();
        groupDao.insertNewGroup(all.get(0));
    }

    @Test
    public void noValueIfIdNotExists() throws SQLException {
        boolean condition = groupDao.getGroupById(1000).isPresent();
        assertFalse(condition);
    }

    @Test
    public void noValueIfNameNotExists() throws SQLException {
        boolean condition = groupDao.getGroupByName("HLSDFJLS").isPresent();
        assertFalse(condition);
    }

    @After
    public void deleteTestDatabase() {
        File file = new File(testDatabaseName);
        file.delete();
    }
}
