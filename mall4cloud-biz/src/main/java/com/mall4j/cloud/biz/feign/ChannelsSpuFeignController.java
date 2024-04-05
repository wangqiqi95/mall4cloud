package com.mall4j.cloud.biz.feign;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.biz.feign.ChannelsSpuFeignClient;
import com.mall4j.cloud.api.biz.vo.ChannelsSpuSkuVO;
import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.service.channels.ChannelsSkuService;
import com.mall4j.cloud.biz.service.channels.ChannelsSpuService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 视频号商品4.0
 * @date 2023/3/14
 */
@RestController
public class ChannelsSpuFeignController implements ChannelsSpuFeignClient {
    @Autowired
    private ChannelsSpuService channelsSpuService;

    @Autowired
    private ChannelsSkuService channelsSkuService;

    @Override
    public ServerResponseEntity<Boolean> deListingProduct(List<Long> spuIds) {
        for (Long spuId : spuIds) {
            channelsSpuService.delisting(spuId);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> zeroSetProductStock(Long skuId) {
        ChannelsSku channelsSku = channelsSkuService.getOne(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getSkuId, skuId));
        if (Objects.nonNull(channelsSku)) {
            channelsSpuService.updateStock(channelsSku.getChannelsSpuId(), channelsSku.getId(), 0, 3);
        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<ChannelsSpuSkuVO>> listChannelsSpuSkuVO(List<Long> outSpuIds) {
        return ServerResponseEntity.success(channelsSpuService.listChannelsSpuSkuVO(outSpuIds));
    }

    @Override
    public ServerResponseEntity<ChannelsSpuSkuVO> getChannelsSpuSkuVO(Long outSpuId) {
        return ServerResponseEntity.success(channelsSpuService.getChannelsSpuSkuVO(outSpuId));
    }

    @Override
    public ServerResponseEntity<Void> reduceChannelsStockBySkuId(Long skuId, Integer stock) {
        channelsSkuService.reduceChannelsStockBySkuId(skuId, stock);
        return ServerResponseEntity.success();
    }
}
