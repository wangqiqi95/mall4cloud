package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class CouponActivityDetailVO {

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "领券时间")
    private Date receiveTime;

    @ApiModelProperty(value = "领取门店ID")
    private Long shopId;

    @ApiModelProperty(value = "领取门店编码")
    private String shopCode;

    @ApiModelProperty(value = "领取门店名称")
    private String shopName;

    @ApiModelProperty(value = "核销人id")
    private Long writeOffUserId;

    @ApiModelProperty(value = "核销人名称")
    private String writeOffUserName;

    @ApiModelProperty(value = "核销人工号")
    private String writeOffUserCode;

    @ApiModelProperty(value = "核销人手机号")
    private String writeOffUserPhone;

    @ApiModelProperty(value = "核销门店id")
    private Long writeOffShopId;

    @ApiModelProperty(value = "     * 核销门店名称\n")
    private String writeOffShopName;

    @ApiModelProperty(value = "核销时间")
    private Timestamp writeOffTime;

    @ApiModelProperty(value = "发放人id")
    private Long createId;

    @ApiModelProperty(value = "发放人名称")
    private String createName;

    @ApiModelProperty(value = "发放人手机号")
    private String createPhone;

    @ApiModelProperty(value = "发放人工号")
    private String createNo;

    /**
     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
     */
    @ApiModelProperty(value = "优惠券来源信息： 1：小程序添加/2：CRM同步优惠券")
    private Integer couponSourceType;

}
