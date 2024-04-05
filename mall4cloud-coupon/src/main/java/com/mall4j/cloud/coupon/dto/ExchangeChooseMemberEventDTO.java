package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExchangeChooseMemberEventDTO {

    @NotNull(message = "兑换个必填")
    @Min(value = 1,message = "兑换个数不能小于1")
    @ApiModelProperty(value = "兑换个数")
    private Integer exchangeNum;

    @NotNull(message = "活动ID为必填项")
    @ApiModelProperty(value = "活动ID")
    private Long eventId;

    @NotNull(message = "当前微信绑定电话为必传项")
    @ApiModelProperty(value = "当前微信绑定电话")
    private String mobile;

    @ApiModelProperty(value = "收货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "收货电话")
    private String deliveryMobile;

    @ApiModelProperty(value = "所属店铺ID")
    private Long belongShopId;

    @ApiModelProperty(value = "所属商铺名称")
    private String belongShopName;

    @ApiModelProperty(value = "所属商铺编码")
    private String belongShopCode;

    //deliveryStatus 快递状态

    //expressCompany 快递公司

    //trackingNumber 查询单号
}
