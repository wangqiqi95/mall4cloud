package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.TCouponCommodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 优惠券商品关联
 *
 * @author shijing
 */

public interface TCouponCommodityMapper extends BaseMapper<TCouponCommodity> {
    void insertBatch(List<TCouponCommodity> list);

    List<TCouponCommodity> getListByCouponIds(@Param("couponIds") List<Long> couponIds);

    List<TCouponCommodity> getListByCouponIdsAndSpuIds(@Param("couponIds") List<Long> couponIds,@Param("spuIds") List<Long> spuIds);

}
