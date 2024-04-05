package com.mall4j.cloud.api.docking.skq_erp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description 积分抵现排行榜
 * @Author axin
 * @Date 2023-02-15 16:17
 **/
@Data
public class StoreIntegralRankRespDto {
    @ApiModelProperty(value = "销售月份")
    private Date salesMonth;

    @ApiModelProperty(value = "门店编号")
    private String shopCode;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店会员订单总数")
    private Integer vipOrder;

    @ApiModelProperty(value = "积分抵现单数")
    private Integer amountUsed;

    @ApiModelProperty(value = "积分抵现占比")
    private Double usageRate=0D;

    @ApiModelProperty(value = "使用占比排名")
    private Integer rank;
}
