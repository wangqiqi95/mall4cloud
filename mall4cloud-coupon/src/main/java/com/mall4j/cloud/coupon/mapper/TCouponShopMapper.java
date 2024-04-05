package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.TCouponCommodity;
import com.mall4j.cloud.coupon.model.TCouponShop;
import com.mall4j.cloud.coupon.model.TCouponUser;

import java.util.List;

/**
 * 用户优惠券
 *
 * @author shijing
 */

public interface TCouponShopMapper extends BaseMapper<TCouponShop> {
    void insertBatch(List<TCouponShop> list);

}
