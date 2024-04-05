package com.mall4j.cloud.api.order.manager;

import com.mall4j.cloud.common.cache.constant.CacheNames;
import com.mall4j.cloud.common.cache.constant.OrderCacheNames;
import com.mall4j.cloud.common.cache.util.CacheManagerUtil;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.order.bo.DeliveryModeBO;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 确认订单适配
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Component
public class ConfirmOrderManager {

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    /**
     * 过滤物流不同的商品
     *
     * @param dbShopCartItems 用户选择的商品项
     * @return 返回过滤掉的商品项item
     */
    public List<ShopCartItemVO> filterShopItemsByType(ShopCartOrderMergerVO shopCartOrderMerger, List<ShopCartItemVO> dbShopCartItems) {
        Integer dvyType = shopCartOrderMerger.getDvyType();
        List<ShopCartItemVO> cartItems = new ArrayList<>();
        List<ShopCartItemVO> filterItems = new ArrayList<>();
        for (ShopCartItemVO shopCartItem : dbShopCartItems) {
            DeliveryModeBO deliveryModeVO = shopCartItem.getDeliveryModeBO();
            boolean hasShopDelivery = deliveryModeVO.getHasShopDelivery() != null && Objects.equals(dvyType, DeliveryType.DELIVERY.value()) && deliveryModeVO.getHasShopDelivery();
            boolean hasCityDelivery = deliveryModeVO.getHasUserPickUp() != null && Objects.equals(dvyType, DeliveryType.STATION.value()) && deliveryModeVO.getHasUserPickUp();
            boolean hasUserPickUp = deliveryModeVO.getHasCityDelivery() != null && Objects.equals(dvyType, DeliveryType.SAME_CITY.value()) && deliveryModeVO.getHasCityDelivery();
            if (hasShopDelivery || hasUserPickUp || hasCityDelivery) {
                // 设置平台分摊金额，为计算完商家之后计算平台优惠做准备
                shopCartItem.setPlatformShareReduce(0L);
                cartItems.add(shopCartItem);
            } else {
                filterItems.add(shopCartItem);
            }
        }
        shopCartOrderMerger.setFilterShopItems(filterItems);
        return cartItems;
    }

