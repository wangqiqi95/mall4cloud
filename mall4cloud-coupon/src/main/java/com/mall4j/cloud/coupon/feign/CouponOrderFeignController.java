package com.mall4j.cloud.coupon.feign;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.coupon.dto.ChooseCouponDTO;
import com.mall4j.cloud.api.coupon.dto.LockCouponDTO;
import com.mall4j.cloud.api.coupon.dto.PlatformChooseCouponDTO;
import com.mall4j.cloud.api.coupon.dto.TCouponActivityCentreParamDTO;
import com.mall4j.cloud.api.coupon.feign.CouponOrderFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponListVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.dto.UpdateCouponStatusDTO;
import com.mall4j.cloud.coupon.service.CouponLockService;
import com.mall4j.cloud.coupon.service.CouponService;
import com.mall4j.cloud.coupon.service.TCouponActivityCentreService;
import com.mall4j.cloud.coupon.service.TCouponUserService;
import com.mall4j.cloud.coupon.util.ChooseCouponHelper;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/12/17
 */
@RestController
public class CouponOrderFeignController implements CouponOrderFeignClient {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponLockService couponLockService;
    @Autowired
    private MapperFacade mapperFacade;
    @Resource
    private TCouponUserService tCouponUserService;
    @Autowired
    TCouponActivityCentreService couponActivityCentreService;

