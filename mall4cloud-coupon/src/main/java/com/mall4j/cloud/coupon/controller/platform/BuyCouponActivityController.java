package com.mall4j.cloud.coupon.controller.platform;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.constant.ActivityStatusEnum;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.BuyCouponLog;
import com.mall4j.cloud.coupon.service.BuyCouponActivityService;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 推券中心
 *
 * @author shijing
 * @date 2022-01-05
 */
@RestController("platformBuyCouponActivityController")
@RequestMapping("/p/buy_coupon_activity")
@Api(tags = "platform-现金买券 ")
public class BuyCouponActivityController {
    @Resource
    private BuyCouponActivityService buyCouponActivityService;
    @Resource
    private TCouponUserService tCouponUserService;

    @PostMapping("/list")
    @ApiOperation(value = "现金买券活动列表")
    public ServerResponseEntity<PageInfo<ActivityListVO>> list(@RequestBody ActivityListDTO param) {
        return buyCouponActivityService.list(param);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增活动")
    public ServerResponseEntity<Void> save(@RequestBody BuyActivityDTO param) {
        return buyCouponActivityService.save(param);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "活动详情")
    public ServerResponseEntity<BuyActivityDetailVO> detail(@RequestParam Long id) {
        return buyCouponActivityService.detail(id);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除活动")
    public ServerResponseEntity<Void> delete(@RequestParam Long id) {
        return buyCouponActivityService.delete(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改活动")
    public ServerResponseEntity<Void> update(@RequestBody BuyActivityDTO param) {
        return buyCouponActivityService.update(param);
    }

    @GetMapping("/enable")
    @ApiOperation(value = "启用活动")
    public ServerResponseEntity<Void> enable(@RequestParam Long id) {
        return buyCouponActivityService.updateStatus(id, ActivityStatusEnum.ENABLE.value().shortValue());
    }

    @GetMapping("/disable")
    @ApiOperation(value = "禁用活动")
    public ServerResponseEntity<Void> disable(@RequestParam Long id) {
        return buyCouponActivityService.updateStatus(id, ActivityStatusEnum.DISABLE.value().shortValue());
    }

    @PostMapping("/activityCouponList")
    @ApiOperation(value = "活动优惠券列表")
    public ServerResponseEntity<PageInfo<BuyActivityCouponVO>> activityCouponList(@RequestBody ActivityCouponListVO param) {
        return buyCouponActivityService.ActivityCouponList(param);
    }

    @PostMapping("/addInventory")
    @ApiOperation(value = "增加库存")
    public ServerResponseEntity<Void> addInventory(@RequestBody AddInventoryDTO param) {
        return buyCouponActivityService.addInventory(param);
    }

    @PostMapping("/inventoryLog")
    @ApiOperation(value = "库存调整记录")
    public ServerResponseEntity<PageInfo<InventoryListVO>> inventoryLog(@RequestBody ActivityCouponListVO param) {
        return buyCouponActivityService.inventoryLog(param);
    }

    @DeleteMapping("/deleteShop")
    @ApiOperation(value = "删除关联门店")
    public ServerResponseEntity<Void> deleteShop(@RequestParam Long id, @RequestParam(required = false) Long shopId,@RequestParam Boolean isAllShop) {
        return buyCouponActivityService.deleteShop(id, shopId,isAllShop);
    }

    @PostMapping("/addShops")
    @ApiOperation(value = "新增关联门店")
    public ServerResponseEntity<Void> addShops(@RequestBody AddShopsDTO param) {
        return buyCouponActivityService.addShops(param);
    }

    /**
     * 商城小程序-领券中心
     */
    @GetMapping("/appBuyActivity")
    @ApiOperation(value = "商城小程序-现金买券首页")
    public ServerResponseEntity<List<AppBuyActivityVO>> appBuyActivity() {
        return buyCouponActivityService.appBuyActivity();
    }

    /**
     * 商城小程序-现金买券详情
     */
    @GetMapping("/appBuyActivityDetail")
    @ApiOperation(value = "商城小程序-现金买券详情")
    public ServerResponseEntity<AppBuyActivityDetailVO> appBuyActivityDetail(@RequestParam("id") Long id) {
        return buyCouponActivityService.appBuyActivityDetail(id);
    }

    /**
     * 商城小程序-优惠券详情
     */
    @GetMapping("/appCouponDetail")
    @ApiOperation(value = "商城小程序-优惠券详情")
    public ServerResponseEntity<AppBuyCouponDetailVO> appCouponDetail(@RequestParam("activityId") Long activityId) {
        return buyCouponActivityService.appCouponDetail(activityId);
    }

    /**
     * 商城小程序-买券
     */
    @GetMapping("/buyCoupon")
    @ApiOperation(value = "商城小程序-买券")
    public ServerResponseEntity<Long> buyCoupon(@RequestParam("activityId") Long activityId, @RequestParam("couponId") Long couponId ,@RequestParam("storeId") Long storeId) {
        return buyCouponActivityService.buyCoupon(activityId, couponId,storeId);
    }

    /**
     * 商城小程序-买券记录
     */
    @GetMapping("/buyCouponLog")
    @ApiOperation(value = "商城小程序-买券记录")
    public ServerResponseEntity<PageInfo<BuyCouponLog>> buyCouponLog(Integer pageNo,Integer pageSize) {
        return buyCouponActivityService.buyCouponLog(pageNo,pageSize);
    }

    @PostMapping("/activityReport")
    @ApiOperation(value = "活动报表")
    public ServerResponseEntity<ActivityReportVO> activityReport(@RequestBody ActivityReportDTO param){
        return buyCouponActivityService.activityReport(param);
    }

    @PostMapping("/couponActivityDetail")
    @ApiOperation(value = "券活动明细")
    public PageInfo<CouponActivityDetailVO> couponActivityDetail(@RequestBody CouponActivityDTO param){
        param.setActivitySource(ActivitySourceEnum.BUY.value());
        return tCouponUserService.couponActivityDetail(param);
    }
}
