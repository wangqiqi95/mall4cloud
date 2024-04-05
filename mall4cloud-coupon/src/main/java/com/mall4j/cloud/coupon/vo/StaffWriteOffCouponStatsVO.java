package com.mall4j.cloud.coupon.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class StaffWriteOffCouponStatsVO {

    @ApiModelProperty("核销会员数量")
    private Long writeOffUsers = 0l;
    @ApiModelProperty("核销券数量")
    private Long writeOffCoupons = 0l;
    @ApiModelProperty("核销券金额")
    private Long writeOffAmount = 0l;

}
