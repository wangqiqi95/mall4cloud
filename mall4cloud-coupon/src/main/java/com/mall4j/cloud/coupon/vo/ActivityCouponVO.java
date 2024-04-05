package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 活动优惠券详情信息
 * @author shijing
 * @date 2022-01-05
 */
@Data
@ApiModel(description = "活动详情信息")
public class ActivityCouponVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "优惠券id")
    private String code;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
    private Short kind;

    @ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
    private Short type;

    @ApiModelProperty(value = "抵用金额")
    private BigDecimal reduceAmount;

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

    @ApiModelProperty(value = "优惠券状态")
    private String status;

    @ApiModelProperty(value = "库存")
    private Integer stocks;

    @ApiModelProperty(value = "累计限领")
    private Integer limitNum;

    @ApiModelProperty(value = "每人每天限领")
    private Integer dailyLimitNum;

}
