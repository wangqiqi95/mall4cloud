package com.mall4j.cloud.api.product.feign;

import com.mall4j.cloud.api.product.bo.SpuSimpleBO;
import com.mall4j.cloud.api.product.dto.*;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.api.product.vo.StdPushSpuVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.*;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
@FeignClient(value = "mall4cloud-product", contextId = "spu")
public interface SpuFeignClient {

    /**
     * 通过spuId需要搜索的商品
     *
     * @param spuId spuid
     * @return 商品信息(spuId, name, mainImgUrl)
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/getById")
    ServerResponseEntity<SpuVO> getById(@RequestParam("spuId") Long spuId);

    /**
     * 通过spuId需要搜索的商品
     *
     * @param spuId spuid
     * @return 商品信息(spuId, name, mainImgUrl)
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/getById")
    ServerResponseEntity<SpuVO> insiderGetById(@RequestParam("spuId") Long spuId);

    /**
     * 通过spuId需要搜索的商品
     *
     * @param spuId spuid
     * @return 商品信息(spuId, name, mainImgUrl)
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/getDetailById")
    ServerResponseEntity<SpuVO> getDetailById(@RequestParam("spuId") Long spuId);

    /**
     * 通过spuId需要搜索的商品
     *
     * @param spuId   spuId
     * @param skuId   skuId
     * @param storeId
     * @return 商品信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/getSpuAndSkuById")
    ServerResponseEntity<SpuAndSkuVO> getSpuAndSkuById(@RequestParam("spuId") Long spuId, @RequestParam("skuId") Long skuId, @RequestParam("storeId") Long storeId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/getEcSpuAndSkuById")
    ServerResponseEntity<SpuAndSkuVO> getEcSpuAndSkuById(@RequestParam("spuId") Long spuId, @RequestParam("skuId") Long skuId, @RequestParam("storeId") Long storeId);


    /**
     * 根据店铺id或者商品id或者为空直接获取可以参与秒杀活动的商品列表
     *
     * @param spuId  spuId
     * @param shopId 店铺id
     * @return 商品信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/listCanSeckillProd")
    ServerResponseEntity<List<SpuVO>> listCanSeckillProd(@RequestParam(value = "isFailure", defaultValue = "0") Long spuId,
                                                         @RequestParam(value = "shopId") Long shopId);

    /**
     * 获取商品活动信息
     *
     * @param shopId
     * @param spuId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/spuActivityBySpuId")
    ServerResponseEntity<SpuActivityAppVO> spuActivityBySpuId(@RequestParam("shopId") Long shopId, @RequestParam("spuId") Long spuId);

    /**
     * 下线spu
     *
     * @param shopId
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/offlineSpuByShopId")
    ServerResponseEntity<Void> offlineSpuByShopId(@RequestBody Long shopId);

    /**
     * 获取spu和sku信息
     *
     * @param spuId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/getSpuAndSkuBySpuId")
    ServerResponseEntity<SpuVO> getSpuAndSkuBySpuId(@RequestParam("spuId") Long spuId);

    /**
     * 更新商品类型
     *
     * @param spuDTO
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/changeSpuType")
    ServerResponseEntity<Void> changeSpuType(@RequestBody SpuDTO spuDTO);

    /**
     * 根据运费id获取商品数量
     *
     * @param transportId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/countByTransportId")
    ServerResponseEntity<Integer> countByTransportId(@RequestParam("spuId") Long transportId);

    /**
     * 删除商品活动缓存
     * @param shopId null:代表所有的店铺  not null: 指定到具体店铺（若不指定spuIds,则按店铺、平台清除商品的缓存）
     * @param spuIds 商品id列表不为空代表有指定删除缓存的商品列表，就不进行全店铺/平台的商品缓存清除
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/removeSpuActivityCache")
    void removeSpuActivityCache(@RequestParam("shopId") Long shopId, @RequestBody List<Long> spuIds);

    /**
     * 将商品变为普通商品
     *
     * @param spuIds 商品id
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/changeToNormalSpu")
    void changeToNormalSpu(@RequestBody List<Long> spuIds);

    /**
     * 根据用户id查询商品收藏数量
     *
     * @param userId 用户id
     * @return 商品收藏数量
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/spu/countByUserId")
    ServerResponseEntity<Integer> countByUserId(@RequestParam("userId") Long userId);

    /**
     * 根据商品id列表获取商品列表
     *
     * @param spuIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/listSpuNameByIds")
    ServerResponseEntity<List<SpuVO>> listSpuNameBySpuIds(@RequestBody List<Long> spuIds);

    /**
     * 根据商品id列表获取商品列表
     *
     * @param spuIds
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/listSpuNameBypBySpuIds")
    ServerResponseEntity<List<SpuVO>> listSpuNameBypBySpuIds(@RequestBody List<Long> spuIds);

    /**
     * 根据商品id列表获取商品列表
     *
     * @param spuIds
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/listSpuBySpuIds")
    ServerResponseEntity<List<SpuVO>> listSpuBySpuIds(@RequestBody List<Long> spuIds);

    /**
     * 根据商品id列表获取商品列表
     *
     * @param param
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/pageList")
    ServerResponseEntity<PageVO<SpuVO>> pageList(@RequestBody SpuListDTO param);

    /**
     * 根据商品code列表获取商品列表
     *
     * @param spuCodes
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/listSpuBySpuCodes")
    ServerResponseEntity<List<SpuCodeVo>> listSpuBySpuCodes(@RequestBody List<String> spuCodes);

    /**
     * 根据店铺id列表更新商品更新时间
     *
     * @param shopIds
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/updateSpuUpdateTime")
    ServerResponseEntity<Void> updateSpuUpdateTimeByShopIds(@RequestBody List<Long> shopIds);

    /**
     * 根据商品id列表下架商品
     *
     * @param shopIds
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/offlineSpuByShopIds")
    ServerResponseEntity<Void> offlineSpuActivityByShopIds(@RequestBody List<Long> shopIds);

    /**
     * 根据基本信息查询商品列表
     *
     * @param spuSimpleBO
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/listSimple")
    ServerResponseEntity<List<SpuSimpleBO>> listSimple(@RequestBody SpuSimpleBO spuSimpleBO);


    /**
     * 获取指定sku的价格
     *
     * @param skuIds
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/listSkuPriceByIds")
    ServerResponseEntity<List<SkuVO>> listSkuPriceByIds(@RequestBody List<Long> skuIds);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/listGiftSpuBySpuIds")
    ServerResponseEntity<List<SpuVO>> listGiftSpuBySpuIds(@RequestBody List<Long> spuIdList);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/isSpuSkuChannel")
    ServerResponseEntity<List<Long>> isSpuSkuChannel(@RequestBody SpuSkuRDTO spuSkuRDTO);

    /**
     * 推送erp商品
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/pushStdSpus")
    ServerResponseEntity<List<StdPushSpuVO>> pushStdSpus(@RequestBody List<String> spuCodes);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/getStoreSpuAndSkuPrice")
    ServerResponseEntity<List<SkuTimeDiscountActivityVO>> getStoreSpuAndSkuPrice(@RequestParam("storeId") Long storeId,
                                                                                 @RequestBody List<SpuSkuPriceDTO> skuPriceDTOs);

    /**
     * 根据spu批量上下架
     * @param dto
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/batchChangeSpuStatus")
    ServerResponseEntity<Void> batchChangeSpuStatus(@RequestBody ChangeSpuStatusDto dto);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/batchZeroSetChannelsStock")
    ServerResponseEntity<Void> batchZeroSetChannelsStock(@RequestBody ZeroSetStockDto dto);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/updateChannelsStock")
    ServerResponseEntity<Void> updateChannelsStock(@RequestBody UpdateChannelsStockDto dto);

    /**
     * 获取spu_sku info list
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/spu/getEcSpuSkuInfoListBySpuId")
    ServerResponseEntity<SpuVO> getEcSpuSkuInfoListBySpuId(@RequestParam ("spuId")Long spuId);
}
