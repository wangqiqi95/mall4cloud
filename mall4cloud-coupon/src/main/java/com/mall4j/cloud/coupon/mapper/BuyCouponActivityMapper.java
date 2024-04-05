package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.ActivityListDTO;
import com.mall4j.cloud.coupon.model.BuyCouponActivity;
import com.mall4j.cloud.coupon.vo.ActivityListVO;
import com.mall4j.cloud.coupon.vo.ActivityReportDetailVO;
import com.mall4j.cloud.coupon.vo.AppBuyActivityDetailVO;
import com.mall4j.cloud.coupon.vo.AppBuyActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 领券活动
 *
 * @author shijing
 */

public interface BuyCouponActivityMapper extends BaseMapper<BuyCouponActivity> {

    List<ActivityListVO> list(ActivityListDTO param);

    List<AppBuyActivityVO> appList(Long shopId);

    List<ActivityReportDetailVO> activityReportDetail(@Param("activityId") Long activityId,
                                                      @Param("couponInfo")String couponInfo,
                                                      @Param("activitySource")Integer activitySource);

}
