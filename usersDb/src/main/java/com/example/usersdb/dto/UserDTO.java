package com.example.usersdb.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank
    private String name;
    @Min(0)
    private Long age;
    @NotBlank
    private String password;
}
