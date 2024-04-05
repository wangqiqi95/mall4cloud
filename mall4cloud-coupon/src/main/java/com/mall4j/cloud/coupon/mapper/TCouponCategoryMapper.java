package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.TCouponCategory;
import com.mall4j.cloud.coupon.model.TCouponShop;

import java.util.List;

/**
 * 用户优惠券
 *
 * @author shijing
 */

public interface TCouponCategoryMapper extends BaseMapper<TCouponCategory> {
    void insertBatch(List<TCouponCategory> list);

}
