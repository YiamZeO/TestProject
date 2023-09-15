package com.example.usersdb.responsObjects;

import lombok.Data;

import java.util.List;

@Data
public class FilteringResponsObject {
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;
    private List<?> dataList;
}
