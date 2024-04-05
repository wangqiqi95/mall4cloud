package com.mall4j.cloud.api.coupon.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 选择优惠券
 * @author FrozenWatermelon
 * @date 2020/12/17
 */
public class BindCouponDTO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "优惠券id数组")
    private List<Long> couponIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<Long> couponIds) {
        this.couponIds = couponIds;
    }

    @Override
    public String toString() {
        return "BindCouponDTO{" +
                "userId=" + userId +
                ", couponIds=" + couponIds +
                '}';
    }
}
