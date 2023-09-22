package com.example.usersdb.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OrderDTO {
    @NotNull
    private Long userId;
    @NotBlank
    private String address;
    @DateTimeFormat
    private Date date;
    @NotNull
    private List<Map<String, Long>> productsInOrder;
}
