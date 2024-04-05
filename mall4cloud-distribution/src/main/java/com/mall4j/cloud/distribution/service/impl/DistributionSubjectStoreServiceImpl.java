package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSubjectStore;
import com.mall4j.cloud.distribution.mapper.DistributionSubjectStoreMapper;
import com.mall4j.cloud.distribution.service.DistributionSubjectStoreService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Service
public class DistributionSubjectStoreServiceImpl implements DistributionSubjectStoreService {

    @Autowired
    private DistributionSubjectStoreMapper distributionSubjectStoreMapper;

    @Override
    public PageVO<DistributionSubjectStore> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionSubjectStoreMapper.list());
    }

    @Override
    public DistributionSubjectStore getById(Long id) {
        return distributionSubjectStoreMapper.getById(id);
    }

    @Override
    public void save(DistributionSubjectStore distributionSubjectStore) {
        distributionSubjectStoreMapper.save(distributionSubjectStore);
    }

    @Override
    public void update(DistributionSubjectStore distributionSubjectStore) {
        distributionSubjectStoreMapper.update(distributionSubjectStore);
    }

    @Override
    public void deleteById(Long id) {
        distributionSubjectStoreMapper.deleteById(id);
    }

    @Override
    public List<DistributionSubjectStore> listBySubjectId(Long subjectId) {
        return distributionSubjectStoreMapper.listBySubjectId(subjectId);
    }

    @Override
    public List<DistributionSubjectStore> listByStoreId(Long storeId) {
        return distributionSubjectStoreMapper.listByStoreId(storeId);
    }

    @Override
    public List<DistributionSubjectStore> listInStoreIds(List<Long> storeIds) {
        return distributionSubjectStoreMapper.listInStoreIds(storeIds);
    }

    @Override
    public void deleteBySubjectIdNotInStoreIds(Long subjectId, List<Long> storeIds) {
        distributionSubjectStoreMapper.deleteBySubjectIdNotInStoreIds(subjectId, storeIds);
    }

    @Override
    public void deleteBySubjectId(Long subjectId) {
        distributionSubjectStoreMapper.deleteBySubjectId(subjectId);
    }

    @Override
    public int countBySpecialSubjectId(Long subjectId) {
        return distributionSubjectStoreMapper.countBySpecialSubjectId(subjectId);
    }
}
