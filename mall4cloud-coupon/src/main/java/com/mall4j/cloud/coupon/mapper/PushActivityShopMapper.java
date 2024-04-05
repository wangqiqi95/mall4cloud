package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.PushCouponActivityShop;

import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */

public interface PushActivityShopMapper extends BaseMapper<PushCouponActivityShop> {

    void insertBatch(List<PushCouponActivityShop> list);


}
