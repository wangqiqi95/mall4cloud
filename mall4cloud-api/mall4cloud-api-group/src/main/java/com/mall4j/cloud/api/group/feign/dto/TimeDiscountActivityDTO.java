package com.mall4j.cloud.api.group.feign.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 限时调价活动
 *
 * @luzhengxiang
 * @create 2022-03-12 4:11 PM
 **/
@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class TimeDiscountActivityDTO {
    @ApiModelProperty("门店id")
    private Long shopId;

    @ApiModelProperty("商品id")
    private List<Long> spuId;

    @ApiModelProperty("类型1，限时调价。2，会员日活动调价 3，虚拟门店价")
    private Integer type;

    @ApiModelProperty("是否门店无库存")
    private boolean noStoreStock=false;

    @ApiModelProperty("skus")
    private List<Long> skuIds=new ArrayList<>();

    @ApiModelProperty("门店无库存sku(门店无库存取官店价格，官店有限时调价价格优先取)")
    private List<Long> skuStoreNoStockIds=new ArrayList<>();


}
