package com.mall4j.cloud.coupon.service.impl;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.model.ChooseMemberEventCouponRelation;
import com.mall4j.cloud.coupon.mapper.ChooseMemberEventCouponRelationMapper;
import com.mall4j.cloud.coupon.service.ChooseMemberEventCouponRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 制定会员活动优惠券关联表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2022-11-03
 */
@Service
public class ChooseMemberEventCouponRelationServiceImpl extends ServiceImpl<ChooseMemberEventCouponRelationMapper, ChooseMemberEventCouponRelation> implements ChooseMemberEventCouponRelationService {

    @Autowired
    private ChooseMemberEventCouponRelationMapper chooseMemberEventCouponRelationMapper;

    @Override
    public ServerResponseEntity<Integer> checkCouponToShop(Long couponId) {
        Integer toShop = 1;

        Integer integer = chooseMemberEventCouponRelationMapper.checkCouponToShop(couponId);
        if (integer > 0){
            toShop = 0;
        }
        return ServerResponseEntity.success(toShop);
    }
}
