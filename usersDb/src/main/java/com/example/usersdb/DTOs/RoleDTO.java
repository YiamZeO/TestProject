package com.example.usersdb.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    @NotBlank
    private String name;
}
