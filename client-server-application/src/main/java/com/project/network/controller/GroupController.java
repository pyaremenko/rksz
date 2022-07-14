package com.project.network.controller;

import java.util.List;

import com.project.network.annotation.PathParameter;
import com.project.network.annotation.RequestBody;
import com.project.network.annotation.RequestMapping;
import com.project.network.annotation.RequestMethod;
import com.project.network.annotation.RestController;
import com.project.network.dto.GroupDto;
import com.project.network.service.GroupService;
import com.project.network.service.implementation.DefaultGroupService;

@RestController
public class GroupController {

    private GroupService groupService = new DefaultGroupService();

    @RequestMapping(path = "/api/groups", method = RequestMethod.GET)
    public List<GroupDto> getAllGroups() {
        return groupService.getAllGroups();
    }

    @RequestMapping(path = "/api/group", method = RequestMethod.PUT)
    public GroupDto addGroup(@RequestBody GroupDto groupDto) {
        return groupService.addGroup(groupDto);
    }

    @RequestMapping(path = "/api/group/{id}", method = RequestMethod.POST)
    public void updateGroup(@RequestBody GroupDto groupDto, @PathParameter("id") String groupId) {
        groupService.updateGroup(groupId, groupDto);
    }

    @RequestMapping(path = "/api/group/{id}", method = RequestMethod.DELETE)
    public void deleteGroup(@PathParameter("id") String groupId) {
        groupService.deleteGroupById(groupId);
    }
}
