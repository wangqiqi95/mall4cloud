package com.mall4j.cloud.api.product.feign;


import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author YXF
 * @date 2021/05/10
 */
@FeignClient(value = "mall4cloud-product",contextId = "brand")
public interface BrandFeignClient {

    /**
     * 获取品牌信息
     * @param brandId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/brand/getByBrandId")
    ServerResponseEntity<BrandVO> getInfo(@RequestParam("brandId") Long brandId);

    /**
     * 根据店铺id把自定义品牌更新为平台品牌
     * @param shopId
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/brand/updateCustomBrandToPlatformBrandByShopId")
    ServerResponseEntity<Void> updateCustomBrandToPlatformBrandByShopId(@RequestParam("brandId") Long shopId);
}
