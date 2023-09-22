package com.example.usersdb.responseObjects;

import lombok.Data;

import java.util.List;

@Data
public class FilteringResponseObj {
    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;
    private List<?> dataList;
}
