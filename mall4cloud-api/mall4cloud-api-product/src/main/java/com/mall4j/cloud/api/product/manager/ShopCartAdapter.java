package com.mall4j.cloud.api.product.manager;

import com.google.common.collect.Lists;
import com.mall4j.cloud.common.order.feign.OrderShopDetailFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车适配器
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@Component
public class ShopCartAdapter {

    @Autowired
    private OrderShopDetailFeignClient orderShopDetailFeignClient;

    /**
     * 将参数转换成组装好的购物项
     * @param shopCartItems 订单参数
     * @param storeId
     * @return 组装好的购物项
     */
    public List<ShopCartVO> conversionShopCart(List<ShopCartItemVO> shopCartItems){

        // 根据店铺ID划分item
        Map<Long, List<ShopCartItemVO>> shopCartMap = shopCartItems.stream().collect(Collectors.groupingBy(ShopCartItemVO::getShopId));

        // 返回一个店铺的所有信息
        List<ShopCartVO> shopCarts = Lists.newArrayList();
        for (Long shopId : shopCartMap.keySet()) {
            // 构建每个店铺的购物车信息
            ShopCartVO shopCart = buildShopCart(shopId,shopCartMap.get(shopId));
            shopCart.setShopId(shopId);
            // 店铺信息
            ServerResponseEntity<ShopInfoInOrderVO> shopNameResponse = orderShopDetailFeignClient.getShopInfoInOrderByShopId(shopId);
            if (!shopNameResponse.isSuccess()) {
                throw new LuckException(shopNameResponse.getMsg());
            }
            ShopInfoInOrderVO shopInfo = shopNameResponse.getData();
            shopCart.setShopName(shopInfo.getShopName());
            shopCart.setShopType(shopInfo.getType());
            shopCarts.add(shopCart);
        }

        return shopCarts;
    }


    private ShopCartVO buildShopCart(Long shopId, List<ShopCartItemVO> shopCartItems) {

        ShopCartVO shopCart = new ShopCartVO();
        shopCart.setShopReduce(0L);
        shopCart.setDiscountReduce(0L);
        shopCart.setShopId(shopId);
        long total = 0L;
        long actualTotal = 0L;
        long reduce = 0L;
        int totalCount = 0;
        long scoreTotal = 0L;

        for (ShopCartItemVO shopCartItem : shopCartItems) {
            total += shopCartItem.getTotalAmount();
            actualTotal += shopCartItem.getActualTotal();
            reduce += shopCartItem.getShareReduce() != null ? shopCartItem.getShareReduce() : 0;
            totalCount += shopCartItem.getCount();
            scoreTotal += shopCartItem.getScorePrice();
        }

        ShopCartItemDiscountVO shopCartItemDiscountVO = new ShopCartItemDiscountVO();
        shopCartItemDiscountVO.setShopCartItems(shopCartItems);

        shopCart.setShopCartItemDiscounts(Collections.singletonList(shopCartItemDiscountVO));
        shopCart.setTotal(total);
        shopCart.setTotalCount(totalCount);
        shopCart.setActualTotal(actualTotal);
        shopCart.setDiscountReduce(0L);
        shopCart.setShopReduce(reduce);
        shopCart.setScoreTotal(scoreTotal);
        return shopCart;
    }
}
