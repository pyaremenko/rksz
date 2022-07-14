package com.project.network.service;

import java.util.List;

import com.project.network.dto.GroupDto;

public interface GroupService {
    List<GroupDto> getAllGroups();

    GroupDto addGroup(GroupDto groupDto);

	void updateGroup(final String id, GroupDto groupDto);

	void deleteGroupById(final String id);
}
