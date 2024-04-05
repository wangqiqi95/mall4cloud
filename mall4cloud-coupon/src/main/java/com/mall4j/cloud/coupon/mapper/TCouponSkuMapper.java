package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.model.TCouponCommodity;
import com.mall4j.cloud.coupon.model.TCouponSku;
import com.mall4j.cloud.coupon.model.TCouponSpu;
import com.mall4j.cloud.coupon.model.TCouponSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 优惠券商品关联
 *
 */
public interface TCouponSkuMapper extends BaseMapper<TCouponSku> {

    void insertBatch(List<TCouponSku> list);

    List<TCouponSku> getListByCouponIds(@Param("couponIds") List<Long> couponIds);
}
