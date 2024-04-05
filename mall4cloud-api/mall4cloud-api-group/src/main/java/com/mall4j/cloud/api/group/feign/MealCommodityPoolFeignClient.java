package com.mall4j.cloud.api.group.feign;

import com.mall4j.cloud.api.group.feign.dto.CommodityPoolAddDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mall4cloud-group", contextId = "commodityPool")
public interface MealCommodityPoolFeignClient {


    /**
     * 添加商品池
     *
     * @param poolAddDTO
     * @return
     */
    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/commodityPool/add")
    ServerResponseEntity<String> commodityPoolAdd(@RequestBody CommodityPoolAddDTO poolAddDTO);

    /**
     * 添加商品池
     *
     * @param poolAddDTO
     * @return
     */
    @PostMapping(FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/commodityPool/remove")
    ServerResponseEntity<String> commodityPoolRemove(@RequestBody CommodityPoolAddDTO poolAddDTO);
}
