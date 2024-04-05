package com.mall4j.cloud.api.coupon.feign;

import com.mall4j.cloud.api.coupon.dto.PayCouponDTO;
import com.mall4j.cloud.api.coupon.vo.BuyCouponLog;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 优惠券
 * @author shijing
 * @date 2022/1/18
 */
@FeignClient(value = "mall4cloud-coupon",contextId ="BuyCoupon")
public interface BuyCouponFeignClient {

    /**
     * 根据订单编号，获取现金买券记录具体信息
     * @param orderNo
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/buyCoupon/getBuyCouponDetail")
    ServerResponseEntity<BuyCouponLog> getBuyCouponDetail(@RequestParam("orderNo") Long orderNo);

    /**
     * 支付成功回调发券
     * @param
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/buyCoupon/payCoupon")
    ServerResponseEntity<Void> payCoupon(@RequestBody PayCouponDTO param);



}
