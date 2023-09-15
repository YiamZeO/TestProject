package com.example.usersdb.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    @NotBlank
    private String name;
}
