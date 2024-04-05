package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/02/13
 */
@Data
public class PurchaseRankingDTO {

    @ApiModelProperty("商品ID")
    private Long spuId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("最小价格")
    private Long minPrice;

    @ApiModelProperty("最大价格")
    private Long maxPrice;

    @ApiModelProperty("商品介绍主图")
    private String mainImgUrl;

    @ApiModelProperty("本月加购人数")
    private Integer purchaseUserNum;

}
