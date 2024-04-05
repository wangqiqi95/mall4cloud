package com.mall4j.cloud.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.coupon.dto.ActivityListDTO;
import com.mall4j.cloud.coupon.model.CrmCouponToken;
import com.mall4j.cloud.coupon.model.GoodsCouponActivity;
import com.mall4j.cloud.coupon.vo.AppGoodsActivityVO;
import com.mall4j.cloud.coupon.vo.GoodsActivityListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 冻结优惠券token
 *
 * @author shijing
 */

public interface CrmCouponTokenMapper extends BaseMapper<CrmCouponToken> {

}
