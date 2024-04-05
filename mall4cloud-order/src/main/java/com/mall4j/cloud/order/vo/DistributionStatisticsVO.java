package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/01/22
 */
@Data
public class DistributionStatisticsVO {

    @ApiModelProperty("业绩")
    private Long todaySales;

    @ApiModelProperty("分销订单支付金额")
    private Long payAmount;

    @ApiModelProperty("订单数量")
    private Integer orderNum;

    @ApiModelProperty("订单实际数量")
    private Integer orderActualNum;

    @ApiModelProperty("退单金额")
    private Long refundAmount;

    @ApiModelProperty("退单数量")
    private Integer refundNum;

    @ApiModelProperty("成功退单金额")
    private Long refundSuccessAmount;

    @ApiModelProperty("成功退单数量")
    private Integer refundSuccessNum;

    @ApiModelProperty("开单(导购/威客)数量")
    private Integer distributionUserNum;

    @ApiModelProperty("预计佣金")
    private Long expectedCommission;

    @ApiModelProperty("累计佣金")
    private Long totalCommission;

    @ApiModelProperty("威客总数")
    private Integer totalWitkeyNum;

    @ApiModelProperty("全部威客开单占比")
    private String totalWitkeyRate;

    @ApiModelProperty("新增威客数量")
    private Integer addWitkeyNum;

    @ApiModelProperty("新增威客开单占比")
    private String addWitkeyRate;

    @ApiModelProperty("新增会员")
    private Integer addUserNum;

    @ApiModelProperty("会员消费占比")
    private String userRate;

    @ApiModelProperty("会员消费贡献")
    private Long totalUserAmount;

}
