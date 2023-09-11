package com.example.usersdb.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductSpecDTO {
    String name;
    Date minDate;
    Date maxDate;
    String descrContain;
    Long minCost;
    Long maxCost;
    Long minQuality;
    Long maxQuality;
    Integer curPage;
    Integer pageSize;
    List<Long> tagsIdList;
}
