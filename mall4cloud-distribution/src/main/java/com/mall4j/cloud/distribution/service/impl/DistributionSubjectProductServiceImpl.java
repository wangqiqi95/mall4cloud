package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSubjectProduct;
import com.mall4j.cloud.distribution.mapper.DistributionSubjectProductMapper;
import com.mall4j.cloud.distribution.service.DistributionSubjectProductService;
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
public class DistributionSubjectProductServiceImpl implements DistributionSubjectProductService {

    @Autowired
    private DistributionSubjectProductMapper distributionSubjectProductMapper;

    @Override
    public PageVO<DistributionSubjectProduct> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionSubjectProductMapper.list());
    }

    @Override
    public DistributionSubjectProduct getById(Long id) {
        return distributionSubjectProductMapper.getById(id);
    }

    @Override
    public void save(DistributionSubjectProduct distributionSubjectProduct) {
        distributionSubjectProductMapper.save(distributionSubjectProduct);
    }

    @Override
    public void update(DistributionSubjectProduct distributionSubjectProduct) {
        distributionSubjectProductMapper.update(distributionSubjectProduct);
    }

    @Override
    public void deleteById(Long id) {
        distributionSubjectProductMapper.deleteById(id);
    }

    @Override
    public List<DistributionSubjectProduct> listBySubjectId(Long subjectId) {
        return distributionSubjectProductMapper.listBySubjectId(subjectId);
    }

    @Override
    public void deleteBySubjectIdNotInProductIds(Long subjectId, List<Long> productIds) {
        distributionSubjectProductMapper.deleteBySubjectIdNotInProductIds(subjectId, productIds);
    }

    @Override
    public void deleteBySubjectId(Long subjectId) {
        distributionSubjectProductMapper.deleteBySubjectId(subjectId);
    }
}
