package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CouponPackListVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;
    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    /**
     * 活动截止时间
     */
    @ApiModelProperty("活动截止时间")
    private Date activityEndTime;

    @ApiModelProperty("0否 1是")
    private Integer isAllShop;
    /**
     * 适用门店集合 -1为全部门店
     */
    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;

    /**
     * 初始优惠券包库存
     */
    @ApiModelProperty("初始优惠券包库存")
    private Integer initialCouponStock;

    /**
     * 当前优惠券包库存
     */
    @ApiModelProperty("当前优惠券包库存")
    private Integer curCouponStock;

    /**
     * 优惠券集合
     */
    @ApiModelProperty("优惠券集合")
    private String couponIds;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 权重
     */
    @ApiModelProperty("权重")
    private Integer weight;

    /**
     * 活动状态
     */
    @ApiModelProperty("活动状态")
    private Integer status;

    @ApiModelProperty("create_time")
    private Date createTime;
    @ApiModelProperty("create_user_name")
    private String createUserName;
}
