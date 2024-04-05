package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.PushCouponActivityCoupon;
import com.mall4j.cloud.coupon.model.ReceiveCouponActivityCoupon;
import com.mall4j.cloud.coupon.vo.ActivityCouponVO;
import com.mall4j.cloud.coupon.vo.ActivityReportDetailVO;
import com.mall4j.cloud.coupon.vo.AppCouponVO;
import com.mall4j.cloud.coupon.vo.ReceiveActivityCouponVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */

public interface ReceiveActivityCouponMapper extends BaseMapper<ReceiveCouponActivityCoupon> {

    void insertBatch(List<ReceiveCouponActivityCoupon> list);

    List<ReceiveActivityCouponVO> couponList(Long activityId);

    List<AppCouponVO> appCouponList(Long activityId);

    int updateCouponStocks(@Param("couponId") Long couponId, @Param("activityId") Long activityId, @Param("version") Integer version);

}
