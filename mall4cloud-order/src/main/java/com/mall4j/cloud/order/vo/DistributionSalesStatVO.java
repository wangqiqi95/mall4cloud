package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DistributionSalesStatVO {

    @ApiModelProperty(value = "今日业绩")
    private Long todaySales;
    @ApiModelProperty(value = "累计业绩")
    private Long totalSales;
    @ApiModelProperty("当月销售额")
    private Long monthSales;

    @ApiModelProperty("今日分销业绩")
    private Long distributionTodaySales;

    @ApiModelProperty("当月分销业绩")
    private Long distributionMonthSales;

    @ApiModelProperty("累计分销业绩")
    private Long distributionTotalSales;

    @ApiModelProperty("今日发展业绩")
    private Long developingTodaySales;

    @ApiModelProperty("当月发展业绩")
    private Long developingMonthSales;

    @ApiModelProperty("累计发展业绩")
    private Long developingTotalSales;

    @ApiModelProperty("门店排名")
    private Integer storeRanking;

    @ApiModelProperty("全区排名")
    private Integer orgRanking;

    @ApiModelProperty("全国排名")
    private Integer allRanking;

    @ApiModelProperty("订单数")
    private Integer orderNum;

    @ApiModelProperty("退单成功金额")
    private Long refundAmount;

    @ApiModelProperty("退单成功数量")
    private Integer refundNum;

    @ApiModelProperty("下单会员人数")
    private Integer userNum;

    @ApiModelProperty("开单导购")
    private Integer buyStaffNum;

    @ApiModelProperty("客单价")
    private Long customerOrderPrice;

    @ApiModelProperty("客单量")
    private Integer customerOrderNum;

    @ApiModelProperty("折扣率")
    private String discountRate;

    @ApiModelProperty("累计佣金")
    private Long totalCommission;

    @ApiModelProperty("当月佣金")
    private Long monthCommission;

    @ApiModelProperty("当月常规佣金")
    private Long commonCommission;

    @ApiModelProperty("当月团购佣金")
    private Long groupCommission;

}
