package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionPosterStore;
import com.mall4j.cloud.distribution.mapper.DistributionPosterStoreMapper;
import com.mall4j.cloud.distribution.service.DistributionPosterStoreService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 分销推广-海报门店
 *
 * @author ZengFanChang
 * @date 2022-01-03 20:00:28
 */
@Service
public class DistributionPosterStoreServiceImpl implements DistributionPosterStoreService {

    @Autowired
    private DistributionPosterStoreMapper distributionPosterStoreMapper;

    @Override
    public PageVO<DistributionPosterStore> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionPosterStoreMapper.list());
    }

    @Override
    public DistributionPosterStore getById(Long id) {
        return distributionPosterStoreMapper.getById(id);
    }

    @Override
    public void save(DistributionPosterStore distributionPosterStore) {
        distributionPosterStoreMapper.save(distributionPosterStore);
    }

    @Override
    public void update(DistributionPosterStore distributionPosterStore) {
        distributionPosterStoreMapper.update(distributionPosterStore);
    }

    @Override
    public void deleteById(Long id) {
        distributionPosterStoreMapper.deleteById(id);
    }

    @Override
    public DistributionPosterStore getByPosterAndStore(Long posterId, Long storeId) {
        return distributionPosterStoreMapper.getByPosterAndStore(posterId, storeId);
    }

    @Override
    public List<DistributionPosterStore> listByPosterId(Long posterId) {
        return distributionPosterStoreMapper.listByPosterId(posterId);
    }

    @Override
    public void deleteByPosterId(Long posterId) {
        distributionPosterStoreMapper.deleteByPosterId(posterId);
    }

    @Override
    public void deleteByPosterAndNotInStore(Long posterId, List<Long> storeIds) {
        distributionPosterStoreMapper.deleteByPosterAndNotInStore(posterId, storeIds);
    }

    @Override
    public List<DistributionPosterStore> listByStoreId(Long storeId) {
        return distributionPosterStoreMapper.listByStoreId(storeId);
    }

    @Override
    public List<DistributionPosterStore> listByPosterIdList(List<Long> posterIdList) {
        return distributionPosterStoreMapper.listByPosterIdList(posterIdList);
    }
}
