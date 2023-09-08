package com.example.usersdb.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
