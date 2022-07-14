package com.project.network.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodDto {
    private Integer id;
    @With
    private String name;
    private GroupDto group;
    private String description;
    private String manufacturer;
    private Integer number;
    private BigDecimal price;
}
