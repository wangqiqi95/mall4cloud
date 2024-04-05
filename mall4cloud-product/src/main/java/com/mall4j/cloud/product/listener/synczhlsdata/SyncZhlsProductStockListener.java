package com.mall4j.cloud.product.listener.synczhlsdata;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.service.ZhlsCommodityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 同步有数商品库存变动监听
 * @Author axin
 * @Date 2023-05-16
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SYNC_ZHLS_PRODUCT_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.SYNC_ZHLS_PRODUCT_STOCK_GROUP,
        selectorExpression = RocketMqConstant.SYNC_ZHLS_PRODUCT_STOCK_TAG
)
public class SyncZhlsProductStockListener implements RocketMQListener<List<ErpSkuStockDTO>> {

    @Autowired
    private ZhlsCommodityService zhlsCommodityService;

    @Override
    public void onMessage(List<ErpSkuStockDTO> reqDtos) {
        if(CollectionUtils.isNotEmpty(reqDtos)){
            log.info("同步有数监听库存mq入参:{}", JSON.toJSONString(reqDtos));
            zhlsCommodityService.skuInfoUpdateStock(reqDtos);
        }
    }
}
