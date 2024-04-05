package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.ActivityListDTO;
import com.mall4j.cloud.coupon.model.PushCouponActivity;
import com.mall4j.cloud.coupon.vo.ActivityListVO;
import com.mall4j.cloud.coupon.vo.ActivityReportDetailVO;
import com.mall4j.cloud.coupon.vo.CouponForShoppersListVO;
import com.mall4j.cloud.coupon.vo.CouponForShoppersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */

public interface PushCouponActivityMapper extends BaseMapper<PushCouponActivity> {

    List<ActivityListVO> list(ActivityListDTO param);

    List<Long> shoppersActivity(@Param("shopId") Long shopId);

    List<CouponForShoppersVO> listForShoppers(@Param("activityIds") List<Long> activityIds, @Param("type") Integer type);

    List<ActivityReportDetailVO> activityReportDetail(@Param("activityId") Long activityId,
                                                      @Param("couponInfo")String couponInfo,
                                                      @Param("activitySource")Integer activitySource);

}
