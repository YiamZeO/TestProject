package com.example.usersdb.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductsSpecAndExcelDataStyleDTO {
    @NotNull
    private ProductSpecDTO productSpecDTO;
    @NotNull
    private ExcelDataStyleDTO excelDataStyleDTO;
}
