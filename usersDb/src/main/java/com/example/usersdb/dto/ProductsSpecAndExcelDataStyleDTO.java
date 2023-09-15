package com.example.usersdb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductsSpecAndExcelDataStyleDTO {
    @NotNull
    private ProductSpecDTO productSpecDTO;
    @NotNull
    private ExcelDataStyleDTO excelDataStyleDTO;
}
