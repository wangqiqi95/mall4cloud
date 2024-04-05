package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.Coupon;
import com.mall4j.cloud.coupon.model.PushCouponActivityCoupon;
import com.mall4j.cloud.coupon.vo.ActivityCouponVO;
import com.mall4j.cloud.coupon.vo.ActivityListVO;
import com.mall4j.cloud.coupon.vo.CouponDetailForShoppersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */

public interface PushActivityCouponMapper extends BaseMapper<PushCouponActivityCoupon> {

    void insertBatch(List<PushCouponActivityCoupon> list);

    List<ActivityCouponVO> couponList(Long activityId);

    /**
     * 导购端卡券详情
     */
    CouponDetailForShoppersVO couponDetail(Long id);

    /**
     * 更新优惠券库存
     */
    int updateCouponStocks(@Param("couponId") Long couponId,@Param("activityId") Long activityId,@Param("version") Integer version);

}
