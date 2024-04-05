package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.factory.ShopCartFeginFallBackFactory;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
@FeignClient(value = "mall4cloud-product",contextId = "shopCart")
public interface ShopCartFeignClient {

    /**
     * 获取购物项
     * @return 购物项
     * @param storeId
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/shopCart/getById")
    ServerResponseEntity<List<ShopCartItemVO>> getCheckedShopCartItems(@RequestParam(value = "storeId",required = false,defaultValue = "1") Long storeId);

    /**
     * 通过购物车id删除用户购物车物品
     * @param shopCartItemIds 购物车id
     * @return
     */
    @DeleteMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/delete_item")
    ServerResponseEntity<Void> deleteItem(@RequestBody List<Long> shopCartItemIds);

    /**
     * 根据店铺id列表更新购物车店铺是否停业属性
     * @param shopIds
     * @param isClosed
     * @return
     */
    @PutMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/updateIsClosedByShopIds")
    ServerResponseEntity<Void> updateIsClosedByShopIds(@RequestBody List<Long> shopIds, @RequestParam(value = "isClosed") Integer isClosed);
}
