package com.mall4j.cloud.user.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.CrmUserTagRelation;
import com.mall4j.cloud.user.mapper.CrmUserTagRelationMapper;
import com.mall4j.cloud.user.service.CrmUserTagRelationService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数云维护 用户标签关系表
 *
 * @author FrozenWatermelon
 * @date 2023-11-25 10:41:27
 */
@Service
public class CrmUserTagRelationServiceImpl implements CrmUserTagRelationService {

    @Autowired
    private CrmUserTagRelationMapper crmUserTagRelationMapper;

    @Override
    public PageVO<CrmUserTagRelation> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> crmUserTagRelationMapper.list());
    }

    @Override
    public CrmUserTagRelation getById(Long id) {
        return crmUserTagRelationMapper.getById(id);
    }

    @Override
    public void save(CrmUserTagRelation crmUserTagRelation) {
        crmUserTagRelationMapper.save(crmUserTagRelation);
    }

    @Override
    public void update(CrmUserTagRelation crmUserTagRelation) {
        crmUserTagRelationMapper.update(crmUserTagRelation);
    }

    @Override
    public void deleteById(Long id) {
        crmUserTagRelationMapper.deleteById(id);
    }
}
