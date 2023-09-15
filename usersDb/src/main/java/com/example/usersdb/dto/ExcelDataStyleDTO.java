package com.example.usersdb.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExcelDataStyleDTO {
    @NotNull
    private Boolean headerIsBold;
    @NotNull
    private Boolean headerIsItalic;
    @NotNull
    private Boolean dataIsBold;
    @NotNull
    private Boolean dataIsItalic;
    @NotEmpty
    private String headerColor;
    @NotEmpty
    private String dataColor;
    @NotEmpty
    private String headerFontColor;
    @NotEmpty
    private String dataFontColor;
}
