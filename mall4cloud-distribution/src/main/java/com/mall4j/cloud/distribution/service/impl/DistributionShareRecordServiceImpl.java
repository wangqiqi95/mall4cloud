package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.distribution.dto.DistributionActivityShareRecordDTO;
import com.mall4j.cloud.distribution.dto.DistributionShareRecordDTO;
import com.mall4j.cloud.distribution.mapper.DistributionShareRecordMapper;
import com.mall4j.cloud.distribution.model.DistributionShareRecord;
import com.mall4j.cloud.distribution.service.DistributionShareRecordService;
import com.mall4j.cloud.distribution.vo.DistributionShareRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 分享推广-分享记录
 *
 * @author ZengFanChang
 * @date 2022-01-01 17:18:07
 */
@Service
public class DistributionShareRecordServiceImpl implements DistributionShareRecordService {

    @Autowired
    private DistributionShareRecordMapper distributionShareRecordMapper;

    @Autowired
    private OnsMQTemplate sendDistributionActivityShareTemplate;

    @Autowired
    private OnsMQTemplate sendDistributionGuideShareTemplate;

    @Override
    public PageVO<DistributionShareRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionShareRecordMapper.list());
    }

    @Override
    public DistributionShareRecord getById(Long id) {
        return distributionShareRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionShareRecord distributionShareRecord) {

        distributionShareRecordMapper.save(distributionShareRecord);

        saveActivityShareRecord(distributionShareRecord);

        saveGuideShareRecord(distributionShareRecord);

    }

    private void saveActivityShareRecord(DistributionShareRecord distributionShareRecord) {
        DistributionActivityShareRecordDTO dto = new DistributionActivityShareRecordDTO();
        dto.setActivityId(distributionShareRecord.getActivityId());
        dto.setActivityType(distributionShareRecord.getActivityType());
        dto.setActivityName(distributionShareRecord.getActivityName());
        dto.setRecordType(1);
        sendDistributionActivityShareTemplate.syncSend(dto);
    }

    private void saveGuideShareRecord(DistributionShareRecord distributionShareRecord) {
        if (null == distributionShareRecord.getShareId()){
            return;
        }
        if (null == distributionShareRecord.getShareType()){
            return;
        }
        if (1 != distributionShareRecord.getShareType()){
            return;
        }
        DistributionShareRecordDTO dto = new DistributionShareRecordDTO();
        dto.setActivityId(distributionShareRecord.getActivityId());
        dto.setActivityType(distributionShareRecord.getActivityType());
        dto.setRecordType(1);
        dto.setShareId(distributionShareRecord.getShareId());
        sendDistributionGuideShareTemplate.syncSend(dto);
    }

    @Override
    public void update(DistributionShareRecord distributionShareRecord) {
        distributionShareRecordMapper.update(distributionShareRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionShareRecordMapper.deleteById(id);
    }

    @Override
    public int countByActivityAndDate(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionShareRecordMapper.countByActivityAndDate(activityId, activityType, startDate, endDate);
    }

    @Override
    public int countByShareActivityAndDate(Long shareId, Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionShareRecordMapper.countByShareActivityAndDate(shareId, activityId, activityType, startDate, endDate);
    }

    @Override
    public List<DistributionShareRecordVO> listStaffByActivity(Long activityId, Integer activityType, Date startDate, Date endDate) {
        return distributionShareRecordMapper.listStaffByActivity(activityId, activityType, startDate, endDate);
    }
}
