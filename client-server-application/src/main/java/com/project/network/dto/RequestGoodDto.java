package com.project.network.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestGoodDto {
    private Integer id;
    private String name;
    private String groupId;
    private String description;
    private String manufacturer;
    private Integer number;
    private BigDecimal price;
}
