package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.GoodsCouponActivity;
import com.mall4j.cloud.coupon.model.GoodsCouponActivityShop;
import com.mall4j.cloud.coupon.model.TCouponShop;

import java.util.List;

/**
 * 商详领券
 *
 * @author shijing
 */

public interface GoodsActivityShopMapper extends BaseMapper<GoodsCouponActivityShop> {
    void insertBatch(List<GoodsCouponActivityShop> list);

}
