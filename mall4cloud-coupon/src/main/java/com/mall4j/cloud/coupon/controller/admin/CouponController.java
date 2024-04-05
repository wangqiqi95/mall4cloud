package com.mall4j.cloud.coupon.controller.admin;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.GetWay;
import com.mall4j.cloud.coupon.dto.CouponDTO;
import com.mall4j.cloud.coupon.dto.UserCouponsDTO;
import com.mall4j.cloud.coupon.model.Coupon;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.vo.CouponVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
@RestController("adminCouponController")
@RequestMapping("/mp/coupon")
@Api(tags = "admin-优惠券")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/admin_page")
    @ApiOperation(value = "店铺优惠券管理列表", notes = "店铺优惠券管理列表")
    public ServerResponseEntity<PageVO<CouponVO>> platformPage(@Valid PageDTO pageDTO, CouponDTO couponDTO) {
        PageVO<CouponVO> couponPage = couponService.adminPage(pageDTO, couponDTO);
        return ServerResponseEntity.success(couponPage);
    }

	@GetMapping("/page")
	@ApiOperation(value = "获取优惠券列表", notes = "分页获取优惠券列表")
	public ServerResponseEntity<PageVO<CouponVO>> page(@Valid PageDTO pageDTO, CouponDTO couponDTO) {
	    couponDTO.setShopId(AuthUserContext.get().getTenantId());
		PageVO<CouponVO> couponPage = couponService.page(pageDTO, couponDTO);
		return ServerResponseEntity.success(couponPage);
	}

	@GetMapping
    @ApiOperation(value = "获取优惠券", notes = "根据couponId获取优惠券")
    public ServerResponseEntity<CouponVO> getByCouponId(@RequestParam Long couponId) {
        return ServerResponseEntity.success(couponService.getCouponAndCouponProdsByCouponId(couponId));
    }

    @PostMapping
    @ApiOperation(value = "保存优惠券", notes = "保存优惠券")
    public ServerResponseEntity<Void> save(@Valid @RequestBody CouponDTO couponDTO) {
        Long shopId = AuthUserContext.get().getTenantId();
        couponDTO.setShopId(shopId);
        if (!Objects.equals(couponDTO.getShopId(), Constant.PLATFORM_SHOP_ID) && Objects.equals(couponDTO.getGetWay(), GetWay.PLATFORM.value())) {
            couponDTO.setGetWay(GetWay.USER.value());
        }
        couponService.save(couponDTO);
        couponService.removeCacheByShopId(AuthUserContext.get().getTenantId(), null);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "更新优惠券", notes = "更新优惠券")
    public ServerResponseEntity<Void> update(@Valid @RequestBody CouponDTO couponDTO) {
        couponService.update(couponDTO);
        couponService.removeCacheByShopId(AuthUserContext.get().getTenantId(), couponDTO.getCouponId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除优惠券", notes = "根据优惠券id删除优惠券")
    public ServerResponseEntity<Void> delete(@RequestParam Long couponId) {
        couponService.deleteById(couponId);
        couponService.removeCacheByShopId(AuthUserContext.get().getTenantId(), couponId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/change_coupon_status")
    @ApiOperation(value = "修改优惠券状态", notes = "修改优惠券状态")
    public ServerResponseEntity<Boolean> changeCouponStatus(@RequestBody CouponDTO couponDTO) {
        couponService.changeCouponStatus(couponDTO.getCouponId(), couponDTO.getStatus());
        couponService.removeCacheByShopId(AuthUserContext.get().getTenantId(), couponDTO.getCouponId());
	    return ServerResponseEntity.success();
    }


    @GetMapping("/page_platform_coupons")
    @ApiOperation(value = "分页获取平台优惠券列表", notes = "分页获取平台优惠券列表")
    public ServerResponseEntity<PageVO<CouponVO>> pagePlatformCoupons(@Valid PageDTO pageDTO, CouponDTO couponDTO) {
        couponDTO.setShopId(Constant.PLATFORM_SHOP_ID);
        couponDTO.setStatus(StatusEnum.ENABLE.value());
        couponDTO.setPutonStatus(StatusEnum.ENABLE.value());
        PageVO<CouponVO> couponPage = couponService.page(pageDTO, couponDTO);
        return ServerResponseEntity.success(couponPage);
    }

    @PutMapping("/send_user_coupon")
    @ApiOperation(value = "批量发放优惠券", notes = "平台批量发放优惠券给用户")
    public ServerResponseEntity<Boolean> sendUserCoupon(@RequestBody UserCouponsDTO userCouponsDTO) {
        List<Long> userIds = userCouponsDTO.getUserIds();
        if (CollUtil.isEmpty(userIds)) {
            return ServerResponseEntity.success();
        }
        List<UserCouponsDTO.SendCoupon> coupons = userCouponsDTO.getSendCoupons();
        if (CollUtil.isEmpty(coupons)) {
            return ServerResponseEntity.success();
        }
        for (Long userId : userIds) {
            for (UserCouponsDTO.SendCoupon coupon : coupons) {
                Long couponId = coupon.getCouponId();
                Integer nums = coupon.getNums();
                if (Objects.equals(0,nums)) {
                    continue;
                }
                for (int i = 0; i < nums; i++) {
                    couponService.batchBindCouponByCouponIds(Collections.singletonList(couponId), userId);
                }
            }
            return ServerResponseEntity.success();
        }
        return ServerResponseEntity.success();
    }


    @PostMapping("/offline")
    @ApiOperation(value = "下线优惠券", notes = "下线优惠券")
    public ServerResponseEntity<Void> offline(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        Coupon coupon = couponService.getByCouponId(offlineHandleEventDto.getHandleId());
        if (Objects.isNull(coupon)) {
            throw new LuckException(ResponseEnum.DATA_ERROR);
        }
        couponService.offline(offlineHandleEventDto);
        // 清除缓存
        couponService.removeCacheByShopId(coupon.getShopId(), coupon.getCouponId());
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_offline_handle_event/{couponId}")
    @ApiOperation(value = "获取最新下线的事件", notes = "获取最新下线的事件")
    public ServerResponseEntity<OfflineHandleEventVO> getOfflineHandleEvent(@PathVariable Long couponId) {
        return ServerResponseEntity.success(couponService.getOfflineHandleEvent(couponId));
    }

    @PostMapping("/audit")
    @ApiOperation(value = "审核活动", notes = "审核活动")
    public ServerResponseEntity<Void> audit(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        if (!Objects.equals(Constant.PLATFORM_SHOP_ID, AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        Coupon coupon = couponService.getByCouponId(offlineHandleEventDto.getHandleId());
        couponService.audit(offlineHandleEventDto, coupon);
        // 清除缓存
        couponService.removeCacheByShopId(coupon.getShopId(), coupon.getCouponId());
        return ServerResponseEntity.success();
    }

    @PostMapping("/audit_apply")
    @ApiOperation(value = "违规活动提交审核", notes = "违规活动提交审核")
    public ServerResponseEntity<Void> auditApply(@RequestBody OfflineHandleEventDTO offlineHandleEventDto) {
        couponService.auditApply(offlineHandleEventDto);
        return ServerResponseEntity.success();
    }

}
