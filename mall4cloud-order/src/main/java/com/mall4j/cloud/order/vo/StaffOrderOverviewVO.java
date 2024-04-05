package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("导购订单概况信息VO")
public class StaffOrderOverviewVO {
    /**
     * 今日销售收入
     */
    @ApiModelProperty(value = "今日/本期 支付金额")
    private Double payActual;
    /**
     * 今日支付客户数
     */
    @ApiModelProperty(value = "今日/本期 支付客户数")
    private Integer payUserCount;
    /**
     * 今日支付订单数
     */
    @ApiModelProperty(value = "今日/本期 支付订单数")
    private Integer payOrderCount;
    /**
     * 今日客单价
     */
    @ApiModelProperty(value = "今日/本期 客单价")
    private Double onePrice;
    /**
     * 支付总件数
     */
    @ApiModelProperty(value = "今日/本期 支付总件数")
    private Integer spuCount;

    /**
     * 件单价
     */
    @ApiModelProperty(value = "今日/本期 件单价")
    private Double spuPrice;

    /**
     * 连带率
     */
    @ApiModelProperty(value = "今日/本期 连带率")
    private Double liandai;

    @ApiModelProperty(value = "今日/本期 实收金额")
    private Double needApplyAmount;

    @ApiModelProperty(value = "今日/本期 退款成功金额")
    private Double needRefundAmount;




    //本月销售看板
    /**
     * 本月销售收入
     */
    @ApiModelProperty(value = "本月支付金额")
    private Double currentMonthPayActual;
    /**
     * 本月支付客户数
     */
    @ApiModelProperty(value = "本月支付客户数")
    private Integer currentMonthPayUserCount;
    /**
     * 本月支付订单数
     */
    @ApiModelProperty(value = "本月支付订单数")
    private Integer currentMonthPayOrderCount;
    /**
     * 本月客单价
     */
    @ApiModelProperty(value = "本月客单价")
    private Double currentMonthOnePrice;

    @ApiModelProperty(value = "本月 实收金额")
    private Double currentMonthNeedApplyAmount;

    @ApiModelProperty(value = "本月 退款成功金额")
    private Double currentMonthNeedRefundAmount;





    /**
     * 支付金额列表
     */
    @ApiModelProperty(value = "支付金额列表")
    private List<Double> payActualList;

    /**
     * 时间日期列表
     */
    @ApiModelProperty(value = "时间日期列表")
    private List<String> dateToStringList;




}
