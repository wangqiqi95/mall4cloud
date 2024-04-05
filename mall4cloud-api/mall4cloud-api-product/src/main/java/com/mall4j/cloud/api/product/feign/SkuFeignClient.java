package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.vo.SkuCodeVO;
import com.mall4j.cloud.common.product.vo.SkuAddrVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.vo.app.SkuAppVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
@FeignClient(value = "mall4cloud-product",contextId = "sku")
public interface SkuFeignClient {

    /**
     * 通过skuId获取sku信息
     * @param skuId skuId
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/sku/getById")
    ServerResponseEntity<SkuVO> getById(@RequestParam("skuId") Long skuId);

    /**
     * 通过skuId获取sku信息
     * @param skuId skuId
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/getById")
    ServerResponseEntity<SkuVO> insiderGetById(@RequestParam("skuId") Long skuId);


    /**
     * 通过SkuCode获取sku信息
     * @param skuCode
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/sku/getBySkuCode")
    ServerResponseEntity<SkuVO> getBySkuCode(@RequestParam("skuCode") String skuCode);

    /**
     * 通过SkuCode获取sku信息
     * @param skuCode
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/getBySkuCode")
    ServerResponseEntity<SkuVO> insiderGetBySkuCode(@RequestParam("skuCode") String skuCode);

    /**
     * 通过spuId获取sku信息
     * @param spuId spuId
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/listBySpuId")
    ServerResponseEntity<List<SkuAppVO>> listBySpuId(@RequestParam("spuId") Long spuId);

    /**
     * 通过spuId&storeId获取sku信息
     * @param spuId spuId
     * @param storeId storeId
     * @return sku信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/listBySpuIdAndStoreId")
    ServerResponseEntity<List<SkuAppVO>> listBySpuIdAndStoreId(@RequestParam("spuId") Long spuId, @RequestParam("storeId") Long storeId);


    /**
     * 根据skuId列表获取商品列表
     * @param skuIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/listSpuDetailByIds")
    ServerResponseEntity<List<SkuAddrVO>> listSpuDetailByIds(@RequestBody List<Long> skuIds);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/code")
    ServerResponseEntity<SkuCodeVO> getCodeBySkuId(@RequestParam("skuId") Long skuId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/sku/listSkuCodeByIds")
    ServerResponseEntity<List<SkuVO>> listSkuCodeByIds(@RequestBody List<Long> skuIds);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/listSkuCodeBypByIds")
    ServerResponseEntity<List<SkuVO>> listSkuCodeBypByIds(@RequestBody List<Long> skuIds);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/listSkusBySpuId")
    ServerResponseEntity<List<SkuVO>> listSkusBySpuId(@RequestBody List<Long> spuIds);
    
    /**
     * 根据priceCodeList获取SkuList
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sku/getSkusByPriceCodeList")
    ServerResponseEntity<List<SkuVO>> getSkusByPriceCodeList(@RequestBody List<String> priceCodes);
}
