package com.mall4j.cloud.coupon.controller.platform;

import com.github.pagehelper.PageInfo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.constant.ActivityStatusEnum;
import com.mall4j.cloud.coupon.dto.*;
import com.mall4j.cloud.coupon.service.GoodsCouponActivityService;
import com.mall4j.cloud.coupon.service.PushCouponActivityService;
import com.mall4j.cloud.coupon.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商详领券
 * @author shijing
 * @date 2022-02-27
 */
@RestController("platformGoodsCouponActivityController")
@RequestMapping("/p/goods_coupon_activity")
@Api(tags = "platform-商详领券 ")
public class GoodsCouponActivityController {
    @Resource
    private GoodsCouponActivityService goodsCouponActivityService;

    @PostMapping("/list")
    @ApiOperation(value = "商详领券活动列表")
    public ServerResponseEntity<PageInfo<GoodsActivityListVO>> list(@RequestBody ActivityListDTO param){
        return goodsCouponActivityService.list(param);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增活动")
    public ServerResponseEntity<Void> save(@RequestBody GoodsActivityDTO param){
        return goodsCouponActivityService.save(param);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "活动详情")
    public ServerResponseEntity<GoodsActivityVO> detail(@RequestParam Long id){
        return goodsCouponActivityService.detail(id);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除活动")
    public ServerResponseEntity<Void> delete(@RequestParam Long id){
        return goodsCouponActivityService.delete(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改活动")
    public ServerResponseEntity<Void> update(@RequestBody GoodsActivityDTO param){
        return goodsCouponActivityService.update(param);
    }

    @GetMapping("/enable")
    @ApiOperation(value = "启用活动")
    public ServerResponseEntity<Void> enable(@RequestParam Long id){
        return goodsCouponActivityService.updateStatus(id, ActivityStatusEnum.ENABLE.value().shortValue());
    }

    @GetMapping("/disable")
    @ApiOperation(value = "禁用活动")
    public ServerResponseEntity<Void> disable(@RequestParam Long id){
        return goodsCouponActivityService.updateStatus(id, ActivityStatusEnum.DISABLE.value().shortValue());
    }

    @DeleteMapping("/deleteShop")
    @ApiOperation(value = "删除关联门店")
    public ServerResponseEntity<Void> deleteShop(@RequestParam Long id,@RequestParam(required = false) Long shopId, @RequestParam Boolean isAllShop){
        return goodsCouponActivityService.deleteShop(id,shopId,isAllShop);
    }

    @PostMapping("/addShops")
    @ApiOperation(value = "新增关联门店")
    public ServerResponseEntity<Void> addShops(@RequestBody AddShopsDTO param){
        return goodsCouponActivityService.addShops(param);
    }
}
