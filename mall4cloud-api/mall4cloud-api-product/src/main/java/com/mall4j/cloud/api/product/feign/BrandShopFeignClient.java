package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.dto.BrandShopDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/7/12 9:08
 */
@FeignClient(value = "mall4cloud-product",contextId = "brand-shop")
public interface BrandShopFeignClient {

    /**
     * 根据店铺id批量保存品牌签约信息
     * @param brandShopDTOList
     * @param shopId
     * @return
     */
    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/brandShop/insertBatchByShopId")
    ServerResponseEntity<Void> insertBatchByShopId(@RequestBody List<BrandShopDTO> brandShopDTOList, @RequestParam(value = "shopId") Long shopId);
}
