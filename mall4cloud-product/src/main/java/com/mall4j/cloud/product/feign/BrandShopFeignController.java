package com.mall4j.cloud.product.feign;

import com.mall4j.cloud.api.product.dto.BrandShopDTO;
import com.mall4j.cloud.api.product.feign.BrandShopFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.BrandShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/7/12 9:15
 */
@RestController
public class BrandShopFeignController implements BrandShopFeignClient {

    @Autowired
    private BrandShopService brandShopService;

    @Override
    public ServerResponseEntity<Void> insertBatchByShopId(List<BrandShopDTO> brandShopDTOList, Long shopId) {
        brandShopService.insertBatchByShopId(brandShopDTOList, shopId);
        return ServerResponseEntity.success();
    }
}
