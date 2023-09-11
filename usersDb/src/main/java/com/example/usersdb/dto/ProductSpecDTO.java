package com.example.usersdb.dto;

import com.example.usersdb.entities.TagsForProducts;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
    List<Long> tagsIdList;
}
