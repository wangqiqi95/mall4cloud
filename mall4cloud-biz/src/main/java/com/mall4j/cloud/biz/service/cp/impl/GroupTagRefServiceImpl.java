package com.mall4j.cloud.biz.service.cp.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.cp.GroupTagRefMapper;
import com.mall4j.cloud.biz.model.cp.GroupTagRef;
import com.mall4j.cloud.biz.service.cp.GroupTagRefService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author hwy
 * @date 2022-02-16 12:01:07
 */
@RequiredArgsConstructor
@Service
public class GroupTagRefServiceImpl extends ServiceImpl<GroupTagRefMapper,GroupTagRef> implements GroupTagRefService {

    private final  GroupTagRefMapper groupTagRefMapper;

    @Override
    public PageVO<GroupTagRef> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, groupTagRefMapper::list);
    }

    @Override
    public List<GroupTagRef> getByGroupId(String groupId) {
        return groupTagRefMapper.getByGroupId(groupId);
    }

    @Override
    public void deleteByGroupId(String groupId) {
        groupTagRefMapper.deleteByGroupId(groupId);
    }

    @Override
    public void saveTo(String groupId, List<String> tagIds) {
        if(StrUtil.isNotEmpty(groupId) && CollUtil.isNotEmpty(tagIds)){
            return;
        }
        this.deleteByGroupId(groupId);

        List<GroupTagRef> groupTagRefs=new ArrayList<>();
        for (String tagId : tagIds) {
            GroupTagRef groupTagRef=new GroupTagRef();
            groupTagRef.setGroupId(groupId);
            groupTagRef.setTagId(tagId);
            groupTagRefs.add(groupTagRef);
        }
        this.saveBatch(groupTagRefs);
    }
}
