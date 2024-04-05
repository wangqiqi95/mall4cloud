package com.mall4j.cloud.coupon.feign;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.coupon.dto.BindCouponDTO;
import com.mall4j.cloud.api.coupon.feign.CouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponDataVO;
import com.mall4j.cloud.api.coupon.vo.CouponUserCountDataVO;
import com.mall4j.cloud.api.coupon.vo.TCouponUserOrderDetailVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.product.vo.SpuCouponAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.constant.SuitableProdType;
import com.mall4j.cloud.coupon.mapper.CouponUserMapper;
import com.mall4j.cloud.coupon.model.Coupon;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.CouponUserService;
import com.mall4j.cloud.coupon.service.ReceiveCouponActivityService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.vo.CouponAppVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author FrozenWatermelon
 * @date 2020/12/17
 */
@RestController
public class CouponFeignController implements CouponFeignClient {

    @Autowired
    private CouponService couponService;
    @Autowired
    private MapperFacade mapperFacade;


    @Autowired
    private CouponUserService couponUserService;
    @Resource
    private ReceiveCouponActivityService receiveCouponActivityService;

    @Autowired
    private CouponUserMapper couponUserMapper;
    @Autowired
    TCouponUserService tCouponUserService;



    @Override
    public ServerResponseEntity<List<CouponDataVO>> getCouponListByCouponIds(List<Long> couponIds) {
        List<Coupon> coupons = couponService.getCouponListByCouponIds(couponIds);
        List<CouponDataVO> couponDataList = mapperFacade.mapAsList(coupons, CouponDataVO.class);
        return ServerResponseEntity.success(couponDataList);
    }

    @Override
    public ServerResponseEntity<List<TCouponUserOrderDetailVO>> getCouponListByOrderIds(List<Long> orderIds) {
        List<TCouponUserOrderDetailVO> couponDataList = tCouponUserService.getCouponsByOrderIds(orderIds);
        return ServerResponseEntity.success(couponDataList);
    }

    @Override
    public ServerResponseEntity<List<TCouponUserOrderDetailVO>> postCouponListByOrderIds(List<Long> orderIds) {
        List<TCouponUserOrderDetailVO> couponDataList = tCouponUserService.getCouponsByOrderIds(orderIds);
        return ServerResponseEntity.success(couponDataList);
    }

    @Override
    public ServerResponseEntity<List<TCouponUserOrderDetailVO>> getCouponListBypByOrderIds(List<Long> orderIds) {
        List<TCouponUserOrderDetailVO> couponDataList = tCouponUserService.getCouponsByOrderIds(orderIds);
        return ServerResponseEntity.success(couponDataList);
    }

    @Override
    public ServerResponseEntity<List<CouponDataVO>> getCouponListByCouponIdsAndPutOnStatus(List<Long> couponIds, Integer putOnStatus) {
        List<Coupon> coupons = couponService.getCouponListByCouponIdsAndPutOnStatus(couponIds,putOnStatus);
        return ServerResponseEntity.success(mapperFacade.mapAsList(coupons, CouponDataVO.class));
    }

    @Override
    public ServerResponseEntity<Void> batchBindCouponByCouponIds(BindCouponDTO bindCouponDTO) {
        couponService.batchBindCouponByCouponIds(bindCouponDTO.getCouponIds(), bindCouponDTO.getUserId());
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Integer> countCanUseCoupon() {

        return ServerResponseEntity.success(couponUserService.countCanUseCoupon(AuthUserContext.get().getUserId()));
    }

    @Override
    public ServerResponseEntity<List<SpuCouponAppVO>> couponOfSpuDetail(Long shopId, Long spuId) {
        // 获取已投放优惠券
        List<CouponAppVO> shopCoupons = couponService.getShopCouponList(shopId);
        List<CouponAppVO> coupons = couponService.getShopCouponList(Constant.PLATFORM_SHOP_ID);
        coupons.addAll(shopCoupons);
        if (CollUtil.isEmpty(coupons)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        coupons.sort(Comparator.comparingLong(CouponAppVO::getCashCondition));
        List<CouponAppVO> couponList = new ArrayList<>();
        for (CouponAppVO coupon : coupons) {
            if (Objects.equals(coupon.getSuitableProdType(), SuitableProdType.ALL_SPU.value()) || (coupon.getSpuIds().contains(spuId))) {
                couponList.add(coupon);
            }
            if (couponList.size() >= Constant.SIZE_OF_THREE) {
                break;
            }
        }
        return ServerResponseEntity.success(mapperFacade.mapAsList(couponList, SpuCouponAppVO.class));
    }

    @Override
    public ServerResponseEntity<CouponUserCountDataVO> countCouponUserByUserId(Long userId) {
        CouponUserCountDataVO couponUserCountDataVO = couponUserService.countCouponUserByUserId(userId);
        return ServerResponseEntity.success(couponUserCountDataVO);
    }

    @Override
    public ServerResponseEntity<List<Long>> countMemberCouponByParam(Long shopId,Date startTime,Date endTime) {
        return ServerResponseEntity.success(couponUserMapper.countMemberCouponByParam(shopId,startTime,endTime));
    }

    @Override
    public ServerResponseEntity<Void> handleSpuOffline(List<Long> spuIds, List<Long> shopIds) {
        couponService.handleSpuOffline(spuIds, shopIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> userReceiveCoupon(Long id, Long couponId, Long storeId, Long userId) {
        return receiveCouponActivityService.userReceive(id,couponId,storeId,userId);
    }

}
