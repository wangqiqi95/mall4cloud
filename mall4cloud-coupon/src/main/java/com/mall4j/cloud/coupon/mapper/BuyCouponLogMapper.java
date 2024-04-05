package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.BuyCouponLog;

import java.util.List;


/**
 * 优惠券购买记录
 *
 * @author shijing
 */

public interface BuyCouponLogMapper extends BaseMapper<BuyCouponLog> {
    List<BuyCouponLog> list(Long userId);

}
