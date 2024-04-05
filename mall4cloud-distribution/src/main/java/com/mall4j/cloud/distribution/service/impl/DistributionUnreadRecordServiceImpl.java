package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionUnreadRecord;
import com.mall4j.cloud.distribution.mapper.DistributionUnreadRecordMapper;
import com.mall4j.cloud.distribution.service.DistributionUnreadRecordService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分销推广-用户未读信息
 *
 * @author ZengFanChang
 * @date 2022-01-23 22:16:06
 */
@Service
public class DistributionUnreadRecordServiceImpl implements DistributionUnreadRecordService {

    @Autowired
    private DistributionUnreadRecordMapper distributionUnreadRecordMapper;

    @Override
    public PageVO<DistributionUnreadRecord> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionUnreadRecordMapper.list());
    }

    @Override
    public DistributionUnreadRecord getById(Long id) {
        return distributionUnreadRecordMapper.getById(id);
    }

    @Override
    public DistributionUnreadRecord getByUser(Integer identityType, Long userId) {
        return distributionUnreadRecordMapper.getByUser(identityType, userId);
    }

    @Override
    public void save(DistributionUnreadRecord distributionUnreadRecord) {
        distributionUnreadRecordMapper.save(distributionUnreadRecord);
    }

    @Override
    public void update(DistributionUnreadRecord distributionUnreadRecord) {
        distributionUnreadRecordMapper.update(distributionUnreadRecord);
    }

    @Override
    public void deleteById(Long id) {
        distributionUnreadRecordMapper.deleteById(id);
    }
}
