package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawConfig;
import com.mall4j.cloud.distribution.mapper.DistributionWithdrawConfigMapper;
import com.mall4j.cloud.distribution.service.DistributionWithdrawConfigService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 佣金管理-佣金提现配置
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:21:36
 */
@Service
public class DistributionWithdrawConfigServiceImpl implements DistributionWithdrawConfigService {

    @Autowired
    private DistributionWithdrawConfigMapper distributionWithdrawConfigMapper;

    @Override
    public PageVO<DistributionWithdrawConfig> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionWithdrawConfigMapper.list());
    }

    @Override
    public DistributionWithdrawConfig getById(Long id) {
        return distributionWithdrawConfigMapper.getById(id);
    }

    @Override
    public void save(DistributionWithdrawConfig distributionWithdrawConfig) {
        DistributionWithdrawConfig config = getByIdentityType(distributionWithdrawConfig.getIdentityType());
        if (null != config){
            distributionWithdrawConfig.setId(config.getId());
            distributionWithdrawConfigMapper.update(distributionWithdrawConfig);
            return;
        }
        distributionWithdrawConfigMapper.save(distributionWithdrawConfig);
    }

    @Override
    public void update(DistributionWithdrawConfig distributionWithdrawConfig) {
        distributionWithdrawConfigMapper.update(distributionWithdrawConfig);
    }

    @Override
    public void deleteById(Long id) {
        distributionWithdrawConfigMapper.deleteById(id);
    }

    @Override
    public DistributionWithdrawConfig getByIdentityType(Integer identityType) {
        return distributionWithdrawConfigMapper.getByIdentityType(identityType);
    }
}
