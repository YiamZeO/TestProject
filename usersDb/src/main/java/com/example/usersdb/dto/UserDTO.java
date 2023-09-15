package com.example.usersdb.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
