package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 限时调价活动DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:13
 */
@Data
public class TimeLimitedDiscountActivityDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Integer id;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("是否全部门店 0 否 1是")
    private Integer isAllShop;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("状态 0 未启用 1已启用")
    private Integer status;

    @ApiModelProperty("门店列表")
    private List<Long> shopIds;

    @ApiModelProperty("spu优惠列表")
    private List<TimeLimitedDiscountSpuDTO> spus;

    @ApiModelProperty("类型1，限时调价。2，会员日活动调价 3，虚拟门店价")
    private Integer type;

    @ApiModelProperty("是否允许同时使用优惠券0否1是，3:指定券使用")
    private Integer canUseCoupon;

    @ApiModelProperty("是否同时参加满减则活动0否1是")
    private Integer canUseDiscount;

    @ApiModelProperty("适用优惠券列表")
    private List<Long> couponIds;


}
