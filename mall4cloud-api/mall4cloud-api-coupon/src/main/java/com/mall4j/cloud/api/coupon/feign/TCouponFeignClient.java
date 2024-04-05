package com.mall4j.cloud.api.coupon.feign;

import com.mall4j.cloud.api.coupon.dto.*;
import com.mall4j.cloud.api.coupon.vo.*;
import com.mall4j.cloud.api.crm.dto.QueryHasCouponUsersRequest;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.CouponOrderVO;
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
 * @author shijing
 * @date 2022/1/18
 */
@FeignClient(value = "mall4cloud-coupon",contextId ="tCoupon")
public interface TCouponFeignClient {

    /**
     * 根据优惠券id列表，获取优惠券列表
     * @param couponId
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/tCoupon/getCouponDetail")
    ServerResponseEntity<CouponDetailVO> getCouponDetail(@RequestParam("couponId") Long couponId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/tCoupon/getCouponDetailByCouponCode")
    ServerResponseEntity<CouponDetailVO> getCouponDetailByCouponCode(@RequestParam("couponCode") String couponCode);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCoupon/save")
    ServerResponseEntity<List<CouponSyncDto>> save(@RequestBody List<CouponDetailDTO> param);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCoupon/update")
    ServerResponseEntity<List<CouponSyncDto>> update(@RequestBody List<CouponDetailDTO> param);

    /**
     * 根据优惠券id列表，获取优惠券列表
     * @param couponIds
     * @return sku信息
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCoupon/selectCouponByIds")
    ServerResponseEntity<List<CouponListVO>> selectCouponByIds(@RequestParam("couponIds")List<Long> couponIds);

    /**
     * 发券
     * @param param
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/receive")
    ServerResponseEntity<Void> receive(@RequestBody ReceiveCouponDTO param);

    /**
     * 批量发券
     * @param param
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/batchReceive")
    ServerResponseEntity<Void> batchReceive(@RequestBody BatchReceiveCouponDTO param);

    /**
     * 用户发券列表
     * @param param
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/list")
    ServerResponseEntity<List<TCouponUser>> userCouponList(@RequestBody TCouponUser param);

    /**
     * 获取可使用的优惠券数量
     * @return 可使用的优惠券数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/countCanUseCoupon")
    ServerResponseEntity<Integer> countCanUseCoupon(@RequestParam("userId") Long userId);

    /**
     * 现金买券支付回调
     * @param param
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCoupon/payCoupon")
    ServerResponseEntity<Void> payCoupon(@RequestBody PayCouponDTO param);

    /**
     * 查询某个时间段已核销的企业券信息
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/selectOrderNo")
    ServerResponseEntity<List<TCouponUser>> selectOrderNo(@RequestParam("startTime") Date startTime, @RequestParam("endTime")  Date endTime);

    /**
     * 查看订单是否使用企业券
     * @param orderNo 订单编号
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/isUseEnterpriseCoupon")
    ServerResponseEntity<Boolean> isUseEnterpriseCoupon(@RequestParam("orderNo")Long orderNo);

    /**
     * 统计用户优惠券信息
     * @param userId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/countCouponUserByUserId")
    ServerResponseEntity<CouponUserCountDataVO> countCouponUserByUserId(@RequestParam("userId") Long userId);

    /**
     * 根据订单ID获取用户优惠券记录
     * @param orderIds
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/selectByOrderIds")
    ServerResponseEntity<List<TCouponUserOrderVo>> selectByOrderIds(@RequestBody List<Long> orderIds);

    /**
     * 同步crm券保存到用户券记录表
     * @param couponOrderVO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/syncCRMCoupon")
    ServerResponseEntity<Void> syncCRMCoupon(@RequestBody CouponOrderVO couponOrderVO);

    /**
     * 查询符合条件的crm优惠券ids
     * @param queryCrmIdsDTO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCoupon/queryCrmIds")
    ServerResponseEntity<List<String>> queryCrmIds(@RequestBody QueryCrmIdsDTO queryCrmIdsDTO);


    /**
     * 查询符合条件的用户ids
     * @param queryHasCouponUsersRequest
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCoupon/getCouponUserIds")
    ServerResponseEntity<List<Long>> getCouponUserIds(@RequestBody QueryHasCouponUsersRequest queryHasCouponUsersRequest);
    
    /**
     * 根据纸质优惠券核销时间获取优惠券信息
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/tCouponUser/listPaperCouponOrder")
    ServerResponseEntity<List<PaperCouponOrderVO>> listPaperCouponOrder(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("startTime") Date startTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam("endTime") Date endTime);
    
}
