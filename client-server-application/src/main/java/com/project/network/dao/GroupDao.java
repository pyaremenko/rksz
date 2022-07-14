package com.project.network.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.project.network.dto.GroupDto;

public interface GroupDao {
    List<GroupDto> getAllGroups() throws SQLException;

    Optional<GroupDto> getGroupById(final int id) throws SQLException;

    Optional<GroupDto> getGroupByName(final String name) throws SQLException;

    int insertNewGroup(final GroupDto groupDto) throws SQLException;

    int updateGroupInfo(final GroupDto groupDto) throws SQLException;

    int deleteGroupById(final int id) throws SQLException;
}
