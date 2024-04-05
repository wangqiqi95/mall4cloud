package com.mall4j.cloud.product.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.product.dto.ErpSkuStockDTO;
import com.mall4j.cloud.api.product.dto.ErpStockDTO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.config.ProductConfigProperties;
import com.mall4j.cloud.product.service.SpuService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 解锁库存的监听
 *
 * @author FrozenWatermelon
 */
@Slf4j
@Component
@RefreshScope
@RocketMQMessageListener(topic = RocketMqConstant.ERP_PRODUCT_STOCK_SYNC_MESSAGE_TOPIC, consumerGroup = "GID_" + RocketMqConstant.ERP_PRODUCT_STOCK_SYNC_MESSAGE_TOPIC)
public class ErpStockSyncConsumer implements RocketMQListener<ErpStockDTO> {


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
    public void onMessage(ErpStockDTO erpStockDTO) {
        log.info("中台库存同步数据:{}", JSONObject.toJSON(erpStockDTO));

        spuService.stockSync(erpStockDTO);

        if(productConfigProperties.getSyncZhlsData()) {
            if (CollectionUtils.isNotEmpty(erpStockDTO.getStockDTOList())) {
                List<ErpSkuStockDTO> erpSkuStockDTOS = Lists.newArrayList();
                for (ErpSkuStockDTO erpSkuStockDTO : erpStockDTO.getStockDTOList()) {
                    if ("XCX1-1".equals(erpSkuStockDTO.getStoreCode()) && Objects.nonNull(erpSkuStockDTO.getSyncType()) && erpSkuStockDTO.getSyncType() == 1) {
                        erpSkuStockDTOS.add(erpSkuStockDTO);
                    }
                }
                SendResult sendResult = syncZhlsProductTemplate.syncSend(erpSkuStockDTOS, RocketMqConstant.SYNC_ZHLS_PRODUCT_STOCK_TAG);
                log.info("库存同步有数,msgId:{},入参：{}", sendResult.getMessageId(), JSON.toJSONString(erpSkuStockDTOS));
            }
        }

    }
}
