package com.mall4j.cloud.product.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.product.dto.ErpPriceDTO;
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
 * 价格同步
 *
 * @author FrozenWatermelon
 */
@Slf4j
@Component
@RefreshScope
@RocketMQMessageListener(topic = RocketMqConstant.ERP_PRODUCT_PRICE_SYNC_MESSAGE_TOPIC, consumerGroup = "GID_" + RocketMqConstant.ERP_PRODUCT_PRICE_SYNC_MESSAGE_TOPIC)
public class ErpPriceSyncConsumer implements RocketMQListener<ErpPriceDTO> {


    @Autowired
    private SpuService spuService;

    /**
     * 订单支付成功锁定库存
     */
    @Override
    public void onMessage(ErpPriceDTO erpPriceDTO) {

        log.info("中台价格同步数据:{}", JSONObject.toJSON(erpPriceDTO));

        spuService.priceSyncNew(erpPriceDTO);
    }
}
