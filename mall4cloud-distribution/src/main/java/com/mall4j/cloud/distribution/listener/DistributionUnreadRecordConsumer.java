package com.mall4j.cloud.distribution.listener;

import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;
import com.mall4j.cloud.distribution.service.DistributionUnreadRecordService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author ZengFanChang
 * @Date 2022/01/23
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.COUNT_DISTRIBUTION_UNREAD_RECORD, consumerGroup = "GID_"+RocketMqConstant.COUNT_DISTRIBUTION_UNREAD_RECORD)
public class DistributionUnreadRecordConsumer implements RocketMQListener<DistributionUnreadRecord> {

    @Autowired
    private DistributionUnreadRecordService distributionUnreadRecordService;

    @Override
    public void onMessage(DistributionUnreadRecord distributionUnreadRecord) {
        DistributionUnreadRecord unreadRecord = distributionUnreadRecordService.getByUser(distributionUnreadRecord.getIdentityType(), distributionUnreadRecord.getUserId());
        if (null == unreadRecord){
            unreadRecord = new DistributionUnreadRecord();
            unreadRecord.setIdentityType(distributionUnreadRecord.getIdentityType());
            unreadRecord.setUserId(distributionUnreadRecord.getUserId());
            unreadRecord.setUnreadBrowseNum(Optional.ofNullable(distributionUnreadRecord.getUnreadBrowseNum()).orElse(0));
            unreadRecord.setUnreadPurchaseNum(Optional.ofNullable(distributionUnreadRecord.getUnreadPurchaseNum()).orElse(0));
            unreadRecord.setUnreadBuyNum(Optional.ofNullable(distributionUnreadRecord.getUnreadBuyNum()).orElse(0));
            distributionUnreadRecordService.save(unreadRecord);
        } else {
            unreadRecord.setUnreadBrowseNum(Optional.ofNullable(distributionUnreadRecord.getUnreadBrowseNum()).orElse(0) + unreadRecord.getUnreadBrowseNum());
            unreadRecord.setUnreadPurchaseNum(Optional.ofNullable(distributionUnreadRecord.getUnreadPurchaseNum()).orElse(0) + unreadRecord.getUnreadPurchaseNum());
            unreadRecord.setUnreadBuyNum(Optional.ofNullable(distributionUnreadRecord.getUnreadBuyNum()).orElse(0) + unreadRecord.getUnreadBuyNum());
            distributionUnreadRecordService.update(unreadRecord);
        }
    }
}
