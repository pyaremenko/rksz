package com.project.network.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Integer id;
    @With
    private String name;
    private String description;
}
