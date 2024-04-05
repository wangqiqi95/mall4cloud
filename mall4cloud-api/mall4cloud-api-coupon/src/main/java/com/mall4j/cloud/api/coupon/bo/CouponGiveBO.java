package com.mall4j.cloud.api.coupon.bo;

import java.util.List;

/**
 * 充值赠送优惠券信息
 * @author FrozenWatermelon
 */
public class CouponGiveBO {

    /**
     * 业务id
     */
    private Long bizId;

    /**
     * 业务类型
     */
    private Integer bizType;

    private Long userId;

    private List<CouponIdAndCountBO> couponInfos;

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CouponIdAndCountBO> getCouponInfos() {
        return couponInfos;
    }

    public void setCouponInfos(List<CouponIdAndCountBO> couponInfos) {
        this.couponInfos = couponInfos;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    @Override
    public String toString() {
        return "CouponGiveBO{" +
                "bizId=" + bizId +
                ", bizType=" + bizType +
                ", userId=" + userId +
                ", couponInfos=" + couponInfos +
                '}';
    }
}
