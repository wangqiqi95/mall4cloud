package com.mall4j.cloud.biz.service.cp.impl;

import com.mall4j.cloud.biz.mapper.cp.DistributionMomentsStoreMapper;
import com.mall4j.cloud.biz.model.cp.DistributionMomentsStore;
import com.mall4j.cloud.biz.service.cp.DistributionMomentsStoreService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分销推广-朋友圈门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Service
public class DistributionMomentsStoreServiceImpl implements DistributionMomentsStoreService {

    @Autowired
    private DistributionMomentsStoreMapper distributionMomentsStoreMapper;

    @Override
    public PageVO<DistributionMomentsStore> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionMomentsStoreMapper.list());
    }

    @Override
    public DistributionMomentsStore getById(Long id) {
        return distributionMomentsStoreMapper.getById(id);
    }

    @Override
    public void save(DistributionMomentsStore distributionMomentsStore) {
        distributionMomentsStoreMapper.save(distributionMomentsStore);
    }

    @Override
    public void update(DistributionMomentsStore distributionMomentsStore) {
        distributionMomentsStoreMapper.update(distributionMomentsStore);
    }

    @Override
    public void deleteById(Long id) {
        distributionMomentsStoreMapper.deleteById(id);
    }

    @Override
    public List<DistributionMomentsStore> listByMomentsId(Long momentsId) {
        return distributionMomentsStoreMapper.listByMomentsId(momentsId);
    }

    @Override
    public List<DistributionMomentsStore> listByStoreId(Long storeId) {
        return distributionMomentsStoreMapper.listByStoreId(storeId);
    }

    @Override
    public int countByMomentsId(Long momentsId) {
        return distributionMomentsStoreMapper.countByMomentsId(momentsId);
    }

    @Override
    public void deleteByMomentsIdNotInStoreIds(Long momentsId, List<Long> storeIds) {
        distributionMomentsStoreMapper.deleteByMomentsIdNotInStoreIds(momentsId, storeIds);
    }

    @Override
    public void deleteByMomentsId(Long momentsId) {
        distributionMomentsStoreMapper.deleteByMomentsId(momentsId);
    }
}
