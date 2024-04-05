package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.distribution.mapper.DistributionStoreActivitySubscribeMapper;
import com.mall4j.cloud.distribution.model.DistributionStoreActivitySubscribe;
import com.mall4j.cloud.distribution.service.DistributionStoreActivitySubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 门店活动-用户订阅
 *
 * @author gww
 * @date 2022-01-28 23:24:49
 */
@Service
public class DistributionStoreActivitySubscribeServiceImpl implements DistributionStoreActivitySubscribeService {

    @Autowired
    private DistributionStoreActivitySubscribeMapper distributionStoreActivitySubscribeMapper;

    @Override
    public DistributionStoreActivitySubscribe getByUserId(Long userId) {
        return distributionStoreActivitySubscribeMapper.getByUserId(userId);
    }

    @Override
    public void subscribe(Long userId) {
        DistributionStoreActivitySubscribe distributionStoreActivitySubscribe = new DistributionStoreActivitySubscribe();
        distributionStoreActivitySubscribe.setIsSubscribe(1);
        distributionStoreActivitySubscribe.setOrgId(0l);
        distributionStoreActivitySubscribe.setUserId(userId);
        distributionStoreActivitySubscribeMapper.save(distributionStoreActivitySubscribe);
    }

    @Override
    public void cancelSubscribe(Long userId) {
        distributionStoreActivitySubscribeMapper.cancelSubscribe(userId);
    }
}
