package com.mall4j.cloud.product.listener;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.product.dto.ErpStockDTO;
import com.mall4j.cloud.api.product.dto.ErpSyncDTO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.service.SpuService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 中台同步商品基础数据
 *
 * 解锁库存的监听
 *
 *同一个topic不同的tag,group也需要不同
 *
 * @author FrozenWatermelon
 */
@Slf4j
@Component
@RefreshScope
@RocketMQMessageListener(topic = RocketMqConstant.ERP_PRODUCT_INFO_SYNC_MESSAGE_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.ERP_PRODUCT_INFO_SYNC_MESSAGE_TOPIC,
        selectorExpression = RocketMqConstant.ERP_PRODUCT_TAG)
public class ErpProductSyncConsumer implements RocketMQListener<ErpSyncDTO> {

    @Autowired
    private SpuService spuService;

    /**
     * 订单支付成功锁定库存
     */
    @Override
    public void onMessage(ErpSyncDTO erpSyncDTO) {
        log.info("中台商品基础数据同步数据:{}", JSONObject.toJSON(erpSyncDTO));

        spuService.sync(erpSyncDTO);
    }
}
