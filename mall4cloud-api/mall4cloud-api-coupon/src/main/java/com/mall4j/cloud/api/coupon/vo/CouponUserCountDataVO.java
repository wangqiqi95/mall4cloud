package com.mall4j.cloud.api.coupon.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户优惠券统计数据
 * @author: cl
 * @date: 2021-04-12 16:12
 */
public class CouponUserCountDataVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("未使用 优惠券的数量")
    private Integer couponUsableNums;

    @ApiModelProperty("已使用 优惠券的数量")
    private Integer couponUsedNums;

    @ApiModelProperty("已过期 优惠券的数量")
    private Integer couponExpiredNums;

    public Integer getCouponUsableNums() {
        return couponUsableNums;
    }

    public void setCouponUsableNums(Integer couponUsableNums) {
        this.couponUsableNums = couponUsableNums;
    }

    public Integer getCouponUsedNums() {
        return couponUsedNums;
    }

    public void setCouponUsedNums(Integer couponUsedNums) {
        this.couponUsedNums = couponUsedNums;
    }

    public Integer getCouponExpiredNums() {
        return couponExpiredNums;
    }

    public void setCouponExpiredNums(Integer couponExpiredNums) {
        this.couponExpiredNums = couponExpiredNums;
    }

    @Override
    public String toString() {
        return "CouponUserCountDataVO{" +
                "couponUsableNums=" + couponUsableNums +
                ", couponUsedNums=" + couponUsedNums +
                ", couponExpiredNums=" + couponExpiredNums +
                '}';
    }
}
