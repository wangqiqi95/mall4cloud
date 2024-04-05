package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.distribution.mapper.DistributionCommissionUnityMapper;
import com.mall4j.cloud.distribution.model.DistributionCommissionUnity;
import com.mall4j.cloud.distribution.service.DistributionCommissionUnityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 佣金配置-统一佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Service
public class DistributionCommissionUnityServiceImpl implements DistributionCommissionUnityService {

    @Autowired
    private DistributionCommissionUnityMapper distributionCommissionUnityMapper;

    @Override
    public DistributionCommissionUnity get() {
        return distributionCommissionUnityMapper.get();
    }

    @Override
    public void save(DistributionCommissionUnity distributionCommissionUnity) {
        distributionCommissionUnityMapper.save(distributionCommissionUnity);
    }

    @Override
    public void update(DistributionCommissionUnity distributionCommissionUnity) {
        distributionCommissionUnityMapper.update(distributionCommissionUnity);
    }
}
