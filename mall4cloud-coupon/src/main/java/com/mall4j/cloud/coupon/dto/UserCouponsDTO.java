package com.mall4j.cloud.coupon.dto;

import java.util.List;

/**
 * 发放用户优惠券DTO
 *
 * @author YXF
 * @date 2020-12-08 17:22:57
 */
public class UserCouponsDTO {
    /**
     * 用户id列表
     */
    private List<Long> userIds;
    /**
     * 发放给用户优惠券的数量
     */
    private List<SendCoupon> sendCoupons;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public List<SendCoupon> getSendCoupons() {
        return sendCoupons;
    }

    public void setSendCoupons(List<SendCoupon> sendCoupons) {
        this.sendCoupons = sendCoupons;
    }

    @Override
    public String toString() {
        return "UserCouponsDTO{" +
                "userIds=" + userIds +
                ", sendCoupons=" + sendCoupons +
                '}';
    }

    public static class SendCoupon{
        /**
         * 优惠券id
         */
        private Long couponId;
        /**
         * 发放给用户优惠券的数量
         */
        private Integer nums;

        public Long getCouponId() {
            return couponId;
        }

        public void setCouponId(Long couponId) {
            this.couponId = couponId;
        }

        @Override
        public String toString() {
            return "SendCoupon{" +
                    "couponId=" + couponId +
                    ", nums=" + nums +
                    '}';
        }

        public Integer getNums() {
            return nums;
        }

        public void setNums(Integer nums) {
            this.nums = nums;
        }
    }
}
