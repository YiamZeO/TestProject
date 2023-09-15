package com.example.usersdb.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ProductDTO {
    @NotBlank
    private String name;
    @DateTimeFormat
    private Date date;
    @NotBlank
    private String description;
    @Min(0)
    private Long cost;
    @Min(0)
    @Max(100)
    private Long quality;
}
