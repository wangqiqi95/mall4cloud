package com.mall4j.cloud.api.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 同步积分兑换优惠券dto
 * @Author axin
 * @Date 2023-02-10 14:36
 **/
@Data
public class SyncPointConvertCouponDto {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "批次id")
    private Long batchId;

    @ApiModelProperty(value = "小程序优惠券项目ID")
    private Long xcxCouponId;

    @ApiModelProperty(value = "小程序优惠券项目名称")
    private String xcxCouponName;

    @ApiModelProperty(value = "CRM券ID")
    private Long crmCouponId;

    @ApiModelProperty(value = "CRM券名称")
    private String crmCouponName;

    @ApiModelProperty(value = "券码")
    private String couponCode;
}
