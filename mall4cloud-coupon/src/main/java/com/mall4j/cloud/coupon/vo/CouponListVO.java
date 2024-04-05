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
 * 优惠券列表信息 *
 * @author shijing
 * @date 2022-01-03 14:55:56
 */
@Data
@ApiModel(description = "优惠券列表信息")
public class CouponListVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "优惠券id")
    private String code;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "优惠券种类（0：普通优惠券/1：包邮券/2：券码导入/3：企业券）")
    private Short kind;

    @ApiModelProperty(value = "优惠券类型（0：抵用券/1：折扣券）")
    private Short type;

    @ApiModelProperty(value = "折扣券最大抵扣金额")
    private BigDecimal maxDeductionAmount;

    @ApiModelProperty(value = "抵用金额")
    private BigDecimal reduceAmount;

    @ApiModelProperty(value = "折扣力度")
    private BigDecimal couponDiscount;

    @ApiModelProperty(value = "生效开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "生效结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "优惠券状态（0：启用/1：禁用）")
    private int status;

    @ApiModelProperty("优惠券来源信息（1：小程序添加/2：CRM同步优惠券）")
    private Short sourceType;

    @ApiModelProperty("CRM同步优惠券的id")
    private String crmCouponId;

    @ApiModelProperty("优惠券来源信息（1：小程序添加/2：CRM同步优惠券）")
    private List<String> scene;

    @ApiModelProperty("是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty("适用范围（0：不限/1：线上/2：线下）")
    private Short applyScopeType;

    @ApiModelProperty("生效时间类型（1：固定时间/2：领取后生效）")
    private int timeType;

    @ApiModelProperty("领券后x天起生效")
    private Integer afterReceiveDays;


}
