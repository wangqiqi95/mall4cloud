package com.mall4j.cloud.product.feign;

import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.common.product.vo.SkuAddrVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
public class SkuFeignController implements SkuFeignClient {

    @Autowired
    private SkuService skuService;


    @Override
    public ServerResponseEntity<SkuVO> getById(Long skuId) {
        return ServerResponseEntity.success(skuService.getSkuBySkuId(skuId));
    }

    @Override
    public ServerResponseEntity<SkuVO> insiderGetById(Long skuId) {
        return ServerResponseEntity.success(skuService.getSkuBySkuId(skuId));
    }

    @Override
    public ServerResponseEntity<SkuVO> getBySkuCode(String skuCode) {
        return ServerResponseEntity.success(skuService.getSkuByCode(skuCode));
    }

    @Override
    public ServerResponseEntity<SkuVO> insiderGetBySkuCode(String skuCode) {
        return ServerResponseEntity.success(skuService.getSkuByCode(skuCode));
    }

    @Override
    public ServerResponseEntity<List<SkuAppVO>> listBySpuId(Long spuId) {
        return ServerResponseEntity.success(skuService.getSpuDetailSkuInfo(spuId, null));
    }

    @Override
    public ServerResponseEntity<List<SkuAppVO>> listBySpuIdAndStoreId(Long spuId, Long storeId) {
        return ServerResponseEntity.success(skuService.getSpuSkuInfo(spuId, storeId));
    }

    @Override
    public ServerResponseEntity<List<SkuAddrVO>> listSpuDetailByIds(List<Long> skuIds) {
        return ServerResponseEntity.success(skuService.listSpuDetailByIds(skuIds));
    }

    @Override
    public ServerResponseEntity<SkuCodeVO> getCodeBySkuId(Long skuId) {
        return ServerResponseEntity.success(skuService.getCodeBySkuId(skuId));
    }

    @Override
    public ServerResponseEntity<List<SkuVO>> listSkuCodeByIds(List<Long> skuIds) {
        return ServerResponseEntity.success(skuService.listSkuCodeByIds(skuIds));
    }

    @Override
    public ServerResponseEntity<List<SkuVO>> listSkuCodeBypByIds(List<Long> skuIds) {
        return ServerResponseEntity.success(skuService.listSkuCodeByIds(skuIds));
    }

    @Override
    public ServerResponseEntity<List<SkuVO>> listSkusBySpuId(List<Long> spuIds) {
        return ServerResponseEntity.success(skuService.listSkusBySpuId(spuIds));
    }
    
    @Override
    public ServerResponseEntity<List<SkuVO>> getSkusByPriceCodeList(List<String> priceCodes) {
        return ServerResponseEntity.success(skuService.getSkusByPriceCodeList(priceCodes));
    }
}
