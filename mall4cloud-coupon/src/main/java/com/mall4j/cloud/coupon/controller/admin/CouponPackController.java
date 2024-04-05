package com.mall4j.cloud.coupon.controller.admin;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.ActivitySourceEnum;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.model.CouponPackShop;
import com.mall4j.cloud.coupon.model.CouponPackStockLog;
import com.mall4j.cloud.coupon.service.CouponPackService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.ActivityReportVO;
import com.mall4j.cloud.coupon.vo.CouponActivityDetailVO;
import com.mall4j.cloud.coupon.vo.CouponPackListVO;
import com.mall4j.cloud.coupon.vo.CouponPackVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mp/coupon_pack")
@Api(tags = "admin-优惠券包活动")
public class CouponPackController {
    @Resource
    private CouponPackService couponPackService;
    @Autowired
    private TCouponUserService tCouponUserService;

    @PutMapping
    @ApiOperation(value = "saveOrUpdate",notes = "新增或修改优惠券包")
    public ServerResponseEntity<Void> saveOrUpdate(@RequestBody @Valid CouponPackDTO param) {
        String username = AuthUserContext.get().getUsername();
        Integer id = param.getId();

        if (null == id){
            param.setCreateUserName(username);
        }else {
            param.setUpdateUserName(username);
        }
        return couponPackService.saveOrUpdateCouponPackActivity(param);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "detail",notes = "优惠券包详情")
    public ServerResponseEntity<CouponPackVO> detail(@PathVariable Integer id){
        return couponPackService.detail(id);
    }

    @PostMapping
    @ApiOperation(value = "page",notes = "优惠券包列表")
    public ServerResponseEntity<PageVO<CouponPackListVO>> page(@RequestBody CouponPackPageDTO param){
        return couponPackService.couponPackPage(param);
    }

    @PostMapping("/enable/{id}")
    @ApiOperation(value = "enable",notes = "启用")
    public ServerResponseEntity<Void> enable(@PathVariable Integer id){
        return couponPackService.enable(id);
    }

    @PostMapping("/disable/{id}")
    @ApiOperation(value = "disable",notes = "禁用")
    public ServerResponseEntity<Void> disable(@PathVariable Integer id){
        return couponPackService.disable(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete",notes = "删除活动")
    public ServerResponseEntity<Void> delete(@PathVariable Integer id){
        return couponPackService.delete(id);
    }

    @GetMapping("/shop/{activityId}")
    @ApiOperation(value = "getShop",notes = "获取门店信息")
    public ServerResponseEntity<List<CouponPackShop>> getShop(@PathVariable Integer activityId) {
        return couponPackService.getActivityShop(activityId);
    }
    @PutMapping("/shop")
    @ApiOperation(value = "addShop",notes = "添加适用门店")
    public ServerResponseEntity<Void> addShop(@RequestBody ModifyShopDTO param) {
        return couponPackService.addActivityShop(param);
    }
    @DeleteMapping("/shop/{activityId}/{shopId}")
    @ApiOperation(value = "deleteShop",notes = "删除适用门店")
    public ServerResponseEntity<Void> deleteShop(@PathVariable Integer activityId,@PathVariable Integer shopId) {
        return couponPackService.deleteActivityShop(activityId,shopId);
    }

    @DeleteMapping("/shop/all/{activityId}")
    @ApiOperation(value = "deleteAllShop",notes = "清空适用门店")
    public ServerResponseEntity<Void> deleteAllShop(@PathVariable Integer activityId) {
        return couponPackService.deleteAllShop(activityId);
    }

    @PostMapping("/stock/add")
    public ServerResponseEntity<Void> stockAdd(@RequestBody AddStockDTO param) {
        return couponPackService.AddStock(param);
    }

    @PostMapping("/select/activity")
    @ApiOperation(value = "selectActivity",notes = "选择活动")
    public ServerResponseEntity<PageVO<CouponPackListVO>> selectActivity(@RequestBody CouponPackSelectDTO param){
        return couponPackService.selectCouponPack(param);
    }

    @PostMapping("/activityReport")
    @ApiOperation(value = "activityReport",notes = "选择活动")
    public ServerResponseEntity<ActivityReportVO> activityReport(@RequestBody ActivityReportDTO param){
        return couponPackService.activityReport(param);
    }

    @PostMapping("/stock/log/{activityId}")
    @ApiOperation(value = "stockLog",notes = "调整库存记录")
    public ServerResponseEntity<PageVO<CouponPackStockLog>> stockLog(@PathVariable Integer activityId, @RequestBody PageDTO param){
        return couponPackService.stockLog(activityId,param);
    }

    @PostMapping("/couponActivityDetail")
    @ApiOperation(value = "券活动明细")
    public PageInfo<CouponActivityDetailVO> couponActivityDetail(@RequestBody CouponActivityDTO param){
        param.setActivitySource(ActivitySourceEnum.COUPON_PACK.value());
        return tCouponUserService.couponActivityDetail(param);
    }
}
