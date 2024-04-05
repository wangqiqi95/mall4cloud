package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardDetail;
import com.mall4j.cloud.distribution.mapper.DistributionDevelopingRewardDetailMapper;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardDetailService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分销推广-发展奖励明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:16:08
 */
@Service
public class DistributionDevelopingRewardDetailServiceImpl implements DistributionDevelopingRewardDetailService {

    @Autowired
    private DistributionDevelopingRewardDetailMapper distributionDevelopingRewardDetailMapper;

    @Override
    public PageVO<DistributionDevelopingRewardDetail> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionDevelopingRewardDetailMapper.list());
    }

    @Override
    public DistributionDevelopingRewardDetail getById(Long id) {
        return distributionDevelopingRewardDetailMapper.getById(id);
    }

    @Override
    public void save(DistributionDevelopingRewardDetail distributionDevelopingRewardDetail) {
        distributionDevelopingRewardDetailMapper.save(distributionDevelopingRewardDetail);
    }

    @Override
    public void update(DistributionDevelopingRewardDetail distributionDevelopingRewardDetail) {
        distributionDevelopingRewardDetailMapper.update(distributionDevelopingRewardDetail);
    }

    @Override
    public void deleteById(Long id) {
        distributionDevelopingRewardDetailMapper.deleteById(id);
    }

    @Override
    public DistributionDevelopingRewardDetail getByRewardIdAndStaffId(Long rewardId, Long staffId) {
        return distributionDevelopingRewardDetailMapper.getByRewardIdAndStaffId(rewardId, staffId);
    }
}
