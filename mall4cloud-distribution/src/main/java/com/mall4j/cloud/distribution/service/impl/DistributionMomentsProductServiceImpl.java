package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionMomentsProduct;
import com.mall4j.cloud.distribution.mapper.DistributionMomentsProductMapper;
import com.mall4j.cloud.distribution.service.DistributionMomentsProductService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 分销推广-朋友圈商品
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
@Service
public class DistributionMomentsProductServiceImpl implements DistributionMomentsProductService {

    @Autowired
    private DistributionMomentsProductMapper distributionMomentsProductMapper;

    @Override
    public PageVO<DistributionMomentsProduct> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionMomentsProductMapper.list());
    }

    @Override
    public DistributionMomentsProduct getById(Long id) {
        return distributionMomentsProductMapper.getById(id);
    }

    @Override
    public void save(DistributionMomentsProduct distributionMomentsProduct) {
        distributionMomentsProductMapper.save(distributionMomentsProduct);
    }

    @Override
    public void update(DistributionMomentsProduct distributionMomentsProduct) {
        distributionMomentsProductMapper.update(distributionMomentsProduct);
    }

    @Override
    public void deleteById(Long id) {
        distributionMomentsProductMapper.deleteById(id);
    }

    @Override
    public List<DistributionMomentsProduct> listByMomentsId(Long momentsId) {
        return distributionMomentsProductMapper.listByMomentsId(momentsId);
    }

    @Override
    public List<DistributionMomentsProduct> listByMomentsIdList(List<Long> momentsIdList) {
        return distributionMomentsProductMapper.listByMomentsIdList(momentsIdList);
    }

    @Override
    public void deleteByMomentsIdNotInProductIds(Long momentsId, List<Long> productIds) {
        distributionMomentsProductMapper.deleteByMomentsIdNotInProductIds(momentsId, productIds);
    }

    @Override
    public void deleteByMomentsId(Long momentsId) {
        distributionMomentsProductMapper.deleteByMomentsId(momentsId);
    }
}