    /**
     * 当算完一遍店铺的各种满减活动时，重算一遍订单金额
     */
    public void recalculateAmountWhenFinishingCalculateShop(ShopCartOrderMergerVO shopCartOrderMerger, List<ShopCartVO> shopCarts, UserDeliveryInfoVO userDeliveryInfo) {

//        Map<Long, ShopTransFeeVO> shopIdWithShopTransFee = userDeliveryInfo.getShopIdWithShopTransFee();
//        Long totalTransfee1 = userDeliveryInfo.getTotalTransfee();

        // 所有店铺的订单信息
        List<ShopCartOrderVO> shopCartOrders = new ArrayList<>();

        long actualTotal = 0;
        long total = 0;
        int totalCount = 0;
        long allOrderReduce = 0;
        long totalTransfee = 0;
        long scoreTotal = 0L;

        // 所有店铺所有的商品item
        for (ShopCartVO shopCart : shopCarts) {
            // 每个店铺的订单信息
            ShopCartOrderVO shopCartOrder = new ShopCartOrderVO();
            shopCartOrder.setOrderType(shopCartOrderMerger.getOrderType().value());
            shopCartOrder.setShopId(shopCart.getShopId());
            shopCartOrder.setShopName(shopCart.getShopName());
            shopCartOrder.setShopType(shopCart.getShopType());
            shopCartOrder.setShopReduce(shopCart.getShopReduce());
            total += shopCart.getTotal();
            totalCount += shopCart.getTotalCount();
            allOrderReduce += shopCart.getShopReduce();

            // 积分订单使用积分
            if (Objects.equals(shopCartOrderMerger.getOrderType(), OrderType.SCORE)) {
                shopCartOrder.setUseScore(shopCart.getScoreTotal());
            }

            // 如果某家店因为各种优惠活动满减，使得金额变为负数，则这家店最低应该支付一分钱，而里面最贵的那件商品，也是支付一分钱，优惠金额 = （商品金额 - 一分钱）
            if (shopCart.getActualTotal() < 1L) {
                shopCartOrder.setActualTotal(1L);
                shopCartOrder.setShopReduce(shopCart.getTotal() - 1L);

                List<ShopCartItemVO> shopCartShopCartItem = getShopShopCartItem(shopCart.getShopCartItemDiscounts());
                resetActualTotal(shopCartShopCartItem);
            }
            //店铺优惠金额 = 优惠金额
            shopCartOrder.setShopReduce(shopCartOrder.getShopReduce());

            // 要计算运费
            totalTransfee = userDeliveryInfo.getTotalTransfee();
            // 店铺的实付 = 购物车实付 + 运费
            shopCartOrder.setActualTotal(shopCart.getActualTotal() + totalTransfee);
            // 运费
            shopCartOrder.setTransfee(totalTransfee);

            // 店铺优惠券优惠金额
            shopCartOrder.setCouponReduce(shopCart.getCouponReduce());
            shopCartOrder.setShopCartItemDiscounts(shopCart.getShopCartItemDiscounts());
            shopCartOrder.setCoupons(shopCart.getCoupons());
            shopCartOrder.setTotal(shopCart.getTotal());
            shopCartOrder.setTotalCount(shopCart.getTotalCount());
            actualTotal += shopCartOrder.getActualTotal();
            if (Objects.equals(shopCartOrderMerger.getOrderType(), OrderType.SCORE)) {
                shopCartOrder.setUseScore(shopCart.getScoreTotal());
            } else {
                shopCartOrder.setUseScore(shopCartOrderMerger.getUsableScore());
            }
            shopCartOrder.setDiscountReduce(shopCart.getDiscountReduce());
            shopCartOrders.add(shopCartOrder);
            scoreTotal += shopCart.getScoreTotal();
        }

        shopCartOrderMerger.setActualTotal(actualTotal);
        shopCartOrderMerger.setTotal(total);
        shopCartOrderMerger.setTotalCount(totalCount);
        shopCartOrderMerger.setOrderReduce(allOrderReduce);
        shopCartOrderMerger.setTotalTransfee(totalTransfee);
        shopCartOrderMerger.setShopCartOrders(shopCartOrders);
        shopCartOrderMerger.setUserAddr(userDeliveryInfo.getUserAddr());
        shopCartOrderMerger.setShopCityStatus(userDeliveryInfo.getShopCityStatus());
        //原运费金额
//        shopCartOrderMerger.setFreeTransfee(userDeliveryInfo.getTotalFreeTransfee());
        if (Objects.equals(shopCartOrderMerger.getOrderType(), OrderType.SCORE)) {
            shopCartOrderMerger.setUsableScore(scoreTotal);
        }
    }

