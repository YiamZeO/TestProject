package com.example.usersdb.DTOs;

import lombok.Data;

import java.util.Date;

@Data
public class ProductSpecDTO {
    String name;
    Date minD;
    Date maxD;
    String descrContain;
    Long minCost;
    Long maxCost;
    Long minQuality;
    Long maxQuality;
    Integer curPage;
    Integer pageSize;
}
