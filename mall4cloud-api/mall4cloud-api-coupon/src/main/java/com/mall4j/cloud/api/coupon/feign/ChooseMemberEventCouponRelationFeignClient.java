package com.mall4j.cloud.api.coupon.feign;

import com.mall4j.cloud.api.coupon.vo.CouponDataVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mall4cloud-coupon",contextId ="ChooseMemberEventCouponRelation")
public interface ChooseMemberEventCouponRelationFeignClient {

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/choose/member/event/coupon/relation/to/shop/check")
    ServerResponseEntity<Integer> eventCouponToShopCheck(@RequestParam("couponId") Long couponId);

}
