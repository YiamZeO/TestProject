package com.example.usersdb.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {
    @NotBlank
    private String name;
    @Min(0)
    private Long age;
    @NotBlank
    private String password;
}
