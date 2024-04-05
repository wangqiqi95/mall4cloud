package com.mall4j.cloud.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StaffReceiveCouponStatsVO {

    @ApiModelProperty("已领券未核销")
    private Long notWriteOffCoupons = 0l;
    @ApiModelProperty("已领券已核销")
    private Long writeOffCoupons = 0l;
    @ApiModelProperty("核销率")
    private BigDecimal writeOffRate = new BigDecimal("0");


}
