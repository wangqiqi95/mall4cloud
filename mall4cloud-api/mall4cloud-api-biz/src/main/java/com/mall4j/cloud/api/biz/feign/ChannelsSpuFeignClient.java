package com.mall4j.cloud.api.biz.feign;

import com.mall4j.cloud.api.biz.vo.ChannelsSpuSkuVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.models.auth.In;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 视频号4.0 feign
 * @date 2023/3/13
 */
@FeignClient(value = "mall4cloud-biz",contextId = "channelsspu")
public interface ChannelsSpuFeignClient {

    /**
     * 商品下架操作
     *
     * @param spuIds spuId
     * @return boolean
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/channels/spu/deListingProduct")
    ServerResponseEntity<Boolean> deListingProduct(@RequestBody List<Long> spuIds);

    /**
     * 商品库存置零
     * @param skuId skuId
     * @return void
     */
    @RequestMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/channels/spu/deListingProduct")
    ServerResponseEntity<Void> zeroSetProductStock(@RequestParam("skuId") Long skuId);

    /**
     * 查询视频号商品信息集合
     * @param outSpuIds 商品ID集合
     * @return list
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/channels/spu/listChannelsSpuSkuVO")
    ServerResponseEntity<List<ChannelsSpuSkuVO>> listChannelsSpuSkuVO(@RequestBody List<Long> outSpuIds);

    /**
     * 查询单个视频号商品信息
     * @param outSpuId spuId
     * @return vo
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/channels/spu/getChannelsSpuSkuVO")
    ServerResponseEntity<ChannelsSpuSkuVO> getChannelsSpuSkuVO(@RequestParam("spuId") Long outSpuId);

    /**
     * 库存扣减
     * @param skuId skuId
     * @param stock stock
     * @return void
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/channels/spu/reduceChannelsStockBySkuId")
    ServerResponseEntity<Void> reduceChannelsStockBySkuId(@RequestParam("skuId")Long skuId, @RequestParam("stock")Integer stock);
}
