package com.mall4j.cloud.distribution.listener;

import com.mall4j.cloud.api.distribution.dto.DevelopingRewardDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionDevelopingRewardDTO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingReward;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetail;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetailRecord;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardStore;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardDetailRecordService;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardDetailService;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardService;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardStoreService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ZengFanChang
 * @Date 2021/12/26
 */
@Component
//@RocketMQMessageListener(topic = RocketMqConstant.ORDER_SETTLED_SHOP_TOPIC, consumerGroup = "GID_"+RocketMqConstant.ORDER_SETTLED_SHOP_TOPIC)
public class DevelopingRewardConsumer implements RocketMQListener<DevelopingRewardDTO> {

    private static final Logger LOG = LoggerFactory.getLogger(DevelopingRewardConsumer.class);

    @Autowired
    private DistributionDevelopingRewardDetailRecordService distributionDevelopingRewardDetailRecordService;

    @Autowired
    private DistributionDevelopingRewardStoreService distributionDevelopingRewardStoreService;

    @Autowired
    private DistributionDevelopingRewardService distributionDevelopingRewardService;

    @Autowired
    private DistributionDevelopingRewardDetailService distributionDevelopingRewardDetailService;

    @Autowired
    private StaffFeignClient staffFeignClient;


    @Override
    public void onMessage(DevelopingRewardDTO dto) {
        LOG.info("处理导购分销发展奖励 dto:{}" , dto);
        if (null == dto.getStaffId()) {
            LOG.info("威客暂无导购信息");
            return;
        }
        ServerResponseEntity<StaffVO> responseEntity = staffFeignClient.getStaffById(dto.getStaffId());
        if (!responseEntity.isSuccess() || null == responseEntity.getData()) {
            LOG.info("导购部不存在");
            return;
        }
        StaffVO staffVO = responseEntity.getData();
        //威客申请处理
        if (dto.isApply()) {
            DistributionDevelopingRewardDetailRecord record = distributionDevelopingRewardDetailRecordService.getByUserId(dto.getUserId());
            if (null != record) {
                LOG.info("威客已存在发展奖励申请记录 param:{}", dto);
                return;
            }
            List<DistributionDevelopingRewardStore> storeList = distributionDevelopingRewardStoreService.listByStoreId(dto.getStoreId());
            List<Long> storeIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(storeList)) {
                storeIds = storeList.stream().map(DistributionDevelopingRewardStore::getStoreId).distinct().collect(Collectors.toList());
            }
            List<DistributionDevelopingReward> rewardList = distributionDevelopingRewardService.listEffectRewardByIds(storeIds);
            if (CollectionUtils.isEmpty(rewardList)) {
                LOG.info("暂无生效发展活动 param:{}", dto);
                return;
            }
            DistributionDevelopingReward reward = rewardList.get(0);
            DistributionDevelopingRewardDetail detail = distributionDevelopingRewardDetailService.getByRewardIdAndStaffId(reward.getId(), dto.getStaffId());
            DistributionDevelopingRewardDetailRecord newRecord = new DistributionDevelopingRewardDetailRecord();
            if (null != detail) {
                if (distributionDevelopingRewardDetailRecordService.countRewardDetailRecordByRewardAndDetail(reward.getId(), detail.getId()) >= reward.getRewardMax()) {
                    LOG.info("导购获取发展奖励已上限 dto:{}, reward:{}, detail:{}", dto, reward, detail);
                    return;
                }
                newRecord.setDevelopingRewardDetailId(detail.getId());
            }
            newRecord.setDevelopingRewardId(reward.getId());
            newRecord.setUserId(dto.getUserId());
            newRecord.setUserName(dto.getUserName());
            newRecord.setMobile(dto.getMobile());
            newRecord.setApplyTime(dto.getApplyTime());
            newRecord.setPass(0);
            distributionDevelopingRewardDetailRecordService.save(newRecord);
        } else {
            //威客通过处理
            DistributionDevelopingRewardDetailRecord record = distributionDevelopingRewardDetailRecordService.getByUserId(dto.getUserId());
            if (null == record) {
                LOG.info("威客不存在发展奖励申请记录 param:{}", dto);
                return;
            }
            if (record.getPass() == 1) {
                LOG.info("威客奖励已处理 param:{}", dto);
                return;
            }
            DistributionDevelopingRewardDTO rewardDTO = distributionDevelopingRewardService.getById(record.getDevelopingRewardId());
            if (null == rewardDTO) {
                LOG.info("发展奖励活动不存在 param:{}, id:{}", dto, record.getDevelopingRewardId());
                return;
            }
            DistributionDevelopingRewardDetail detail = distributionDevelopingRewardDetailService.getByRewardIdAndStaffId(rewardDTO.getId(), dto.getStaffId());
            if (null == detail) {
                detail = new DistributionDevelopingRewardDetail();
                detail.setDevelopingRewardId(rewardDTO.getId());
                detail.setStaffId(dto.getStaffId());
                detail.setStaffCode(staffVO.getStaffNo());
                detail.setStaffName(staffVO.getStaffName());
                detail.setMobile(staffVO.getMobile());
                detail.setStoreId(staffVO.getStoreId());
                detail.setStoreName(staffVO.getStoreName());
                detail.setMemberNum(1);
                detail.setTotalReward(rewardDTO.getRewardAmount());
                distributionDevelopingRewardDetailService.save(detail);
                rewardDTO.setStaffNum(rewardDTO.getStaffNum() + 1);
            } else {
                detail.setMemberNum(detail.getMemberNum() + 1);
                detail.setTotalReward(detail.getTotalReward() + rewardDTO.getRewardAmount());
                distributionDevelopingRewardDetailService.update(detail);
            }
            rewardDTO.setMemberNum(rewardDTO.getMemberNum() + 1);
            rewardDTO.setTotalReward(rewardDTO.getTotalReward() + rewardDTO.getRewardAmount());
            distributionDevelopingRewardService.update(rewardDTO);

            record.setDevelopingRewardDetailId(detail.getId());
            record.setPass(1);
            record.setPassTime(new Date());
            distributionDevelopingRewardDetailRecordService.update(record);
        }
    }

}
