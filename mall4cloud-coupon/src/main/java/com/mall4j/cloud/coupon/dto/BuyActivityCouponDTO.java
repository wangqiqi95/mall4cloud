package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

@Data
@ApiModel(description = "优惠券信息表")
public class BuyActivityCouponDTO implements Serializable {

    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

    @ApiModelProperty(value = "库存")
    private Integer stocks;

    @ApiModelProperty(value = "累计限领")
    private Integer limitNum;

    @ApiModelProperty(value = "每人每天限领")
    private Integer dailyLimitNum;

    @ApiModelProperty(value = "价格")
    private Long price;
}
