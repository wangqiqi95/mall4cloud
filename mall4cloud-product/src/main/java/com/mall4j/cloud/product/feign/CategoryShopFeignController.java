package com.mall4j.cloud.product.feign;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.product.bo.PlatformCommissionOrderItemBO;
import com.mall4j.cloud.api.product.dto.CategoryShopDTO;
import com.mall4j.cloud.api.product.feign.CategoryShopFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.bo.CategoryRateBO;
import com.mall4j.cloud.product.service.CategoryService;
import com.mall4j.cloud.product.service.CategoryShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2021/5/24
 */
@RestController
public class CategoryShopFeignController implements CategoryShopFeignClient {

    @Autowired
    private CategoryShopService categoryShopService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public ServerResponseEntity<List<PlatformCommissionOrderItemBO>> calculatePlatformCommission(List<PlatformCommissionOrderItemBO> platformCommissionOrderItems) {

        // 获取整个平台的佣金比例
        List<CategoryRateBO> categoryRates = categoryService.listRate();
        // 如果为空表示为从店铺改价过来，需要先获取分类ids
        if(Objects.isNull(platformCommissionOrderItems.get(0).getCategoryId())){
            List<PlatformCommissionOrderItemBO> rateOrderItemList = categoryService.listBySkuIds(platformCommissionOrderItems);
            Map<Long, Long> rateMap = rateOrderItemList.stream().collect(Collectors.toMap(PlatformCommissionOrderItemBO::getSkuId, PlatformCommissionOrderItemBO::getCategoryId));
            for (PlatformCommissionOrderItemBO platformCommissionOrderItem : platformCommissionOrderItems) {
                platformCommissionOrderItem.setCategoryId(rateMap.get(platformCommissionOrderItem.getSkuId()));
            }
        }
        for (PlatformCommissionOrderItemBO platformCommissionOrderItem : platformCommissionOrderItems) {
            for (CategoryRateBO categoryRate : categoryRates) {
                // 分类id相同，用平台的佣金
                if (Objects.equals(platformCommissionOrderItem.getCategoryId(), categoryRate.getCategoryId())) {
                    platformCommissionOrderItem.setRate(categoryRate.getRate());
                }

            }
        }
        Map<Long, List<PlatformCommissionOrderItemBO>> shopIdWithPlatformCommissionMap = platformCommissionOrderItems.stream().collect(Collectors.groupingBy(PlatformCommissionOrderItemBO::getShopId));


        // 如果店铺有特定的比例，就用店铺的比例
        for (Long shopId : shopIdWithPlatformCommissionMap.keySet()) {

            // 获取店铺各个分类的费率
            List<CategoryRateBO> shopCategoryRates = categoryShopService.listRateByShopId(shopId);
            if (CollectionUtil.isEmpty(shopCategoryRates)) {
                continue;
            }
            List<PlatformCommissionOrderItemBO> shopPlatformCommissionOrderItems = shopIdWithPlatformCommissionMap.get(shopId);

            for (PlatformCommissionOrderItemBO shopPlatformCommissionOrderItem : shopPlatformCommissionOrderItems) {
                for (CategoryRateBO categoryRate : shopCategoryRates) {
                    // 分类id相同，用平台的佣金
                    if (Objects.equals(shopPlatformCommissionOrderItem.getCategoryId(), categoryRate.getCategoryId()) && categoryRate.getRate() != null) {
                        shopPlatformCommissionOrderItem.setRate(categoryRate.getRate());
                    }
                }
            }
        }

        return ServerResponseEntity.success(platformCommissionOrderItems);
    }

    @Override
    public ServerResponseEntity<Void> insertBatchByShopId(List<CategoryShopDTO> categoryShopDTOList, Long shopId) {
        categoryShopService.insertBatchByShopId(categoryShopDTOList, shopId);
        return ServerResponseEntity.success();
    }
}
