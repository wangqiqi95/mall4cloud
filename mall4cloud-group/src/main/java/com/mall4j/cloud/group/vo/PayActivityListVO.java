package com.mall4j.cloud.group.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "支付有礼活动列表实体")
public class PayActivityListVO implements Serializable {
    /**
     * id
     */
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

    /**
     * 适用门店id集合 -1为全部门店
     */
    @ApiModelProperty("是否全部门店 0否 1是")
    private Integer isAllShop;
    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;


    /**
     * 赠送积分开关
     */
    @ApiModelProperty("赠送积分开关")
    private Integer activityPointSwitch;

    /**
     * 赠送积分数
     */
    @ApiModelProperty("赠送积分数")
    private Integer activityPointNumber;

    /**
     * 赠送优惠券开关
     */
    @ApiModelProperty("赠送优惠券开关")
    private Integer activityCouponSwitch;

    /**
     * 赠送优惠券id
     */
    @ApiModelProperty("赠送优惠券id")
    private String activityCouponId;

    @ApiModelProperty("赠送优惠券名称")
    private String activityCouponName;

    /**
     * 权重
     */
    @ApiModelProperty("权重")
    private Integer weight;

    /**
     * 活动状态 0 禁用 1启用
     */
    @ApiModelProperty("活动状态 0 禁用 1启用 2进行中 3 未开始 4已结束")
    private Integer status;


    @ApiModelProperty("参与金额")
    private BigDecimal participationAmount;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;


    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;
}
