package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.SecurityDefinition;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 活动优惠券详情信息
 * @author shijing
 * @date 2022-01-12
 */
@Data
@ApiModel(description = "导购端卡券详情")
public class CouponDetailForShoppersVO implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "卡券id")
    private Long couponId;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "卡券名称")
    private String name;

    @ApiModelProperty(value = "卡券类型（0：抵用券/1：折扣券）")
    private Short type;

    @ApiModelProperty(value = "抵用金额")
    private Long reduceAmount;

    @ApiModelProperty(value = "折扣力度")
    private BigDecimal couponDiscount;

    @ApiModelProperty(value = "生效时间类型（1：固定时间/2：领取后生效）")
    private Short timeType;

    @ApiModelProperty("领券后X天起生效")
    private Integer afterReceiveDays;

    @ApiModelProperty(value = "生效开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "生效结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "金额限制类型（0：不限/1：满额）")
    private Short amountLimitType;

    @ApiModelProperty(value = "限制金额")
    private Integer amountLimitNum;

    @ApiModelProperty(value = "商品限制类型（0：不限/1：不超过/2：不少于）")
    private Short commodityLimitType;

    @ApiModelProperty(value = "商品限制件数")
    private Integer commodityLimitNum;

    @ApiModelProperty(value = "适用商品类型（0：不限/1：指定商品）")
    private Short commodityScopeType;

    @ApiModelProperty(value = "使用范围（0：不限/1：线上/2：线下）")
    private Short applyScopeType;

    @ApiModelProperty(value = "库存")
    private Integer stocks;

    @ApiModelProperty(value = "领取数量")
    private Integer receiveAmount;

    @ApiModelProperty(value = "剩余数量")
    private Integer leaveAmount;

    @ApiModelProperty(value = "累计限领")
    private Integer limitNum;

    @ApiModelProperty(value = "优惠券描述")
    private String description;

    @ApiModelProperty(value = "领券状态 0:未领取 1:已领取")
    private Integer receiveStatus = 0;


}
