package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FollowWechatListVO implements Serializable {
    private Integer id;

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

    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;
    @ApiModelProperty("适用门店数")
    private Integer applyShopNum;
    /**
     * 赠送积分开关
     */
    @ApiModelProperty("赠送积分开关")
    private Integer activityPointSwitch;

    @ApiModelProperty("赠送积分数")
    private Integer activityPointNumber;

    /**
     * 赠送优惠券开关
     */
    @ApiModelProperty("赠送优惠券开关")
    private Integer activityCouponSwitch;

    /**
     * 赠送优惠券id集合
     */
    @ApiModelProperty("赠送优惠券id集合")
    private String activityCouponIds;

    @ApiModelProperty("赠送优惠券数量")
    private Integer activityCouponNum;

    @ApiModelProperty("奖励人数")
    private int awardNum;
    /**
     * 权重
     */
    @ApiModelProperty("权重")
    private Integer weight;

    /**
     * 活动状态 0 禁用 1启用
     */
    @ApiModelProperty("活动状态 0 禁用 1启用 2进行中 3 未开始 4已结束")
    private int status;

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
