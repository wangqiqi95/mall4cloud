package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.CodeListDTO;
import com.mall4j.cloud.coupon.model.TCouponCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 优惠券码关联
 *
 * @author shijing
 */

public interface TCouponCodeMapper extends BaseMapper<TCouponCode> {
    void insertBatch(List<TCouponCode> list);

    List<TCouponCode> list(CodeListDTO param);

    int receive(@Param("id") Long id);

    TCouponCode getLimitOne(@Param("couponId") Long couponId,@Param("status") Integer status);


}
