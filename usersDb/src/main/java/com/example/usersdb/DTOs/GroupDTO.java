package com.example.usersdb.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupDTO {
    @NotBlank
    private String name;
    @Min(0)
    private Long rating;
}
