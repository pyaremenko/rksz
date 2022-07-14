package com.project.network.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageDto {
    private Integer statusCode;
    private String message;
    private Date timestamp;
}
