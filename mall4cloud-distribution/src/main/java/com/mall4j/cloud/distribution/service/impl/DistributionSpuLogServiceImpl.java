package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSpuLog;
import com.mall4j.cloud.distribution.mapper.DistributionSpuLogMapper;
import com.mall4j.cloud.distribution.service.DistributionSpuLogService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 分销商品浏览记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@Service
public class DistributionSpuLogServiceImpl implements DistributionSpuLogService {

    @Autowired
    private DistributionSpuLogMapper distributionSpuLogMapper;

    @Override
    public PageVO<DistributionSpuLog> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionSpuLogMapper.list());
    }

    @Override
    public DistributionSpuLog getByDistributionSpuLogId(Long distributionSpuLogId) {
        return distributionSpuLogMapper.getByDistributionSpuLogId(distributionSpuLogId);
    }

    @Override
    public void save(DistributionSpuLog distributionSpuLog) {
        distributionSpuLogMapper.save(distributionSpuLog);
    }

    @Override
    public void update(DistributionSpuLog distributionSpuLog) {
        distributionSpuLogMapper.update(distributionSpuLog);
    }

    @Override
    public void deleteById(Long distributionSpuLogId) {
        distributionSpuLogMapper.deleteById(distributionSpuLogId);
    }
}
