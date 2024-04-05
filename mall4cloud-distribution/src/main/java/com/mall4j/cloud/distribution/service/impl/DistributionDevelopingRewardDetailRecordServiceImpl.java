package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetailRecord;
import com.mall4j.cloud.distribution.mapper.DistributionDevelopingRewardDetailRecordMapper;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardDetailRecordService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分销推广-发展奖励发展明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 21:39:02
 */
@Service
public class DistributionDevelopingRewardDetailRecordServiceImpl implements DistributionDevelopingRewardDetailRecordService {

    @Autowired
    private DistributionDevelopingRewardDetailRecordMapper distributionDevelopingRewardDetailRecordMapper;

    @Override
    public PageVO<DistributionDevelopingRewardDetailRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionDevelopingRewardDetailRecordMapper.list());
    }

    @Override
    public DistributionDevelopingRewardDetailRecord getById(Long id) {
        return distributionDevelopingRewardDetailRecordMapper.getById(id);
    }

    @Override
    public void save(DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord) {
        distributionDevelopingRewardDetailRecordMapper.save(distributionDevelopingRewardDetailRecord);
    }

    @Override
    public void update(DistributionDevelopingRewardDetailRecord distributionDevelopingRewardDetailRecord) {
        distributionDevelopingRewardDetailRecordMapper.update(distributionDevelopingRewardDetailRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionDevelopingRewardDetailRecordMapper.deleteById(id);
    }

    @Override
    public DistributionDevelopingRewardDetailRecord getByUserId(Long userId) {
        return distributionDevelopingRewardDetailRecordMapper.getByUserId(userId);
    }

    @Override
    public int countRewardDetailRecordByRewardAndDetail(Long rewardId, Long detailId) {
        return distributionDevelopingRewardDetailRecordMapper.countRewardDetailRecordByRewardAndDetail(rewardId, detailId);
    }
}
