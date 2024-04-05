package com.mall4j.cloud.distribution.listener;

import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.user.dto.UserActionSaveNotifyDTO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.distribution.model.*;
import com.mall4j.cloud.distribution.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author ZengFanChang
 * @Date 2022/02/24
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.USER_ACTION_SAVE_NOTIFY_TOPIC, consumerGroup = "GID_"+RocketMqConstant.USER_ACTION_SAVE_NOTIFY_TOPIC)
public class GuideShareRecordConsumer implements RocketMQListener<UserActionSaveNotifyDTO> {

    @Autowired
    private DistributionBrowseRecordService distributionBrowseRecordService;

    @Autowired
    private DistributionBuyRecordService distributionBuyRecordService;

    @Autowired
    private DistributionPurchaseRecordService distributionPurchaseRecordService;

    @Autowired
    private DistributionPosterService distributionPosterService;

    @Autowired
    private DistributionMomentsService distributionMomentsService;

    @Autowired
    private DistributionSpecialSubjectService distributionSpecialSubjectService;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Override
    public void onMessage(UserActionSaveNotifyDTO userActionSaveNotifyDTO) {
        log.info("分销埋点处理 userActionSaveNotifyDTO:{}", userActionSaveNotifyDTO);
        if (null == userActionSaveNotifyDTO || null == userActionSaveNotifyDTO.getType() || userActionSaveNotifyDTO.getType() == 4){
            return;
        }
        if (null == userActionSaveNotifyDTO.getPageType() || userActionSaveNotifyDTO.getPageType() == 5 || userActionSaveNotifyDTO.getPageType() == 6){
            return;
        }
        String productName = "";
        if (null != userActionSaveNotifyDTO.getActivityId()){
            if (userActionSaveNotifyDTO.getPageType() == 1){
                DistributionPoster poster = distributionPosterService.getById(userActionSaveNotifyDTO.getActivityId());
                if (null != poster){
                    productName = poster.getPosterName();
                }
            } else if (userActionSaveNotifyDTO.getPageType() == 2){
                DistributionSpecialSubject specialSubject = distributionSpecialSubjectService.getById(userActionSaveNotifyDTO.getActivityId());
                if (null != specialSubject){
                    productName = specialSubject.getSubjectName();
                }
            } else if (userActionSaveNotifyDTO.getPageType() == 3){
                DistributionMoments moments = distributionMomentsService.getById(userActionSaveNotifyDTO.getActivityId());
                if (null != moments){
                    productName = moments.getTitle();
                }
            } else if (userActionSaveNotifyDTO.getPageType() == 4){
                ServerResponseEntity<SpuVO> supData = spuFeignClient.getById(userActionSaveNotifyDTO.getActivityId());
                if (supData.isSuccess() && null != supData.getData()){
                    productName = supData.getData().getName();
                }
            }
        }
        if (userActionSaveNotifyDTO.getType() == 1){
            DistributionBuyRecord record = new DistributionBuyRecord();
            record.setBuyId(userActionSaveNotifyDTO.getUserId());
            record.setBuyName(userActionSaveNotifyDTO.getUserName());
            record.setBuyType(3);
            record.setTentacleNo(userActionSaveNotifyDTO.getTentacleNo());
            record.setActivityId(userActionSaveNotifyDTO.getActivityId());
            record.setActivityName(productName);
            record.setActivityType(userActionSaveNotifyDTO.getPageType());
            record.setProductId(userActionSaveNotifyDTO.getProductId());
            record.setProductName(userActionSaveNotifyDTO.getProductName());
            distributionBuyRecordService.save(record);
        } else if (userActionSaveNotifyDTO.getType() == 2){
            DistributionPurchaseRecord record = new DistributionPurchaseRecord();
            record.setPurchaseId(userActionSaveNotifyDTO.getUserId());
            record.setPurchaseName(userActionSaveNotifyDTO.getUserName());
            record.setTentacleNo(userActionSaveNotifyDTO.getTentacleNo());
            record.setStoreId(userActionSaveNotifyDTO.getStoreId());
            record.setActivityId(userActionSaveNotifyDTO.getActivityId());
            record.setActivityName(productName);
            record.setActivityType(userActionSaveNotifyDTO.getPageType());
            record.setProductId(userActionSaveNotifyDTO.getProductId());
            record.setProductName(userActionSaveNotifyDTO.getProductName());
            distributionPurchaseRecordService.save(record);
        } else if (userActionSaveNotifyDTO.getType() == 3){
            DistributionBrowseRecord record = new DistributionBrowseRecord();
            record.setBrowseId(userActionSaveNotifyDTO.getUserId());
            record.setBrowseName(userActionSaveNotifyDTO.getUserName());
            record.setBrowseType(3);
            record.setTentacleNo(userActionSaveNotifyDTO.getTentacleNo());
            if (null != userActionSaveNotifyDTO.getProductId() && userActionSaveNotifyDTO.getPageType() == 4){
                userActionSaveNotifyDTO.setActivityId(userActionSaveNotifyDTO.getProductId());
                productName = userActionSaveNotifyDTO.getProductName();
            }
            record.setActivityId(userActionSaveNotifyDTO.getActivityId());
            record.setActivityName(productName);
            record.setActivityType(userActionSaveNotifyDTO.getPageType());
            distributionBrowseRecordService.save(record);
        }
    }

}
