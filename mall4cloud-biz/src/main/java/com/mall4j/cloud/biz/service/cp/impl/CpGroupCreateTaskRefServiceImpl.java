package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.CpGroupCreateTaskRefMapper;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTaskRef;
import com.mall4j.cloud.biz.model.cp.GroupTagRef;
import com.mall4j.cloud.biz.service.cp.CpGroupCreateTaskRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author gmq
 * @date 2024-01-19 15:49:54
 */
@Service
public class CpGroupCreateTaskRefServiceImpl extends ServiceImpl<CpGroupCreateTaskRefMapper, CpGroupCreateTaskRef> implements CpGroupCreateTaskRefService {

    @Autowired
    private CpGroupCreateTaskRefMapper cpGroupCreateTaskRefMapper;

    @Override
    public List<CpGroupCreateTaskRef> listByGroupId(String groupId) {
        return cpGroupCreateTaskRefMapper.listByGroupId(groupId);
    }

    @Override
    public void saveTo(String groupId,Integer scope, List<String> tagIds) {
        if(StrUtil.isEmpty(groupId) && CollUtil.isEmpty(tagIds)){
            return;
        }
        this.deleteByGroupId(groupId);

        List<CpGroupCreateTaskRef> groupTagRefs=new ArrayList<>();
        for (String tagId : tagIds) {
            CpGroupCreateTaskRef groupTagRef=new CpGroupCreateTaskRef();
            groupTagRef.setGroupId(groupId);
            groupTagRef.setScope(scope);
            groupTagRef.setTagId(tagId);
            groupTagRefs.add(groupTagRef);
        }
        this.saveBatch(groupTagRefs);
    }

    @Override
    public void deleteByGroupId(String groupId) {
        cpGroupCreateTaskRefMapper.deleteByGroupId(groupId);
    }
}
