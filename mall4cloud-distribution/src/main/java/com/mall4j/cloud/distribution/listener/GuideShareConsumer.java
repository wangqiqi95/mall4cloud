package com.mall4j.cloud.distribution.listener;

import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.DateUtils;
import com.mall4j.cloud.distribution.dto.DistributionShareRecordDTO;
import com.mall4j.cloud.distribution.model.DistributionGuideShareRecord;
import com.mall4j.cloud.distribution.service.DistributionGuideShareRecordService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * @Author ZengFanChang
 * @Date 2022/01/02
 */
@Component
@RocketMQMessageListener(topic = RocketMqConstant.COUNT_DISTRIBUTION_GUIDE_SHARE, consumerGroup = "GID_"+RocketMqConstant.COUNT_DISTRIBUTION_GUIDE_SHARE)
public class GuideShareConsumer implements RocketMQListener<DistributionShareRecordDTO> {


    @Autowired
    private DistributionGuideShareRecordService distributionGuideShareRecordService;

    @Autowired
    private StaffFeignClient staffFeignClient;


    @Override
    public void onMessage(DistributionShareRecordDTO distributionShareRecordDTO) {
        if (null == distributionShareRecordDTO.getShareId()) {
            return;
        }
        Date currentDate = DateUtils.dateToFastOrLast(new Date(), 1);
        DistributionGuideShareRecord guideShareRecord = distributionGuideShareRecordService.getByGuideAndActivityAndDate(distributionShareRecordDTO.getShareId(), distributionShareRecordDTO.getActivityType(), currentDate);

        if (null != guideShareRecord) {
            buildGuideShare(guideShareRecord, distributionShareRecordDTO);
            distributionGuideShareRecordService.update(guideShareRecord);
        } else {
            guideShareRecord = new DistributionGuideShareRecord();
            guideShareRecord.setGuideId(distributionShareRecordDTO.getShareId());
            ServerResponseEntity<StaffVO> responseEntity = staffFeignClient.getStaffById(distributionShareRecordDTO.getShareId());
            if (responseEntity.isSuccess() && null != responseEntity.getData()) {
                guideShareRecord.setGuideName(responseEntity.getData().getStaffName());
                guideShareRecord.setGuideNo(responseEntity.getData().getStaffNo());
                guideShareRecord.setStoreId(responseEntity.getData().getStoreId());
                guideShareRecord.setStoreName(responseEntity.getData().getStoreName());
                guideShareRecord.setStoreCode(responseEntity.getData().getStoreCode());
            }
            guideShareRecord.setActivityType(distributionShareRecordDTO.getActivityType());
            guideShareRecord.setDataTime(currentDate);
            buildGuideShare(guideShareRecord, distributionShareRecordDTO);
            distributionGuideShareRecordService.save(guideShareRecord);
        }
    }


    private void buildGuideShare(DistributionGuideShareRecord guideShareRecord, DistributionShareRecordDTO dto) {
        switch (dto.getRecordType()) {
            case 1:
                guideShareRecord.setShareNum(Optional.ofNullable(guideShareRecord.getShareNum()).orElse(0) + 1);
                break;
            case 2:
                guideShareRecord.setBrowseNum(Optional.ofNullable(guideShareRecord.getBrowseNum()).orElse(0) + 1);
                if (!dto.isExist()) {
                    guideShareRecord.setBrowseUserNum(Optional.ofNullable(guideShareRecord.getBrowseUserNum()).orElse(0) + 1);
                }
                break;
            case 3:
                if (!dto.isExist()) {
                    guideShareRecord.setPurchaseUserNum(Optional.ofNullable(guideShareRecord.getPurchaseUserNum()).orElse(0) + 1);
                }
                break;
            case 4:
                break;
            default:
                break;
        }
    }

}
