package com.mall4j.cloud.coupon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券详情信息
 * @author shijing
 * @date 2022-03-19
 */
@Data
@ApiModel(description = "核销优惠券详情信息")
public class WriteOffDetailVO {
    @ApiModelProperty(value = "用户优惠券id")
    private Long id;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "优惠券券码")
    private String couponCode;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
    private Short kind;

    @ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
    private Short type;

    @ApiModelProperty(value = "抵用金额")
    private Long reduceAmount;

    @ApiModelProperty(value = "折扣力度")
    private Integer couponDiscount;

    @ApiModelProperty(value = "金额限制类型（0：不限/1：满额）")
    private Short amountLimitType;

    @ApiModelProperty(value = "限制金额")
    private BigDecimal amountLimitNum;

    @ApiModelProperty(value = "生效时间类型（1：固定时间/2：领取后生效）")
    private int timeType;

    @ApiModelProperty(value = "领券后x天起生效")
    private Integer afterReceiveDays;

    @ApiModelProperty(value = "生效开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "生效结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "优惠券说明")
    private String description;

}
