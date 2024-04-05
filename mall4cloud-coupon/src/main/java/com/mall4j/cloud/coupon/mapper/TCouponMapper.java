package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.coupon.dto.QueryCrmIdsDTO;
import com.mall4j.cloud.coupon.dto.CouponListDTO;
import com.mall4j.cloud.coupon.model.TCoupon;
import com.mall4j.cloud.coupon.vo.CouponListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 */

public interface TCouponMapper extends BaseMapper<TCoupon> {

    List<CouponListVO> list(CouponListDTO param);

    List<TCoupon> selectCouponByIds(@Param("ids") List<Long> ids);

    List<String> queryCrmIds(QueryCrmIdsDTO param);
}
