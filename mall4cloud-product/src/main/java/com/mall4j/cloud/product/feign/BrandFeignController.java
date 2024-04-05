package com.mall4j.cloud.product.feign;

import com.mall4j.cloud.api.product.feign.BrandFeignClient;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.BrandService;
import com.mall4j.cloud.product.service.BrandShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author lhd
 * @date 2020/12/23
 */
@RestController
public class BrandFeignController implements BrandFeignClient {

    @Autowired
    private BrandService brandService;

    @Override
    public ServerResponseEntity<BrandVO> getInfo(Long brandId) {
        return ServerResponseEntity.success(brandService.getInfo(brandId));
    }

    @Override
    public ServerResponseEntity<Void> updateCustomBrandToPlatformBrandByShopId(Long shopId) {
        brandService.updateCustomBrandToPlatformBrandByShopId(shopId);
        return ServerResponseEntity.success();
    }
}
