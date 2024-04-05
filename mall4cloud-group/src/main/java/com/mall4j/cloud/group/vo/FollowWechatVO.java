package com.mall4j.cloud.group.vo;

import com.mall4j.cloud.group.model.FollowWechatActivityShop;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class FollowWechatVO implements Serializable {
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
    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;

    @ApiModelProperty("微信公众号id集合")
    private String wechatIds;

    @ApiModelProperty("领取条件 1 关注所有 2 关注任意一个")
    private Integer receiveCondition;

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
     * 赠送优惠券id集合
     */
    @ApiModelProperty("赠送优惠券id集合")
    private String activityCouponIds;

    /**
     * 权重
     */
    @ApiModelProperty("权重")
    private Integer weight;

    /**
     * 活动状态 0 禁用 1启用
     */
    @ApiModelProperty("活动状态 0 禁用 1启用")
    private Integer status;

    @ApiModelProperty("适用门店列表")
    private List<FollowWechatActivityShop> shops;

}
