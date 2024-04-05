package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/02/19
 */
@Data
public class DistributionStoreStatisticsVO {

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("业绩")
    private Long sales;

    @ApiModelProperty("订单数量")
    private Integer orderNum;

    @ApiModelProperty("退单数量")
    private Integer refundNum;

    @ApiModelProperty("开单导购人数")
    private Integer staffNum;

}
