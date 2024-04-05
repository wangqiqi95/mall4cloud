package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSpuBind;
import com.mall4j.cloud.distribution.mapper.DistributionSpuBindMapper;
import com.mall4j.cloud.distribution.service.DistributionSpuBindService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户商品绑定信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
@Service
public class DistributionSpuBindServiceImpl implements DistributionSpuBindService {

    @Autowired
    private DistributionSpuBindMapper distributionSpuBindMapper;

    @Override
    public PageVO<DistributionSpuBind> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> distributionSpuBindMapper.list());
    }

    @Override
    public DistributionSpuBind getById(Long id) {
        return distributionSpuBindMapper.getById(id);
    }

    @Override
    public void save(DistributionSpuBind distributionSpuBind) {
        distributionSpuBindMapper.save(distributionSpuBind);
    }

    @Override
    public void update(DistributionSpuBind distributionSpuBind) {
        distributionSpuBindMapper.update(distributionSpuBind);
    }

    @Override
    public void deleteById(Long id) {
        distributionSpuBindMapper.deleteById(id);
    }
}
