package com.mall4j.cloud.distribution.listener;

import cn.hutool.core.util.StrUtil;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionSpecialSubjectDTO;
import com.mall4j.cloud.distribution.mapper.DistributionBrowseRecordMapper;
import com.mall4j.cloud.distribution.model.DistributionActivityShareRecord;
import com.mall4j.cloud.distribution.model.DistributionMoments;
import com.mall4j.cloud.distribution.model.DistributionPoster;
import com.mall4j.cloud.distribution.model.DistributionSpecialSubject;
import com.mall4j.cloud.distribution.service.DistributionActivityShareRecordService;
import com.mall4j.cloud.distribution.service.DistributionMomentsService;
import com.mall4j.cloud.distribution.service.DistributionPosterService;
import com.mall4j.cloud.distribution.service.DistributionSpecialSubjectService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author ZengFanChang
 * @Date 2022/01/02
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.COUNT_DISTRIBUTION_ACTIVITY_SHARE, consumerGroup = "GID_"+RocketMqConstant.COUNT_DISTRIBUTION_ACTIVITY_SHARE)
public class ActivityShareConsumer implements RocketMQListener<DistributionActivityShareRecordDTO> {


    @Autowired
    private DistributionActivityShareRecordService distributionActivityShareRecordService;

    @Resource
    private DistributionBrowseRecordMapper distributionBrowseRecordMapper;

    //海报
    @Autowired
    private DistributionPosterService distributionPosterService;
    //专题
    @Autowired
    private DistributionSpecialSubjectService distributionSpecialSubjectService;
    //朋友圈
    @Autowired
    private DistributionMomentsService distributionMomentsService;
    @Autowired
    private SpuFeignClient spuFeignClient;



    @Override
    public void onMessage(DistributionActivityShareRecordDTO distributionActivityShareRecordDTO) {
        DistributionActivityShareRecord activity = distributionActivityShareRecordService.getByActivity(distributionActivityShareRecordDTO.getActivityId(), distributionActivityShareRecordDTO.getActivityType());
        if (null != activity) {
            buildActivityShare(activity, distributionActivityShareRecordDTO);
            if(StrUtil.isBlank(activity.getActivityName())){
                activity.setActivityName(getActivityName(activity.getActivityId(),activity.getActivityType()));
            }
            distributionActivityShareRecordService.update(activity);
        } else {
            activity = new DistributionActivityShareRecord();
            activity.setActivityId(distributionActivityShareRecordDTO.getActivityId());
            activity.setActivityType(distributionActivityShareRecordDTO.getActivityType());
            activity.setActivityName(distributionActivityShareRecordDTO.getActivityName());
            buildActivityShare(activity, distributionActivityShareRecordDTO);
            if(StrUtil.isBlank(activity.getActivityName())){
                activity.setActivityName(getActivityName(activity.getActivityId(),activity.getActivityType()));
            }
            distributionActivityShareRecordService.save(activity);
        }
    }

    /**
     * 活动类型 1海报 2专题 3朋友圈 4商品
     * @param activityId
     * @param activityType
     * @return
     */
    private String getActivityName(Long activityId,Integer activityType){
        if(Objects.isNull(activityId) || Objects.isNull(activityType)){
            return "";
        }
        if(activityType==1){//海报
            DistributionPoster distributionPoster=distributionPosterService.getById(activityId);
            if(distributionPoster!=null){
                return distributionPoster.getPosterName();
            }
        }else if(activityType==2){//专题
            DistributionSpecialSubject subject=distributionSpecialSubjectService.getById(activityId);
            if(subject!=null){
                return subject.getSubjectName();
            }
        }else if(activityType==3){//朋友圈
            DistributionMoments distributionMoments=distributionMomentsService.getById(activityId);
            if(distributionMoments!=null){
                return distributionMoments.getTitle();
            }
        }else if(activityType==4){//商品
            ServerResponseEntity<SpuVO> response=spuFeignClient.getById(activityId);
            if(response.isSuccess() && Objects.nonNull(response.getData())){
                return response.getData().getName();
            }
        }
        return "";
    }


    private void buildActivityShare(DistributionActivityShareRecord activityShareRecord, DistributionActivityShareRecordDTO dto) {
        switch (dto.getRecordType()) {
            case 1:
                activityShareRecord.setShareNum(Optional.ofNullable(activityShareRecord.getShareNum()).orElse(0) + 1);
                break;
            case 2:
                activityShareRecord.setBrowseNum(Optional.ofNullable(activityShareRecord.getBrowseNum()).orElse(0) + 1);
                if (!dto.isExist()) {
                    activityShareRecord.setBrowseUserNum(Optional.ofNullable(activityShareRecord.getBrowseUserNum()).orElse(0) + 1);
                }
                break;
            case 3:
                activityShareRecord.setPurchaseNum(Optional.ofNullable(activityShareRecord.getPurchaseNum()).orElse(0) + 1);
                if (!dto.isExist()) {
                    activityShareRecord.setPurchaseUserNum(Optional.ofNullable(activityShareRecord.getPurchaseUserNum()).orElse(0) + 1);
                }
                break;
            case 4:
                activityShareRecord.setBuyNum(Optional.ofNullable(activityShareRecord.getBuyNum()).orElse(0) + 1);
                if (!dto.isExist()) {
                    activityShareRecord.setBuyUserNum(Optional.ofNullable(activityShareRecord.getBuyUserNum()).orElse(0) + 1);
                }
                break;
            default:
                break;
        }
    }

}
