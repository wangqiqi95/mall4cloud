package com.mall4j.cloud.flow.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.flow.model.ProductAnalyseUser;
import com.mall4j.cloud.flow.mapper.ProductAnalyseUserMapper;
import com.mall4j.cloud.flow.service.ProductAnalyseUserService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 流量分析—页面数据统计表
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
@Service
public class ProductAnalyseUserServiceImpl implements ProductAnalyseUserService {

    @Autowired
    private ProductAnalyseUserMapper productAnalyseUserMapper;

    @Override
    public PageVO<ProductAnalyseUser> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> productAnalyseUserMapper.list());
    }

    @Override
    public ProductAnalyseUser getByProductAnalyseUserId(Long productAnalyseUserId) {
        return productAnalyseUserMapper.getByProductAnalyseUserId(productAnalyseUserId);
    }

    @Override
    public void save(ProductAnalyseUser productAnalyseUser) {
        productAnalyseUserMapper.save(productAnalyseUser);
    }

    @Override
    public void update(ProductAnalyseUser productAnalyseUser) {
        productAnalyseUserMapper.update(productAnalyseUser);
    }

    @Override
    public void deleteById(Long productAnalyseUserId) {
        productAnalyseUserMapper.deleteById(productAnalyseUserId);
    }
}
