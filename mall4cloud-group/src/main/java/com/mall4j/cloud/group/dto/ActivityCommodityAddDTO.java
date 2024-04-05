package com.mall4j.cloud.group.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ActivityCommodityAddDTO {
    private List<String> failCommodityIds;
    //门店对应重复商品集合
    private Map<String,List<String>> backRepStoreAndSpus;
    //全部门店重复商品集合
    private List<String> backRepSpus;

    private String backMsg;
}
