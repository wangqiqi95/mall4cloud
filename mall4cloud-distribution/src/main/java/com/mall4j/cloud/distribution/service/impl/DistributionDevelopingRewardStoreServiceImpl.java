package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionDevelopingRewardStore;
import com.mall4j.cloud.distribution.mapper.DistributionDevelopingRewardStoreMapper;
import com.mall4j.cloud.distribution.service.DistributionDevelopingRewardStoreService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 分销推广-发展奖励门店
 *
 * @author ZengFanChang
 * @date 2021-12-26 17:38:24
 */
@Service
public class DistributionDevelopingRewardStoreServiceImpl implements DistributionDevelopingRewardStoreService {

    @Autowired
    private DistributionDevelopingRewardStoreMapper distributionDevelopingRewardStoreMapper;

    @Override
    public PageVO<DistributionDevelopingRewardStore> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionDevelopingRewardStoreMapper.list());
    }

    @Override
    public DistributionDevelopingRewardStore getById(Long id) {
        return distributionDevelopingRewardStoreMapper.getById(id);
    }

    @Override
    public void save(DistributionDevelopingRewardStore distributionDevelopingRewardStore) {
        distributionDevelopingRewardStoreMapper.save(distributionDevelopingRewardStore);
    }

    @Override
    public void update(DistributionDevelopingRewardStore distributionDevelopingRewardStore) {
        distributionDevelopingRewardStoreMapper.update(distributionDevelopingRewardStore);
    }

    @Override
    public void deleteById(Long id) {
        distributionDevelopingRewardStoreMapper.deleteById(id);
    }

    @Override
    public List<DistributionDevelopingRewardStore> listByRewardId(Long rewardId) {
        return distributionDevelopingRewardStoreMapper.listByRewardId(rewardId);
    }

    @Override
    public List<DistributionDevelopingRewardStore> listByStoreId(Long storeId) {
        return distributionDevelopingRewardStoreMapper.listByStoreId(storeId);
    }

    @Override
    public void deleteByRewardIdNotInStoreIds(Long rewardId, List<Long> storeIds) {
        distributionDevelopingRewardStoreMapper.deleteByRewardIdNotInStoreIds(rewardId, storeIds);
    }

    @Override
    public void deleteByRewardId(Long rewardId){
        distributionDevelopingRewardStoreMapper.deleteByRewardId(rewardId);
    }
}
