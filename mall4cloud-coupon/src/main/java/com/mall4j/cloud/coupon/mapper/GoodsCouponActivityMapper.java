package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.ActivityListDTO;
import com.mall4j.cloud.coupon.model.GoodsCouponActivity;
import com.mall4j.cloud.coupon.model.PushCouponActivity;
import com.mall4j.cloud.coupon.vo.ActivityListVO;
import com.mall4j.cloud.coupon.vo.AppGoodsActivityVO;
import com.mall4j.cloud.coupon.vo.CouponForShoppersVO;
import com.mall4j.cloud.coupon.vo.GoodsActivityListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 商详领券
 *
 * @author shijing
 */

public interface GoodsCouponActivityMapper extends BaseMapper<GoodsCouponActivity> {
    List<GoodsActivityListVO> list(ActivityListDTO param);

    List<AppGoodsActivityVO> couponsForGoods(@Param("commodityId") Long commodityId, @Param("shopId")Long shopId);

}
