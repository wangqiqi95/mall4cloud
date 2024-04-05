package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawProcessLog;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawProcessLogMapper;
import com.mall4j.cloud.distribution.service.DistributionWithdrawProcessLogService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 佣金处理批次记录
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:49
 */
@Service
public class DistributionWithdrawProcessLogServiceImpl implements DistributionWithdrawProcessLogService {

    @Autowired
    private DistributionWithdrawProcessLogMapper distributionWithdrawProcessLogMapper;

    @Override
    public PageVO<DistributionWithdrawProcessLog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionWithdrawProcessLogMapper.list());
    }

    @Override
    public DistributionWithdrawProcessLog getById(Long id) {
        return distributionWithdrawProcessLogMapper.getById(id);
    }

    @Override
    public void save(DistributionWithdrawProcessLog distributionWithdrawProcessLog) {
        distributionWithdrawProcessLogMapper.save(distributionWithdrawProcessLog);
    }

    @Override
    public void update(DistributionWithdrawProcessLog distributionWithdrawProcessLog) {
        distributionWithdrawProcessLogMapper.update(distributionWithdrawProcessLog);
    }

    @Override
    public void deleteById(Long id) {
        distributionWithdrawProcessLogMapper.deleteById(id);
    }
}
