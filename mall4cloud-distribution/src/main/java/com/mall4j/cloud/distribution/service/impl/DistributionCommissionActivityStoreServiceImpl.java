package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityStoreDTO;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityStoreMapper;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivity;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivityStore;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivityStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 佣金配置-活动佣金-门店
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Service
public class DistributionCommissionActivityStoreServiceImpl implements DistributionCommissionActivityStoreService {

    @Autowired
    private DistributionCommissionActivityStoreMapper distributionCommissionActivityStoreMapper;
    @Autowired
    private DistributionCommissionActivityMapper distributionCommissionActivityMapper;


    @Override
    public List<Long> findStoreIdListByActivityId(Long activityId) {
        List<DistributionCommissionActivityStore> commissionActivityStoreList = distributionCommissionActivityStoreMapper
                .listByActivityId(activityId);
        if (!CollectionUtils.isEmpty(commissionActivityStoreList)) {
            return commissionActivityStoreList.stream().map(DistributionCommissionActivityStore :: getStoreId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void save(DistributionCommissionActivityStoreDTO distributionCommissionActivityStoreDTO) {
        if (CollectionUtils.isEmpty(distributionCommissionActivityStoreDTO.getStoreIdList())) {
            throw new LuckException("门店ID集合不能为空");
        }
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper.getById(
                distributionCommissionActivityStoreDTO.getActivityId());
        if (Objects.isNull(distributionCommissionActivity)) {
            throw new LuckException("活动不存在");
        }
        List<DistributionCommissionActivityStore> distributionCommissionActivityStoreList = distributionCommissionActivityStoreMapper
                .listByActivityId(distributionCommissionActivity.getId());
        List<Long> activityStoreIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributionCommissionActivityStoreList)) {
            activityStoreIdList = distributionCommissionActivityStoreList.stream().map(DistributionCommissionActivityStore:: getStoreId)
                    .collect(Collectors.toList());
        }
        List<Long> finalActivityStoreIdList = activityStoreIdList;
        List<Long> diffStoreIdList = distributionCommissionActivityStoreDTO.getStoreIdList().stream().filter(t ->
                !finalActivityStoreIdList.contains(t)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(diffStoreIdList)) {
            List<DistributionCommissionActivityStore> activityStoreList = diffStoreIdList.stream().map(s -> {
                DistributionCommissionActivityStore commissionActivityStore = new DistributionCommissionActivityStore();
                commissionActivityStore.setActivityId(distributionCommissionActivity.getId());
                commissionActivityStore.setStoreId(s);
                commissionActivityStore.setOrgId(0l);
                return commissionActivityStore;
            }).collect(Collectors.toList());
            distributionCommissionActivityStoreMapper.saveBatch(activityStoreList);
        }
    }

    @Override
    public void deleteByActivityIdAndStoreId(Long activityId, Long storeId) {
        distributionCommissionActivityStoreMapper.deleteByActivityIdAndStoreId(activityId, storeId);
    }
}
