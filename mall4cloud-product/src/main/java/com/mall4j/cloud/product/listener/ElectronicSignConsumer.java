package com.mall4j.cloud.product.listener;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.dto.ElectronicSignDto;
import com.mall4j.cloud.api.product.dto.ElectronicDateMqDTO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.service.ElectronicSignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电子价签同步
 *
 * @author luzhengxiang
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ALI_ELECTRONIC_SIGN_SYNC_MESSAGE_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.ALI_ELECTRONIC_SIGN_SYNC_MESSAGE_TOPIC)
public class ElectronicSignConsumer implements RocketMQListener<ElectronicDateMqDTO> {

    @Autowired
    private ElectronicSignService electronicSignService;

    @Override
    public void onMessage(ElectronicDateMqDTO dateMqDTO) {
        log.info("电子价签同步 mq请求，入参为:", JSONObject.toJSONString(dateMqDTO.getSpuIds()));
        if(dateMqDTO.getMqType()==1){
            electronicSignService.syncSpu(dateMqDTO.getSpuIds());
        }else if(dateMqDTO.getMqType()==2){
            electronicSignService.syncStoreSpu(dateMqDTO.getStoreIds());
        }
    }
}
