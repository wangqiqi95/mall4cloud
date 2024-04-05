package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionCommissionLog;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionLogMapper;
import com.mall4j.cloud.distribution.service.DistributionCommissionLogService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 佣金流水信息
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
@Service
public class DistributionCommissionLogServiceImpl implements DistributionCommissionLogService {

    @Autowired
    private DistributionCommissionLogMapper distributionCommissionLogMapper;

    @Override
    public PageVO<DistributionCommissionLog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionCommissionLogMapper.list());
    }

    @Override
    public DistributionCommissionLog getById(Long id) {
        return distributionCommissionLogMapper.getById(id);
    }

    @Override
    public void save(DistributionCommissionLog distributionCommissionLog) {
        distributionCommissionLogMapper.save(distributionCommissionLog);
    }

    @Override
    public void update(DistributionCommissionLog distributionCommissionLog) {
        distributionCommissionLogMapper.update(distributionCommissionLog);
    }

    @Override
    public void deleteById(Long id) {
        distributionCommissionLogMapper.deleteById(id);
    }
}
