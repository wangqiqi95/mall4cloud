package com.mall4j.cloud.api.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "买券支付回调")
public class PayCouponDTO {

    @ApiModelProperty("订单编号")
    private Long orderNo;

    @ApiModelProperty("微信支付编号")
    private String wechatPayNo;

}
