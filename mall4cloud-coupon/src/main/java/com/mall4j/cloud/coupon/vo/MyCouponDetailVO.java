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
 * @date 2022-02-06
 */
@Data
@ApiModel(description = "用户优惠券详情信息")
public class MyCouponDetailVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "优惠券券码")
    private String couponCode;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

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

    @ApiModelProperty(value = "生效开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "生效结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "适用场景")
    private List<String> scene;

    @ApiModelProperty(value = "优惠券封面")
    private String coverUrl;

    @ApiModelProperty(value = "优惠券说明")
    private String description;

    @ApiModelProperty(value = "是否全部门店")
    private Boolean isAllShop;

    @ApiModelProperty(value = "适用门店数量")
    private Integer shopNum ;

    @ApiModelProperty(value = "适用门店")
    private List<Long> shops ;

    @ApiModelProperty("适用范围（0：不限/1：线上/2：线下）")
    private Short applyScopeType;

    @ApiModelProperty("优惠券来源信息（1：小程序添加/2：CRM同步优惠券）")
    private Short sourceType;



}
