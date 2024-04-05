package com.mall4j.cloud.common.order.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.ShopInfoInOrderVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-multishop",contextId = "product-shop-detail")
public interface OrderShopDetailFeignClient {


    /**
     * 根据店铺id获取店铺在订单当中的信息
     * @param shopId 店铺id
     * @return 店铺名称
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/shopDetail/getShopInfoInOrderByShopId")
    ServerResponseEntity<ShopInfoInOrderVO> getShopInfoInOrderByShopId(@RequestParam("shopId") Long shopId);

}
