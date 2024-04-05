package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商城小程序商详领券
 * @author shijing
 * @date 2022-02-27
 */
@Data
@ApiModel(description = "商品优惠券详情信息")
public class AppGoodsActivityVO {

    @ApiModelProperty(value = "领券活动id")
    private Long id;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
    private Short kind;

    @ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
    private Short type;

    @ApiModelProperty(value = "抵用金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty(value = "折扣力度")
    private BigDecimal couponDiscount;

    @ApiModelProperty(value = "金额限制类型（0：不限/1：满额）")
    private Short amountLimitType;

    @ApiModelProperty(value = "限制金额")
    private BigDecimal amountLimitNum;

    @ApiModelProperty(value = "生效时间类型（1：固定时间/2：领取后生效）")
    private int timeType;

    @ApiModelProperty(value = "生效开始时间")
    private Date startTime;

    @ApiModelProperty(value = "生效结束时间")
    private Date endTime;

    @ApiModelProperty(value = "领券后x天起生效")
    private Integer afterReceiveDays;

    @ApiModelProperty(value = "每人限制兑换总数")
    private Long personMaxAmount;

    @ApiModelProperty(value = "用户是否已经领取")
    private Boolean receiveStatus = true;

}
