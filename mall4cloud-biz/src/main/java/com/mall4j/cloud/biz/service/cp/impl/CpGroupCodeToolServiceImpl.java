package com.mall4j.cloud.biz.service.cp.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.CpGroupCodeToolMapper;
import com.mall4j.cloud.biz.model.cp.CpGroupCodeTool;
import com.mall4j.cloud.biz.service.cp.CpGroupCodeToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自动拉群与群活码关联表
 *
 * @author gmq
 * @date 2023-11-06 14:47:45
 */
@Service
public class CpGroupCodeToolServiceImpl extends ServiceImpl<CpGroupCodeToolMapper, CpGroupCodeTool> implements CpGroupCodeToolService {

    @Autowired
    private CpGroupCodeToolMapper cpAutoGroupRelMapper;

    @Override
    public CpGroupCodeTool getByRelGroupId(Long autoGroupId,Integer codeFrom) {
        return cpAutoGroupRelMapper.getByRelGroupId(autoGroupId,codeFrom);
    }

    @Override
    public CpGroupCodeTool getByGroupId(Long groupId) {
        return cpAutoGroupRelMapper.getByGroupId(groupId);
    }
}
