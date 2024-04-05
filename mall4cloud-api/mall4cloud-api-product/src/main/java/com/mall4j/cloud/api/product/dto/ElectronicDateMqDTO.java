package com.mall4j.cloud.api.product.dto;

import lombok.Data;

import java.util.List;

/**
 * @Date 2022年4月21日, 0021 21:38
 * @Created by eury
 */
@Data
public class ElectronicDateMqDTO {

    /**
     * 1:同步商品 2:门店开关开启价签同步
     */
    private int mqType=1;

    /**
     * 门店集合
     */
    private List<Long> storeIds;

    /**
     * 商品集合
     */
    private List<Long> spuIds;

}
