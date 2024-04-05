package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.BuyCouponActivityCoupon;
import com.mall4j.cloud.coupon.model.PushCouponActivityCoupon;
import com.mall4j.cloud.coupon.model.ReceiveCouponActivityCoupon;
import com.mall4j.cloud.coupon.vo.ActivityCouponVO;
import com.mall4j.cloud.coupon.vo.AppBuyCouponDetailVO;
import com.mall4j.cloud.coupon.vo.AppBuyCouponVO;
import com.mall4j.cloud.coupon.vo.BuyActivityCouponVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */

public interface BuyActivityCouponMapper extends BaseMapper<BuyCouponActivityCoupon> {

    void insertBatch(List<BuyCouponActivityCoupon> list);

    List<BuyActivityCouponVO> couponList(Long activityId);

    List<AppBuyCouponVO> appCouponList(Long id);

    AppBuyCouponDetailVO appCouponDetail(Long id);

    int updateCouponStocks(@Param("couponId") Long couponId, @Param("activityId") Long activityId, @Param("version") Integer version);

}
