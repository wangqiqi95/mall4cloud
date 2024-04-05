package com.mall4j.cloud.group.vo;


import com.mall4j.cloud.group.model.PayActivityShop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "支付有礼活动详情实体")
public class PayActivityVO implements Serializable {
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动图片url
     */
    @ApiModelProperty("活动图片url")
    private String activityPicUrl;

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

    /**
     * 消费金额
     */
    @ApiModelProperty("消费金额")
    private BigDecimal consumptionAmount;

    /**
     * 适用商品范围
     */
    @ApiModelProperty("适用商品范围")
    private String applyCommodityIds;

    /**
     * 每人每天最大参与订单数
     */
    @ApiModelProperty("每人每天最大参与订单数")
    private Integer dayMaxOrderNum;

    /**
     * 订单最低累计数
     */
    @ApiModelProperty("订单最低累计数")
    private Integer minOrderTotalNum;

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
    private List<PayActivityShop> shops;
}
