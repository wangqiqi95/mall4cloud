package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.dto.ErpPriceDTO;
import com.mall4j.cloud.api.product.dto.ErpStockDTO;
import com.mall4j.cloud.api.product.dto.ErpSyncDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.bo.EsProductBO;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
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
@FeignClient(value = "mall4cloud-product",contextId = "product")
public interface ProductFeignClient {

    /**
     * 通过spuId需要搜索的商品
     * @param spuId spuid
     * @return es保存的商品信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/loadEsProductBO")
    ServerResponseEntity<EsProductBO> loadEsProductBO(@RequestParam("spuId") Long spuId);

    /**
     * 根据平台categoryId，获取spuId列表
     * @param shopCategoryIds
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByShopCategoryIds")
    ServerResponseEntity<List<Long>> getSpuIdsByShopCategoryIds(@RequestParam("shopCategoryIds")List<Long> shopCategoryIds);

    /**
     * 根据categoryId列表，获取spuId列表
     * @param categoryIds
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByCategoryIds")
    ServerResponseEntity<List<Long>> getSpuIdsByCategoryIds(@RequestParam("categoryIds")List<Long> categoryIds);

    /**
     * 根据brandId，获取spuId列表
     * @param brandId
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByBrandId")
    ServerResponseEntity<List<Long>> getSpuIdsByBrandId(@RequestParam("brandId")Long brandId);

    /**
     * 根据店铺id，获取spuId列表
     * @param shopId
     * @return spuId列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuIdsByShopId")
    ServerResponseEntity<List<Long>> getSpuIdsByShopId(@RequestParam("shopId")Long shopId);

    /**
     * 获取spu分组列表
     * @param spuId
     * @return spu分组列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/getSpuTagBySpuId")
    ServerResponseEntity<List<Long>> getSpuTagBySpuId(@RequestParam("shopId") Long spuId);

    /**
     * 商品状态发生改变时，需要处理的事件
     *
     * @param spuId 商品id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/handleStatusChange")
    ServerResponseEntity<Void> handleStatusChange(@RequestParam("spuId") Long spuId);


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/erp/sync")
    ServerResponseEntity<Void> erpSync(@RequestBody ErpSyncDTO dto);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/price/sync")
    ServerResponseEntity<Void> priceSync(@RequestBody ErpPriceDTO erpPriceDto);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/stock/sync")
    ServerResponseEntity<Void> stockSync(@RequestBody ErpStockDTO erpStockDTO);

    /**
     * 商品搜索-通用查询
     * @param productSearch
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/commonSearch")
    ServerResponseEntity<PageVO<SpuCommonVO>> commonSearch(@RequestBody ProductSearchDTO productSearch);


    /**
     * 优惠券可用商品查询 ，按照销量倒叙排序 ，门店商品价格
     * @param productSearch
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchSpu/couponSearch")
    ServerResponseEntity<PageVO<SpuCommonVO>> couponSearch(@RequestBody ProductSearchDTO productSearch);

    /**
     * 爱铺货商品同步处理
     * @param spuDTO
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/product/iph/sync")
    ServerResponseEntity<Void> iphSync(@RequestBody SpuDTO spuDTO);
}
