package com.mall4j.cloud.coupon.controller.app;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.model.BuyCouponLog;
import com.mall4j.cloud.coupon.service.BuyCouponActivityService;
import com.mall4j.cloud.coupon.service.GoodsCouponActivityService;
import com.mall4j.cloud.coupon.service.ReceiveCouponActivityService;
import com.mall4j.cloud.coupon.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 优惠券
 *
 * @author shijing
 * @date 2022-02-06
 */
@RestController("appCouponActivityController")
@RequestMapping("/ma/coupon_activity")
@Api(tags = "app-优惠券活动")
public class CouponActivityController {

    @Resource
    private ReceiveCouponActivityService receiveCouponActivityService;
    @Resource
    private BuyCouponActivityService buyCouponActivityService;
    @Resource
    private GoodsCouponActivityService goodsCouponActivityService;

    @GetMapping("/app_receive_activity")
    @ApiOperation(value = "领券活动首页")
    public ServerResponseEntity<AppReceiveActivityVO> appReceiveActivity(@RequestParam(value = "storeId",required = false,defaultValue = "") Long storeId) {
        return receiveCouponActivityService.appReceiveActivity(storeId);
    }

    @GetMapping("/user_receive")
    @ApiOperation(value = "领券中心-用户领券")
    public ServerResponseEntity<Void> userReceive(@RequestParam("id") Long id,@RequestParam("couponId") Long couponId,@RequestParam(value = "storeId",required = false,defaultValue = "") Long storeId){
        Long userId = AuthUserContext.get().getUserId();
        return receiveCouponActivityService.userReceive(id,couponId,storeId,userId);
    }

    @GetMapping("/app_buy_activity")
    @ApiOperation(value = "现金买券首页")
    public ServerResponseEntity<List<AppBuyActivityVO>> appBuyActivity() {
        return buyCouponActivityService.appBuyActivity();
    }

    @GetMapping("/app_buy_activity_detail")
    @ApiOperation(value = "现金买券详情")
    public ServerResponseEntity<AppBuyActivityDetailVO> appBuyActivityDetail(@RequestParam("id") Long id) {
        return buyCouponActivityService.appBuyActivityDetail(id);
    }

    @GetMapping("/appCouponDetail")
    @ApiOperation(value = "现金买券-优惠券详情")
    public ServerResponseEntity<AppBuyCouponDetailVO> appCouponDetail(@RequestParam("couponId") Long couponId) {
        return buyCouponActivityService.appCouponDetail(couponId);
    }

    @GetMapping("/buy_coupon")
    @ApiOperation(value = "现金买券-用户买券")
    public ServerResponseEntity<Long> buyCoupon(@RequestParam("activityId") Long activityId, @RequestParam("couponId") Long couponId,@RequestParam("storeId") Long storeId) {
        return buyCouponActivityService.buyCoupon(activityId, couponId, storeId);
    }

    @GetMapping("/buy_coupon_log")
    @ApiOperation(value = "现金买券-买券记录")
    public ServerResponseEntity<PageInfo<BuyCouponLog>> buyCouponLog(@RequestParam("pageNo")Integer pageNo, @RequestParam("pageSize")Integer pageSize) {
        return buyCouponActivityService.buyCouponLog(pageNo,pageSize);
    }

    @GetMapping("/coupons_for_goods")
    @ApiOperation(value = "商详领券")
    public ServerResponseEntity<List<AppGoodsActivityVO>> couponsForGoods(@RequestParam("commodityId") Long commodityId,@RequestParam("storeId") Long storeId) {
        return goodsCouponActivityService.couponsForGoods(commodityId,storeId);
    }

    @GetMapping("/goods_coupon_receive")
    @ApiOperation(value = "商详领券-用户领取")
    public ServerResponseEntity<Void> goodsCouponReceive(@RequestParam("id") Long id,@RequestParam("couponId") Long couponId) {
        return goodsCouponActivityService.userReceive(id,couponId);
    }

}
