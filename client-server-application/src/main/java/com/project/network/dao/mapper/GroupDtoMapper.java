package com.project.network.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.network.dto.GroupDto;
import com.project.network.dao.RowMapper;

public class GroupDtoMapper implements RowMapper<GroupDto> {

    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    private static final String GROUP_DESCRIPTION = "group_description";

    @Override
    public GroupDto map(ResultSet rs) throws SQLException {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(rs.getInt(GROUP_ID));
        groupDto.setName(rs.getString(GROUP_NAME));
        groupDto.setDescription(rs.getString(GROUP_DESCRIPTION));
        return groupDto;
    }
    
}
