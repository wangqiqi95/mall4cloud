package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.GoodsCouponActivity;
import com.mall4j.cloud.coupon.model.GoodsCouponActivityCommodity;
import com.mall4j.cloud.coupon.model.GoodsCouponActivityShop;

import java.util.List;

/**
 * 商详领券
 *
 * @author shijing
 */

public interface GoodsActivityCommodityMapper extends BaseMapper<GoodsCouponActivityCommodity> {
    void insertBatch(List<GoodsCouponActivityCommodity> list);


}
