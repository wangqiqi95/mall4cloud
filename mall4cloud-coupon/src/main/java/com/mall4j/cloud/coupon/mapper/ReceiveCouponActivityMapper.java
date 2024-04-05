package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.ActivityListDTO;
import com.mall4j.cloud.coupon.model.PushCouponActivity;
import com.mall4j.cloud.coupon.model.ReceiveCouponActivity;
import com.mall4j.cloud.coupon.vo.ActivityListVO;
import com.mall4j.cloud.coupon.vo.ActivityReportDetailVO;
import com.mall4j.cloud.coupon.vo.AppReceiveActivityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 领券活动
 *
 * @author shijing
 */

public interface ReceiveCouponActivityMapper extends BaseMapper<ReceiveCouponActivity> {

    List<ActivityListVO> list(ActivityListDTO param);

    AppReceiveActivityVO appActivity(@Param("shopId") Long shopId);

    List<ActivityReportDetailVO> activityReportDetail(@Param("activityId") Long activityId,
                                                      @Param("couponInfo")String couponInfo,
                                                      @Param("activitySource")Integer activitySource);


}
