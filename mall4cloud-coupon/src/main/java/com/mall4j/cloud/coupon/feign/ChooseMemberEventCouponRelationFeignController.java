package com.mall4j.cloud.coupon.feign;

import com.mall4j.cloud.api.coupon.feign.ChooseMemberEventCouponRelationFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.service.ChooseMemberEventCouponRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChooseMemberEventCouponRelationFeignController implements ChooseMemberEventCouponRelationFeignClient {

    @Autowired
    private ChooseMemberEventCouponRelationService chooseMemberEventCouponRelationService;

    @Override
    public ServerResponseEntity<Integer> eventCouponToShopCheck(Long couponId) {
        return chooseMemberEventCouponRelationService.checkCouponToShop(couponId);
    }
}