    /**
     * 结束平台优惠的计算之后，还要重算一遍金额
     */
    public void recalculateAmountWhenFinishingCalculatePlatform(ShopCartOrderMergerVO shopCartOrderMerger, UserDeliveryInfoVO userDeliveryInfo) {
        Map<Long, ShopTransFeeVO> shopIdWithShopTransFee = userDeliveryInfo.getShopIdWithShopTransFee();
        //所有订单实际金额
        long actualTotal = 0L;
        //所有订单优惠金额
        long allOrderReduce = 0L;
        long totalTransfee = 0L;

        List<ShopCartOrderVO> shopCartOrders = shopCartOrderMerger.getShopCartOrders();

        for (ShopCartOrderVO shopCartOrder : shopCartOrders) {

            //订单实际金额
            long orderActualTotal = 0L;
            //商家优惠金额
            long orderReduceAmount = 0L;
            //商家优惠金额
            long orderPlatformAmount = 0L;

            List<ShopCartItemVO> shopCartShopCartItem = getShopShopCartItem(shopCartOrder.getShopCartItemDiscounts());

            for (ShopCartItemVO shopCartItem : shopCartShopCartItem) {
                shopCartItem.setShareReduce(shopCartItem.getShareReduce() + shopCartItem.getPlatformShareReduce());
                shopCartItem.setActualTotal(shopCartItem.getTotalAmount() - shopCartItem.getShareReduce());

                orderActualTotal += shopCartItem.getActualTotal();
                orderReduceAmount += shopCartItem.getShareReduce();
                orderPlatformAmount += shopCartItem.getPlatformShareReduce();
            }

            // 如果是折扣特别小的情况下，导致实际金额为0是，改变最小支付金额为0.01元,并且优惠金额减去0.01。
            // 此时的实际支付金额已经包含运费了
            if (shopCartOrder.getActualTotal() < 1) {
                // 重算订单项
                resetActualTotal(shopCartShopCartItem);
                shopCartOrder.setActualTotal(1L);
                shopCartOrder.setShopReduce(shopCartOrder.getTotal() - 1L);
            }

//            // 要计算运费
//            if (shopIdWithShopTransFee != null) {
//                ShopTransFeeVO shopTransFeeVO = shopIdWithShopTransFee.get(shopCartOrder.getShopId());
//
//                // 店铺的实付 = 购物车实付 + 运费
//                shopCartOrder.setActualTotal(shopCartOrder.getActualTotal() + shopTransFeeVO.getTransfee());
//                // 店铺免运费金额
//                shopCartOrder.setFreeTransfee(shopTransFeeVO.getFreeTransfee());
//
//                //店铺优惠金额 = 优惠金额  + 减免运费
//                shopCartOrder.setShopReduce(shopCartOrder.getShopReduce() + shopTransFeeVO.getFreeTransfee());
//
//                // 运费
//                shopCartOrder.setTransfee(shopTransFeeVO.getTransfee());
//            } else {
//
//                // 店铺的实付 = 购物车实付 + 运费
//                shopCartOrder.setActualTotal(orderActualTotal + shopCartOrder.getTransfee());
//            }
            if (shopCartOrder.getLevelFreeTransfee() != null && shopCartOrder.getLevelFreeTransfee() != 0L) {

                // 店铺先说包邮，然后平台再说会员包邮，所以实际上平台的包邮剩下来的邮费 = shopCartOrder.getLevelFreeTransfee() - shopCartOrder.getTransfee()

                Long transfee = shopCartOrder.getLevelFreeTransfee() - shopCartOrder.getTransfee();
                //放入平台优惠金额,如果用户等级免自营店运费也要放进去
                shopCartOrder.setPlatformAmount(shopCartOrder.getPlatformAmount() + transfee);
                shopCartOrder.setActualTotal(orderActualTotal - transfee);
            }


            actualTotal += shopCartOrder.getActualTotal();
            allOrderReduce += shopCartOrder.getShopReduce();
            totalTransfee += shopCartOrder.getTransfee();
        }
        shopCartOrderMerger.setActualTotal(actualTotal);
        shopCartOrderMerger.setTotalTransfee(totalTransfee);
        shopCartOrderMerger.setOrderReduce(allOrderReduce);
        shopCartOrderMerger.setUserAddr(userDeliveryInfo.getUserAddr());
        shopCartOrderMerger.setShopCityStatus(userDeliveryInfo.getShopCityStatus());
    }


    /**
     * 重新计算金额，避免订单无法支付
     */
    private void resetActualTotal(List<ShopCartItemVO> shopCartShopCartItem) {
        Iterator<ShopCartItemVO> iterator = shopCartShopCartItem.iterator();
        while (iterator.hasNext()) {
            ShopCartItemVO shopCartItem = iterator.next();
            shopCartItem.setShareReduce(shopCartItem.getShareReduce() + shopCartItem.getPlatformShareReduce());
            shopCartItem.setActualTotal(shopCartItem.getTotalAmount() - shopCartItem.getShareReduce());
            if (iterator.hasNext()) {
                shopCartItem.setActualTotal(0L);
                shopCartItem.setShareReduce(shopCartItem.getTotalAmount());
            } else {
                shopCartItem.setActualTotal(1L);
                shopCartItem.setShareReduce(shopCartItem.getTotalAmount() - 1L);
            }
        }
    }

    /**
     * 获取店铺下的所有订单
     */
    private List<ShopCartItemVO> getShopShopCartItem(List<ShopCartItemDiscountVO> shopCartItemDiscounts) {
        List<ShopCartItemVO> shopCartShopCartItem = new ArrayList<>();
        for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
            shopCartShopCartItem.addAll(shopCartItemDiscount.getShopCartItems());
        }
        shopCartShopCartItem.sort(Comparator.comparingDouble(ShopCartItemVO::getTotalAmount));
        return shopCartShopCartItem;
    }


    public void cacheCalculatedInfo(Long userId, ShopCartOrderMergerVO shopCartOrderMerger) {
        // 防止重复提交
        RedisUtil.STRING_REDIS_TEMPLATE.opsForValue().set(OrderCacheNames.ORDER_CONFIRM_UUID_KEY + CacheNames.UNION + userId, String.valueOf(userId));
        // 保存订单计算结果缓存，省得重新计算 并且 用户确认的订单金额与提交的一致
        cacheManagerUtil.putCache(OrderCacheNames.ORDER_CONFIRM_KEY, String.valueOf(userId), shopCartOrderMerger);
    }


}
