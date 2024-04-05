package com.mall4j.cloud.coupon.controller.platform;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.constant.ActivityStatusEnum;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.service.ReceiveCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 推券中心
 * @author shijing
 * @date 2022-01-05
 */
@RestController("platformReceiveCouponActivityController")
@RequestMapping("/p/receive_coupon_activity")
@Api(tags = "platform-领券中心 ")
public class ReceiveCouponActivityController {
    @Resource
    private ReceiveCouponActivityService receiveCouponActivityService;
    @Resource
    private TCouponUserService tCouponUserService;

    @PostMapping("/list")
    @ApiOperation(value = "领券活动列表")
    public ServerResponseEntity<PageInfo<ActivityListVO>> list(@RequestBody ActivityListDTO param){
        return receiveCouponActivityService.list(param);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增活动")
    public ServerResponseEntity<Void> save(@RequestBody ReceiveActivityDTO param){
        return receiveCouponActivityService.save(param);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "活动详情")
    public ServerResponseEntity<ReceiveActivityDetailVO> detail(@RequestParam Long id){
        return receiveCouponActivityService.detail(id);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除活动")
    public ServerResponseEntity<Void> delete(@RequestParam Long id){
        return receiveCouponActivityService.delete(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改活动")
    public ServerResponseEntity<Void> update(@RequestBody ReceiveActivityDTO param){
        return receiveCouponActivityService.update(param);
    }

    @GetMapping("/enable")
    @ApiOperation(value = "启用活动")
    public ServerResponseEntity<Void> enable(@RequestParam Long id){
        return receiveCouponActivityService.updateStatus(id, ActivityStatusEnum.ENABLE.value().shortValue());
    }

    @GetMapping("/disable")
    @ApiOperation(value = "禁用活动")
    public ServerResponseEntity<Void> disable(@RequestParam Long id){
        return receiveCouponActivityService.updateStatus(id, ActivityStatusEnum.DISABLE.value().shortValue());
    }

    @PostMapping("/activityCouponList")
    @ApiOperation(value = "活动优惠券列表")
    public ServerResponseEntity<PageInfo<ReceiveActivityCouponVO>> activityCouponList(@RequestBody ActivityCouponListVO param){
        return receiveCouponActivityService.ActivityCouponList(param);
    }

    @PostMapping("/addInventory")
    @ApiOperation(value = "增加库存")
    public ServerResponseEntity<Void> addInventory(@RequestBody AddInventoryDTO param){
        return receiveCouponActivityService.addInventory(param);
    }

    @PostMapping("/inventoryLog")
    @ApiOperation(value = "库存调整记录")
    public ServerResponseEntity<PageInfo<InventoryListVO>> inventoryLog(@RequestBody ActivityCouponListVO param){
        return receiveCouponActivityService.inventoryLog(param);
    }


    @DeleteMapping("/deleteShop")
    @ApiOperation(value = "删除关联门店")
    public ServerResponseEntity<Void> deleteShop(@RequestParam Long id,@RequestParam(required = false) Long shopId,@RequestParam Boolean isAllShop){
        return receiveCouponActivityService.deleteShop(id,shopId,isAllShop);
    }

    @PostMapping("/addShops")
    @ApiOperation(value = "新增关联门店")
    public ServerResponseEntity<Void> addShops(@RequestBody AddShopsDTO param){
        return receiveCouponActivityService.addShops(param);
    }

    @PostMapping("/activityReport")
    @ApiOperation(value = "活动报表")
    public ServerResponseEntity<ActivityReportVO> activityReport(@RequestBody ActivityReportDTO param){
        return receiveCouponActivityService.activityReport(param);
    }

    @PostMapping("/couponActivityDetail")
    @ApiOperation(value = "券活动明细")
    public PageInfo<CouponActivityDetailVO> couponActivityDetail(@RequestBody CouponActivityDTO param){
        param.setActivitySource(ActivitySourceEnum.RECEIVE.value());
        return tCouponUserService.couponActivityDetail(param);
    }
}
