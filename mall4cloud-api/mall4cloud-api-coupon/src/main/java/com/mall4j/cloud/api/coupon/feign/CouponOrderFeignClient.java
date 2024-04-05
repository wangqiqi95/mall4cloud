package com.mall4j.cloud.api.coupon.feign;


import com.mall4j.cloud.api.coupon.dto.ChooseCouponDTO;
import com.mall4j.cloud.api.coupon.dto.LockCouponDTO;
import com.mall4j.cloud.api.coupon.dto.PlatformChooseCouponDTO;
import com.mall4j.cloud.api.coupon.dto.UpdateCouponStatusDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.order.vo.ShopCartVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 优惠券
 * @author FrozenWatermelon
 * @date 2020/12/17
 */
@FeignClient(value = "mall4cloud-coupon",contextId ="couponOrder")
public interface CouponOrderFeignClient {

    /**
     * 选择店铺优惠券，并组装返回优惠券列表
     * @param param 优惠券参数
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/chooseShopCoupon")
    ServerResponseEntity<List<ShopCartVO>> chooseShopCoupon(@RequestBody ChooseCouponDTO param);


    /**
     * 选择店铺优惠券，并组装返回优惠券列表
     * @param param 优惠券参数
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/staffChooseShopCoupon")
    ServerResponseEntity<List<ShopCartVO>> staffChooseShopCoupon(@RequestBody ChooseCouponDTO param);

    /**
     * 选择平台优惠券，并组装返回优惠券列表
     * @param param 优惠券参数
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/choosePlatformCoupon")
    ServerResponseEntity<ShopCartOrderMergerVO> choosePlatformCoupon(@RequestBody PlatformChooseCouponDTO param);


    /**
     * 选择平台优惠券，并组装返回优惠券列表
     * @param param 优惠券参数
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/staffChoosePlatformCoupon")
    ServerResponseEntity<ShopCartOrderMergerVO> staffChoosePlatformCoupon(@RequestBody PlatformChooseCouponDTO param);


    /**
     * 锁定订优惠券状态
     * @param lockCouponParams 订单id和优惠券id关联信息
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/lockCoupon")
    ServerResponseEntity<Void> lockCoupon(@RequestBody List<LockCouponDTO> lockCouponParams);

    /**
     * 修改优惠券状态
     * @param param 订单id和优惠券id关联信息
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/updateCouponStatus")
    ServerResponseEntity<Void> updateCouponStatus(@RequestBody UpdateCouponStatusDTO param);
}
