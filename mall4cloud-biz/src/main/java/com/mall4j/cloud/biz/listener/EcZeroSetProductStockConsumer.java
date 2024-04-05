package com.mall4j.cloud.biz.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.service.channels.ChannelsSkuService;
import com.mall4j.cloud.biz.service.channels.ChannelsSpuService;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.EC_ZERO_SET_PRODUCT_STOCK_TOPIC,consumerGroup = "GID_"+RocketMqConstant.EC_ZERO_SET_PRODUCT_STOCK_TOPIC)
public class EcZeroSetProductStockConsumer implements RocketMQListener<Long> {

    @Autowired
    ChannelsSkuService channelsSkuService;
    @Autowired
    ChannelsSpuService channelsSpuService;

    /**
     * 视频号4.0 设置0库存
     */
    @Override
    public void onMessage(Long skuId) {
        log.info("库存同步设置视频号库存为0，接收参数:{}",skuId);

        ChannelsSku channelsSku = channelsSkuService.getOne(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getSkuId, skuId));
        if (Objects.nonNull(channelsSku)) {
            channelsSpuService.updateStock(channelsSku.getChannelsSpuId(), channelsSku.getId(), 0, 3);
        }
    }
}
