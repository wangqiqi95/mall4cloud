package com.mall4j.cloud.docking.listener;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.docking.skq_erp.service.IStdOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.ORDER_NOTIFY_STD_TOPIC,
        consumerGroup = "GID_"+RocketMqConstant.ORDER_REFUND_STD_GROUP,
        selectorExpression = RocketMqConstant.ORDER_REFUND_STD_TOPIC_TAG)
public class OrderRefundNoticePushConsumer implements RocketMQListener<List<PushRefundDto>> {

    @Autowired
    IStdOrderService stdOrderService;

    @Override
    public void onMessage(List<PushRefundDto> pushRefundDtos) {
        try {
            //根据订单id推送订单
            log.info("[推送退单] -- 参数 :{}", JSONUtil.toJsonStr(pushRefundDtos));
            ServerResponseEntity<String> responseEntity = stdOrderService.pushRefund(pushRefundDtos);

            //推送退单
            log.info("[退单推送参数] -- ：{}", JSONObject.toJSONString(pushRefundDtos));
            if(responseEntity==null || responseEntity.isFail()){
                Assert.faild(responseEntity.getMsg());
            }
        } catch (Exception e) {
            log.error("[推送退单] -- 异常输出 :{}", e.getMessage());
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }



}
