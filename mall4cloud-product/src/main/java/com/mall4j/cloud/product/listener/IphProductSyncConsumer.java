package com.mall4j.cloud.product.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.SendResult;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.product.dto.ErpSyncDTO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.config.ProductConfigProperties;
import com.mall4j.cloud.product.constant.SpuStatus;
import com.mall4j.cloud.product.service.SpuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 爱铺货同步商品数据
 *
 * 解锁库存的监听
 *
 * 同一个topic不同的tag,group也需要不同
 *
 * @author FrozenWatermelon
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ERP_PRODUCT_INFO_SYNC_MESSAGE_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.IPH_PRODUCT_GROUP,
        selectorExpression = RocketMqConstant.IPH_PRODUCT_TAG)
public class IphProductSyncConsumer implements RocketMQListener<SpuDTO> {

    @Autowired
    private SpuService spuService;
    @Autowired
    private OnsMQTemplate syncZhlsProductTemplate;

    @Autowired
    private ProductConfigProperties productConfigProperties;

    /**
     * 订单支付成功锁定库存
     */
    @Override
    public void onMessage(SpuDTO spuDTO) {

        log.info("---iphProdSync---爱铺货mq spuCode【{}】",spuDTO.getSpuCode());

        spuService.iphSync(spuDTO);

        if(productConfigProperties.getSyncZhlsData()) {
            SendResult sendResult = syncZhlsProductTemplate.syncSend(spuDTO, RocketMqConstant.SYNC_ZHLS_IPH_PRODUT_TAG);
            log.info("爱铺货商品同步有数,msgId:{},入参：{}", sendResult.getMessageId(), spuDTO);
        }
    }
}
