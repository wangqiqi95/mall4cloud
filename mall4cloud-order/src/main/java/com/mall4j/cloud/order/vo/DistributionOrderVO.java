package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DistributionOrderVO {

    @ApiModelProperty(value = "订单号")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "订单项")
    private List<DistributionOrderItemVO> orderItems;

    @ApiModelProperty("分销佣金状态 0-待结算 1-已结算待提现 2-已结算已提现")
    private Integer distributionStatus;

    @ApiModelProperty("订单创建时间")
    private Date createTime;

    @ApiModelProperty(value = "分销佣金")
    private Long distributionAmount;

    @ApiModelProperty(value = "发展佣金")
    private Long developingAmount;

    @ApiModelProperty("实际总值")
    private Long actualTotal;

    @ApiModelProperty("总值")
    private Long total;

    @ApiModelProperty("分销用户类型 1-导购 2-微客")
    private Long distributionUserType;

}
