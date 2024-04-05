package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.model.ChooseMemberEventCouponRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 制定会员活动优惠券关联表 服务类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
public interface ChooseMemberEventCouponRelationService extends IService<ChooseMemberEventCouponRelation> {

    ServerResponseEntity<Integer> checkCouponToShop(@Param("couponId") Long couponId);

}