    @Override
    public ServerResponseEntity<List<ShopCartVO>> chooseShopCoupon(ChooseCouponDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        Long storeId = param.getStoreId();
        String couponCode = param.getCouponCode();
        List<ShopCartVO> shopCarts = param.getShopCarts();

        for (ShopCartVO shopCart : shopCarts) {
            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCart.getShopCartItemDiscounts();
            List<ShopCartItemVO> shopCartItems = new ArrayList<>();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                shopCartItems.addAll(shopCartItemDiscount.getShopCartItems());
            }

            for (ShopCartItemVO shopCartItem : shopCartItems) {
                //如果下单商品中包含虚拟门店活动设置了下单商品 指定优惠券可用
                // 判断 该用户是否有 下单商品参与的虚拟门店 指定可用优惠券 -> 如果有的话，将指定可用的优惠券设置到优惠券对象中。
                //如果参加了虚拟门店活动价格，并且设置了指定优惠券可用。
                if(shopCartItem.isInvateStorePriceFlag() && shopCartItem.getFriendlyCouponUseFlag()==3 &&
                        ObjectUtil.isNotNull(shopCartItem.getInvateStoreActivityId())){
                    TCouponActivityCentreParamDTO paramDTO = new TCouponActivityCentreParamDTO();
                    paramDTO.setActivitySource(3);
                    paramDTO.setActivityId(shopCartItem.getInvateStoreActivityId().longValue());
                    List<CouponListVO> couponListVOS = couponActivityCentreService.couponACList(paramDTO);
                    if(CollectionUtil.isNotEmpty(couponListVOS)){
                        List<Long> couponIds = couponListVOS.stream().map(CouponListVO::getId).collect(Collectors.toList());
                        shopCartItem.setInvateStoreCouponids(couponIds);
                    }
                }

            }
            // 因为经过满减，实际金额的顺序已经变了
            shopCartItems = shopCartItems.stream().sorted(Comparator.comparingDouble(ShopCartItemVO::getActualTotal)).collect(Collectors.toList());

            // 获取用户有效的优惠券
            // todo 考虑spuids传进来
            List<CouponOrderVO> shopCoupons = tCouponUserService.couponList(userId, storeId, shopCart.getActualTotal(), shopCartItems , couponCode);

            // 用户选中的所有优惠券
            List<String> couponIds = param.getCouponIds();

            List<String> availableCouponCodes = shopCoupons.stream().map(CouponOrderVO::getCouponCode).collect(Collectors.toList());
            
            Integer userChangeCoupon = param.getUserChangeCoupon();
    
            //有录入的纸质券码,选中这张优惠券
            if(StrUtil.isNotEmpty(couponCode)){
        
                // 如果纸质券码的优惠券,不在可用优惠券列表中,进行提示
                if(CollectionUtil.isEmpty(availableCouponCodes)  ||  !availableCouponCodes.contains(couponCode)){
                    Assert.faild("当前商品未满足使用条件，请查看纸质券使用细则");
                }
        
                //清空couponIds后,选中当前券码优惠券
                couponIds.clear();
                couponIds.add(couponCode);
                userChangeCoupon = 1;
            }
            
            //如果用户选择了优惠券。并且有可用优惠券 检查选择的优惠券是否可用。
            if(CollectionUtil.isNotEmpty(couponIds) && CollectionUtil.isNotEmpty(availableCouponCodes) && StrUtil.isNotEmpty(couponIds.get(0))){
                //这里可能因为选择了正价折扣券而导致会员日活动的问题。
                if(!availableCouponCodes.contains(couponIds.get(0))){
                    Assert.faild("券与活动商品冲突，请选择其它优惠券");
                }
            }
            //如果用户选择了优惠券。并且没有可用优惠券 提示优惠券冲突
            if(CollectionUtil.isEmpty(availableCouponCodes) && CollectionUtil.isNotEmpty(couponIds) && StrUtil.isNotEmpty(couponIds.get(0))){
                Assert.faild("券与活动商品冲突，请选择其它优惠券");
            }

            ChooseCouponHelper chooseCouponHelper = new ChooseCouponHelper(shopCartItems, shopCoupons, couponIds, userChangeCoupon, param.getTotalTransfee()).invoke();

            if (chooseCouponHelper.getChooseCoupon() != null) {

                long couponReduce = chooseCouponHelper.getCouponReduce();
                // 最后组装订单信息
                //订单总额重新赋值
                shopCart.setActualTotal(shopCartItems.stream().mapToLong(ShopCartItemVO::getActualTotal).sum());
                shopCart.setTotal(shopCartItems.stream().mapToLong(ShopCartItemVO::getTotalAmount).sum());
                //优惠券金额
                shopCart.setCouponReduce(couponReduce);
//                shopCart.setActualTotal(shopCart.getActualTotal() - shopCart.getCouponReduce());
                shopCart.setShopReduce(shopCart.getShopReduce() + shopCart.getCouponReduce());
            }

            //优惠券 计算完分摊金额清空优惠券中的 适用商品，防止对象太大返回前端。
            for (CouponOrderVO shopCoupon : shopCoupons) {
                shopCoupon.setSpuIds(null);
                shopCoupon.setPriceCodes(null);
            }
            shopCart.setCoupons(shopCoupons);
            shopCart.setTransfee(chooseCouponHelper.getTotalTransfee());
            // 优惠前原运费金额
//            shopCart.setFreeTransfee(param.getTotalTransfee());
        }
        return ServerResponseEntity.success(shopCarts);
    }

    @Override
    public ServerResponseEntity<List<ShopCartVO>> staffChooseShopCoupon(ChooseCouponDTO param) {
        Long userId = param.getUserId();
        Long storeId = param.getStoreId();

        List<ShopCartVO> shopCarts = param.getShopCarts();

        for (ShopCartVO shopCart : shopCarts) {
            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCart.getShopCartItemDiscounts();
            List<ShopCartItemVO> shopCartItems = new ArrayList<>();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                shopCartItems.addAll(shopCartItemDiscount.getShopCartItems());
            }
            // 因为经过满减，实际金额的顺序已经变了
            shopCartItems = shopCartItems.stream().sorted(Comparator.comparingDouble(ShopCartItemVO::getActualTotal)).collect(Collectors.toList());

            // 获取用户有效的优惠券
            List<CouponOrderVO> shopCoupons = tCouponUserService.couponList(userId, storeId, shopCart.getActualTotal(), shopCartItems, null);

            // 用户选中的所有优惠券
            List<String> couponIds = param.getCouponIds();

            Integer userChangeCoupon = param.getUserChangeCoupon();

            ChooseCouponHelper chooseCouponHelper = new ChooseCouponHelper(shopCartItems, shopCoupons, couponIds, userChangeCoupon, param.getTotalTransfee()).invoke();

            if (chooseCouponHelper.getChooseCoupon() != null) {

                long couponReduce = chooseCouponHelper.getCouponReduce();
                // 最后组装订单信息
                //订单总额重新赋值
                shopCart.setActualTotal(shopCartItems.stream().mapToLong(ShopCartItemVO::getActualTotal).sum());
                shopCart.setTotal(shopCartItems.stream().mapToLong(ShopCartItemVO::getTotalAmount).sum());
                //优惠券金额
                shopCart.setCouponReduce(couponReduce);
//                shopCart.setActualTotal(shopCart.getActualTotal() - shopCart.getCouponReduce());
                shopCart.setShopReduce(shopCart.getShopReduce() + shopCart.getCouponReduce());
            }

            shopCart.setCoupons(shopCoupons);
            shopCart.setTransfee(chooseCouponHelper.getTotalTransfee());
        }
        return ServerResponseEntity.success(shopCarts);
    }

    @Override
    public ServerResponseEntity<ShopCartOrderMergerVO> choosePlatformCoupon(PlatformChooseCouponDTO param) {
        Long userId = AuthUserContext.get().getUserId();
        ShopCartOrderMergerVO shopCartOrderMergerVO = param.getShopCartOrderMergerVO();

        // 获取用户可用平台优惠券
        List<CouponOrderVO> couponList = couponService.getCouponListByUserIdAndShopId(userId, 0L);
        if (CollectionUtil.isEmpty(couponList)) {
            return ServerResponseEntity.success(shopCartOrderMergerVO);
        }

        // 完整的订单信息
        // 订单项目
        List<ShopCartItemVO> shopAllShopCartItems = new ArrayList<>();
        List<ShopCartOrderVO> shopCartOrders = shopCartOrderMergerVO.getShopCartOrders();
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                shopAllShopCartItems.addAll(shopCartItemDiscount.getShopCartItems());
            }
        }
        // 因为经过满减，实际金额的顺序已经变了
        shopAllShopCartItems = shopAllShopCartItems.stream().sorted(Comparator.comparingDouble(ShopCartItemVO::getActualTotal)).collect(Collectors.toList());

        ChooseCouponHelper chooseCouponHelper = new ChooseCouponHelper(shopAllShopCartItems, couponList, param.getCouponIds(), param.getUserChangeCoupon(), Constant.ZERO_LONG).invoke();


        if (chooseCouponHelper.getChooseCoupon() != null) {
            Long couponReduce = chooseCouponHelper.getCouponReduce();
            Map<Long, Long> shopReduceMap = chooseCouponHelper.getShopReduceMap();
            for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
                Long couponAmount = shopReduceMap.get(shopCartOrder.getShopId());
                shopCartOrder.setPlatformCouponReduce(couponAmount);
                couponAmount = Objects.isNull(shopCartOrder.getPlatformAmount()) ? couponAmount : shopCartOrder.getPlatformAmount() + couponAmount;
                shopCartOrder.setPlatformAmount(couponAmount);
                shopCartOrder.setActualTotal(shopCartOrder.getActualTotal() - shopCartOrder.getPlatformCouponReduce());
                shopCartOrder.setShopReduce(shopCartOrder.getShopReduce() + shopCartOrder.getPlatformCouponReduce());
            }
            shopCartOrderMergerVO.setOrderReduce(shopCartOrderMergerVO.getOrderReduce() + couponReduce);
            shopCartOrderMergerVO.setActualTotal(shopCartOrderMergerVO.getActualTotal() - couponReduce);
        }
        shopCartOrderMergerVO.setCoupons(couponList);
        return ServerResponseEntity.success(shopCartOrderMergerVO);
    }

    @Override
    public ServerResponseEntity<ShopCartOrderMergerVO> staffChoosePlatformCoupon(PlatformChooseCouponDTO param) {
        ShopCartOrderMergerVO shopCartOrderMergerVO = param.getShopCartOrderMergerVO();

        // 获取用户可用平台优惠券
        List<CouponOrderVO> couponList = couponService.getCouponListByUserIdAndShopId(param.getUserId(), 0L);
        if (CollectionUtil.isEmpty(couponList)) {
            return ServerResponseEntity.success(shopCartOrderMergerVO);
        }

        // 完整的订单信息
        // 订单项目
        List<ShopCartItemVO> shopAllShopCartItems = new ArrayList<>();
        List<ShopCartOrderVO> shopCartOrders = shopCartOrderMergerVO.getShopCartOrders();
        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCartOrder.getShopCartItemDiscounts();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                shopAllShopCartItems.addAll(shopCartItemDiscount.getShopCartItems());
            }
        }
        // 因为经过满减，实际金额的顺序已经变了
        shopAllShopCartItems = shopAllShopCartItems.stream().sorted(Comparator.comparingDouble(ShopCartItemVO::getActualTotal)).collect(Collectors.toList());

        ChooseCouponHelper chooseCouponHelper = new ChooseCouponHelper(shopAllShopCartItems, couponList, param.getCouponIds(), param.getUserChangeCoupon(), Constant.ZERO_LONG).invoke();


        if (chooseCouponHelper.getChooseCoupon() != null) {
            Long couponReduce = chooseCouponHelper.getCouponReduce();
            Map<Long, Long> shopReduceMap = chooseCouponHelper.getShopReduceMap();
            for (ShopCartOrderVO shopCartOrder : shopCartOrders) {
                Long couponAmount = shopReduceMap.get(shopCartOrder.getShopId());
                shopCartOrder.setPlatformCouponReduce(couponAmount);
                couponAmount = Objects.isNull(shopCartOrder.getPlatformAmount()) ? couponAmount : shopCartOrder.getPlatformAmount() + couponAmount;
                shopCartOrder.setPlatformAmount(couponAmount);
                shopCartOrder.setActualTotal(shopCartOrder.getActualTotal() - shopCartOrder.getPlatformCouponReduce());
                shopCartOrder.setShopReduce(shopCartOrder.getShopReduce() + shopCartOrder.getPlatformCouponReduce());
            }
            shopCartOrderMergerVO.setOrderReduce(shopCartOrderMergerVO.getOrderReduce() + couponReduce);
            shopCartOrderMergerVO.setActualTotal(shopCartOrderMergerVO.getActualTotal() - couponReduce);
        }
        shopCartOrderMergerVO.setCoupons(couponList);
        return ServerResponseEntity.success(shopCartOrderMergerVO);
    }

    @Override
    public ServerResponseEntity<Void> lockCoupon(List<LockCouponDTO> lockCouponParams) {
        if (CollectionUtil.isEmpty(lockCouponParams)) {
            return ServerResponseEntity.success();
        }
        return couponLockService.lockCoupon(lockCouponParams);
    }

    @Override
    public ServerResponseEntity<Void> updateCouponStatus(com.mall4j.cloud.api.coupon.dto.UpdateCouponStatusDTO param) {
        UpdateCouponStatusDTO updateCouponStatusDTO = BeanUtil.copyProperties(param, UpdateCouponStatusDTO.class);
        tCouponUserService.updateCouponStatus(updateCouponStatusDTO);
        return ServerResponseEntity.success();
    }
}
