package com.mall4j.cloud.biz.service.cp.impl;

import com.aliyuncs.ecs.transform.v20140526.RemoveTagsResponseUnmarshaller;
import com.mall4j.cloud.biz.mapper.cp.CpMaterialLableMapper;
import com.mall4j.cloud.biz.model.cp.CpMaterialLable;
import com.mall4j.cloud.biz.service.cp.CpMaterialLableService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 素材 互动雷达标签
 *
 * @author FrozenWatermelon
 * @date 2023-10-24 18:42:12
 */
@Service
public class CpMaterialLableServiceImpl implements CpMaterialLableService {

    @Autowired
    private CpMaterialLableMapper cpMaterialLableMapper;

    @Override
    public PageVO<CpMaterialLable> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> cpMaterialLableMapper.list());
    }

    @Override
    public CpMaterialLable getById(Long id) {
        return cpMaterialLableMapper.getById(id);
    }

    @Override
    public void save(CpMaterialLable cpMaterialLable) {
        cpMaterialLableMapper.save(cpMaterialLable);
    }

    @Override
    public void update(CpMaterialLable cpMaterialLable) {
        cpMaterialLableMapper.update(cpMaterialLable);
    }

    @Override
    public void deleteById(Long id) {
        cpMaterialLableMapper.deleteById(id);
    }

    public void deleteByMatId(Long id) {
        cpMaterialLableMapper.deleteByMatId(id);
    }

    @Override
    public List<CpMaterialLable> listByMatId(Long matId) {
        return cpMaterialLableMapper.listByMatId(matId);
    }
}
