package com.mall4j.cloud.product.listener.synczhlsdata;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.constant.SpuStatus;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.service.ZhlsCommodityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 同步有数商品上架监听
 * @Author axin
 * @Date 2023-05-16
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SYNC_ZHLS_PRODUCT_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.SYNC_ZHLS_PUT_SHELF_GROUP,
        selectorExpression = RocketMqConstant.SYNC_ZHLS_PUT_SHELF_TAG
)
public class SyncZhlsPutShelfListener implements RocketMQListener<List<Long>> {

    @Autowired
    private ZhlsCommodityService zhlsCommodityService;
    @Autowired
    private SkuMapper skuMapper;

    @Override
    public void onMessage(List<Long> spuIds) {
        log.info("同步有数监听商品上架mq入参:{}", JSON.toJSONString(spuIds));

        List<Sku> skus = skuMapper.selectList(Wrappers.lambdaQuery(Sku.class).in(Sku::getSpuId, spuIds));
        List<Long> skuIds = skus.stream().map(Sku::getSkuId).collect(Collectors.toList());

        zhlsCommodityService.skuInfoUpdateStatus(skuIds, SpuStatus.PUT_SHELF.value());
    }
}
