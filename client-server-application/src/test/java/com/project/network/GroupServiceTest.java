package com.project.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.project.network.dto.GroupDto;
import com.project.network.exception.ResponseErrorException;
import com.project.network.service.implementation.DefaultGroupService;


public class GroupServiceTest
{

	private static DefaultGroupService groupService;

	private static final String testDatabaseName = "test.db";

	@Before
	public  void createTestDatabase() throws SQLException, IOException
	{
		groupService = new DefaultGroupService();
		Application application = new Application();
		application.initializeDatabase(testDatabaseName);
		GroupDto meat = new GroupDto();
		String name = "Meat";
		meat.setName(name);
		meat.setId(1);
		groupService.addGroup(meat);

		GroupDto fruits = new GroupDto();
		name = "Fruits";
		fruits.setName(name);
		fruits.setId(2);
		fruits.setDescription("Bananas , apples ...");
		groupService.addGroup(fruits);
	}

	@Test
	public void afterUpdateDataMustBeUpdated() throws SQLException
	{
		String afterName = "MEAT";
		int groupIndex = 0;
		List<GroupDto> beforeUpdate = groupService.getAllGroups();
		GroupDto groupDto = new GroupDto(1 , afterName , "Some Description");
		assertThatNoException().isThrownBy(() -> groupService.updateGroup(groupDto.getId().toString(), groupDto));
		List<GroupDto> afterUpdate = groupService.getAllGroups();
		assertNotEquals(beforeUpdate.get(groupIndex).getName() , afterUpdate.get(groupIndex).getName());
		assertEquals(afterName, afterUpdate.get(groupIndex).withName(afterName).getName());
	}

	@Test
	public void afterDeleteOneGroupsNumberMustBeSmaller() throws SQLException
	{

		List<GroupDto> beforeDelete = groupService.getAllGroups();
		assertEquals(2, beforeDelete.size());
		int deleteIndex = 1;

		assertThatNoException().isThrownBy(() -> groupService.deleteGroupById(beforeDelete.get(0).getId().toString()));

		List<GroupDto> afterDelete = groupService.getAllGroups();
		assertEquals(1, afterDelete.size());
		assertEquals(beforeDelete.get(deleteIndex).getId(), afterDelete.get(deleteIndex - 1).getId());
		assertEquals(beforeDelete.get(deleteIndex).getName(), afterDelete.get(deleteIndex - 1).getName());
		assertEquals(beforeDelete.get(deleteIndex).getDescription(), afterDelete.get(deleteIndex - 1).getDescription());
	}

	@Test
	public void mustReturnZeroIfUpdateIdNotExists() throws SQLException
	{
		GroupDto groupDto = new GroupDto();
		groupDto.setId(1000);
		groupDto.setName("name");
		assertThatExceptionOfType(ResponseErrorException.class).isThrownBy(
				() -> groupService.updateGroup(groupDto.getId().toString(), groupDto));
	}

	@Test
	public void mustReturnZeroIfDeleteIdNotExists() throws SQLException
	{
		assertThatExceptionOfType(ResponseErrorException.class).isThrownBy(() -> groupService.deleteGroupById("1000"));
	}

	@Test
	public void throwIfInsertNameAlreadyExists()
	{
		assertThatExceptionOfType(ResponseErrorException.class).isThrownBy(() ->
		{
			List<GroupDto> all = groupService.getAllGroups();
			groupService.addGroup(all.get(0));
		});
	}

	@After
	public void deleteTestDatabase()
	{
		File file = new File(testDatabaseName);
		file.delete();
	}
}
