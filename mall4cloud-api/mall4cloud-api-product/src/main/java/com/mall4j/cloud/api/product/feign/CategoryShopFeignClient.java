package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.bo.PlatformCommissionOrderItemBO;
import com.mall4j.cloud.api.product.dto.CategoryShopDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/5/24
 */
@FeignClient(value = "mall4cloud-product",contextId = "category-shop")
public interface CategoryShopFeignClient {


    /**
     * 根据分类，店铺，获取订单项的佣金比例
     * @param platformCommissionOrderItems 订单项平台佣金需要的参数
     * @return 订单项佣金比例
     */
    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/categoryShop/calculatePlatformCommission")
    ServerResponseEntity<List<PlatformCommissionOrderItemBO>> calculatePlatformCommission(@RequestBody List<PlatformCommissionOrderItemBO>  platformCommissionOrderItems);

    /**
     * 根据店铺id批量保存分类签约信息
     * @param categoryShopDTOList
     * @param shopId
     * @return
     */
    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/categoryShop/insertBatchByShopId")
    ServerResponseEntity<Void> insertBatchByShopId(@RequestBody List<CategoryShopDTO> categoryShopDTOList, @RequestParam(value = "shopId") Long shopId);
}
