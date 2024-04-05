package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreParamDTO;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.coupon.model.TCouponActivityCentre;
import java.util.List;

/**
 * 优惠券关联活动
 *
 * @author gmq
 * @date 2022-10-17 11:20:21
 */
public interface TCouponActivityCentreMapper extends BaseMapper<TCouponActivityCentre> {

    List<CouponListVO> couponList(TCouponActivityCentreParamDTO param);

}
