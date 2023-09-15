package com.example.usersdb.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
