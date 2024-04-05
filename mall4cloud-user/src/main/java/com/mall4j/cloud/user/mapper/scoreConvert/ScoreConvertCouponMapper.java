package com.mall4j.cloud.user.mapper.scoreConvert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvertCoupon;
import com.mall4j.cloud.user.model.scoreConvert.ScoreConvertShop;

import java.util.List;

/**
 * 积分兑换优惠券
 *
 * @author shijing
 * @date 2022-01-29
 */

public interface ScoreConvertCouponMapper extends BaseMapper<ScoreConvertCoupon> {
    void insertBatch(List<ScoreConvertCoupon> list);

}
