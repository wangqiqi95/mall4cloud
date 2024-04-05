package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.BuyCouponActivityShop;
import com.mall4j.cloud.coupon.model.PushCouponActivityShop;
import com.mall4j.cloud.coupon.model.ReceiveCouponActivityShop;

import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */

public interface ReceiveActivityShopMapper extends BaseMapper<ReceiveCouponActivityShop> {

    void insertBatch(List<ReceiveCouponActivityShop> list);


}
