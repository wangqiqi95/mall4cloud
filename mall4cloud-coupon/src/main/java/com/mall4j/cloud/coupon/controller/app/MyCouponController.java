package com.mall4j.cloud.coupon.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.CouponUserService;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 优惠券
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
@RestController("appMyCouponController")
@RequestMapping("/my_coupon")
@Api(tags = "app-我的优惠券")
public class MyCouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponUserService couponUserService;

    @GetMapping("/get_page")
    @ApiOperation(value = "查看用户的优惠券(所有/指定状态)分页列表", notes = "查看用户的优惠券(所有/指定状态)分页列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "status", value = "优惠券状态 0:已过期 1:未使用 2:已使用", dataType = "Integer", defaultValue = "-1"),
            @ApiImplicitParam(name = "type", value = "0：所有 1：平台优惠券 2：店铺优惠券", dataType = "Long", defaultValue = "0")
    })
    public ServerResponseEntity<PageVO<CouponAppVO>> getUserCouponPage(PageDTO page, @RequestParam("type") Long type, @RequestParam("status") Integer status) {
        return ServerResponseEntity.success(couponService.getUserCouponPage(page, type, status));
    }

    @GetMapping("/get_my_coupons_status_or_type_count")
    @ApiOperation(value = "获取用户优惠券的数量", notes = "获取各个状态下的优惠券个数")
    public ServerResponseEntity<Map<String, Long>> getCouponCountByStatus() {
        Map<String, Long> couponCount = couponUserService.getCouponCountByStatus(AuthUserContext.get().getUserId());
        return ServerResponseEntity.success(couponCount);
    }

    @PostMapping("/receive")
    @ApiOperation(value = "领取优惠券接口", notes = "领取优惠券接口")
    public ServerResponseEntity<String> receive(@RequestBody Long couponId) {
        if (Objects.isNull(couponId)) {
            throw new LuckException("优惠券Id不能为空");
        }
        couponService.receive(couponId);
        return ServerResponseEntity.success("领取优惠券成功");
    }

    @DeleteMapping("/delete_coupon/{couponUserId}")
    @ApiOperation(value = "删除用户优惠券", notes = "通过优惠券id删除用户优惠券")
    @ApiImplicitParam(name = "couponUserId", value = "用户优惠券Id", dataType = "Long")
    public ServerResponseEntity<String> deleteCoupon(@PathVariable("couponUserId") Long couponUserId) {
        couponUserService.deleteUserCouponByCouponId(couponUserId);
        return ServerResponseEntity.success("删除成功");
    }
}
