package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.CouponPackPageDTO;
import com.mall4j.cloud.coupon.dto.CouponPackSelectDTO;
import com.mall4j.cloud.coupon.model.CouponPackActivity;
import com.mall4j.cloud.coupon.vo.ActivityReportDetailVO;
import com.mall4j.cloud.coupon.vo.CouponPackListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponPackActivityMapper extends BaseMapper<CouponPackActivity> {
    void addStock(@Param("id") Integer id, @Param("stock") Integer stock);

    void reduceStock(@Param("id") Integer id);

    List<CouponPackListVO> couponPackList(CouponPackPageDTO param);

    List<CouponPackListVO> couponPackActivityList(CouponPackSelectDTO param);

    ActivityReportDetailVO activityReportDetail(@Param("activityId") Long activityId,
                                                      @Param("couponInfo")String couponInfo,
                                                      @Param("activitySource")Integer activitySource,
                                                @Param("couponId")String couponId);
}
