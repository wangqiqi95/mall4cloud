package com.mall4j.cloud.api.coupon.feign;


import com.mall4j.cloud.api.coupon.dto.BindCouponDTO;
import com.mall4j.cloud.api.coupon.vo.CouponDataVO;
import com.mall4j.cloud.api.coupon.vo.CouponUserCountDataVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.vo.SpuCouponAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 优惠券
 * @author FrozenWatermelon
 * @date 2020/12/17
 */
@FeignClient(value = "mall4cloud-coupon",contextId ="coupon")
public interface CouponFeignClient {

    /**
     * 根据优惠券id列表，获取优惠券列表
     * @param couponIds
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/getCouponListByCouponIds")
    ServerResponseEntity<List<CouponDataVO>> getCouponListByCouponIds(@RequestParam("couponIds") List<Long> couponIds);

    /**
     * 根据优惠券id列表，获取优惠券列表
     * @param orderIds
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/getCouponListByOrderIds")
    ServerResponseEntity<List<TCouponUserOrderDetailVO>> getCouponListByOrderIds(@RequestParam("orderIds") List<Long> orderIds);

    /**
     * 根据优惠券id列表，获取优惠券列表
     * @param orderIds
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/postCouponListByOrderIds")
    ServerResponseEntity<List<TCouponUserOrderDetailVO>> postCouponListByOrderIds(@RequestParam("orderIds") List<Long> orderIds);

    /**
     * 根据优惠券id列表，获取优惠券列表
     * @param orderIds
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/getCouponListBypByOrderIds")
    ServerResponseEntity<List<TCouponUserOrderDetailVO>> getCouponListBypByOrderIds(@RequestBody List<Long> orderIds);

    /**
     * 根据优惠券id列表，投放状态，获取优惠券列表
     * @param couponIds 优惠券ids
     * @param putOnStatus 投放状态
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/getCouponListByCouponIdsAndPutOnStatus")
    ServerResponseEntity<List<CouponDataVO>> getCouponListByCouponIdsAndPutOnStatus(@RequestParam("couponIds") List<Long> couponIds, @RequestParam(value = "putOnStatus", required = false) Integer putOnStatus);

    /**
     * 绑定优惠券（平台赠送优惠券给用户）
     * @param bindCouponDTO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/batchBindCouponByCouponIds")
    ServerResponseEntity<Void> batchBindCouponByCouponIds(@RequestBody BindCouponDTO bindCouponDTO);


    /**
     * 获取可使用的优惠券数量
     * @return 可使用的优惠券数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/coupon/countCanUseCoupon")
    ServerResponseEntity<Integer> countCanUseCoupon();

    /**
     * 商品详情的优惠券列表
     *
     * @param shopId
     * @param spuId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/couponListBySpuId")
    ServerResponseEntity<List<SpuCouponAppVO>> couponOfSpuDetail(@RequestParam("shopId") Long shopId, @RequestParam("spuId") Long spuId);

    /**
     * 统计用户优惠券信息
     * @param userId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/countCouponUserByUserId")
    ServerResponseEntity<CouponUserCountDataVO> countCouponUserByUserId(@RequestParam("userId") Long userId);

    /**
     * 获取领券会员数
     * @param shopId 店铺id
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @return 领券会员数
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/countMemberCouponByParam")
    ServerResponseEntity<List<Long>> countMemberCouponByParam(@RequestParam("shopId") Long shopId,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("startTime") Date startTime,
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")@RequestParam("endTime")Date endTime);

    /**
     * 处理商品下线
     *
     * @param spuIds 商品id列表
     * @param shopIds 店铺id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/handleSpuOffline")
    ServerResponseEntity<Void> handleSpuOffline(@RequestParam("spuIds") List<Long> spuIds, @RequestParam("shopIds") List<Long> shopIds);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/coupon/userReceiveCoupon")
    ServerResponseEntity<Void> userReceiveCoupon(@RequestParam("id") Long id,@RequestParam("couponId") Long couponId,@RequestParam("storeId") Long storeId, @RequestParam("userId") Long userId);


}
