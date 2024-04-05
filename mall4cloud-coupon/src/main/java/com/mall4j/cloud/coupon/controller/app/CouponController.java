package com.mall4j.cloud.coupon.controller.app;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.api.vo.EsPageVO;
import com.mall4j.cloud.common.product.vo.search.ProductSearchVO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.SuitableProdType;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.CouponUserService;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 优惠券
 *
 * @author YXF
 * @date 2020-12-08 17:22:56
 */
@RestController("appCouponController")
@RequestMapping("/ma/coupon")
@Api(tags = "app-优惠券")
public class CouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponUserService couponUserService;


    @GetMapping("/general_coupon_list")
    @ApiOperation(value = "平台优惠券列表(领券中心, 访客接口)", notes = "平台优惠券列表")
    public ServerResponseEntity<List<CouponAppVO >> generalCouponList() {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Long userId = null;
        if (Objects.nonNull(userInfoInTokenBO)) {
            userId = userInfoInTokenBO.getUserId();
        }
        return ServerResponseEntity.success(couponService.generalCouponList(userId));
    }

    @GetMapping("/get_coupon_page")
    @ApiOperation(value = "店铺商品券列表(领券中心, 访客接口)", notes = "获取店铺商品券列表")
    public ServerResponseEntity<PageVO<CouponAppVO>> getCouponPage(@Valid PageDTO page) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Long userId = null;
        if (Objects.nonNull(userInfoInTokenBO)) {
            userId = userInfoInTokenBO.getUserId();
        }
        PageVO<CouponAppVO> couponDto = couponService.getProdCouponPage(page, userId);
        return ServerResponseEntity.success(couponDto);
    }

    @GetMapping("/get_shop_or_spu_coupon_list")
    @ApiOperation(value = "获取指定店铺或指定商品的所有优惠券 ", notes = "通过shopId或shopId和spuId获取指定店铺或指定商品的所有优惠券")
    public ServerResponseEntity<List<CouponAppVO>> getShopOrSpuCouponList(Long shopId, Long spuId) {
        // 获取已投放优惠券
        List<CouponAppVO> coupons = couponService.getShopCouponList(shopId);
        if (Objects.isNull(spuId)) {
            return ServerResponseEntity.success(coupons);
        }
        // 过滤指定商品参与
        coupons.addAll(couponService.getShopCouponList(Constant.PLATFORM_SHOP_ID));
        Iterator<CouponAppVO> iterator = coupons.iterator();
        List<Long> couponIds = new ArrayList<>();
        while (iterator.hasNext()) {
            CouponAppVO coupon = iterator.next();
            if (Objects.equals(coupon.getSuitableProdType(), SuitableProdType.ALL_SPU.value()) || (coupon.getSpuIds().contains(spuId))) {
                couponIds.add(coupon.getCouponId());
                continue;
            }
            iterator.remove();
        }
        couponUserService.listByAndShopIdOrSpuId(coupons, couponIds);
        return ServerResponseEntity.success(coupons);
    }


    @GetMapping("/get_spus_by_coupon_id")
    @ApiOperation(value = "根据优惠券，获取可用的商品列表", notes = "根据优惠券，获取可用的商品列表")
    @ApiImplicitParam(name = "couponId", value = "优惠券id")
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> getSpusByCouponId(PageDTO pageDTO, ProductSearchDTO productSearch,
                                                                             @RequestParam("couponId") Long couponId) {
        return ServerResponseEntity.success(couponService.spuListByCouponId(pageDTO, productSearch, couponId));
    }
}
