package com.mall4j.cloud.user.manager;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.user.mapper.GroupTagRelationMapper;
import com.mall4j.cloud.user.model.GroupTagRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupTagRelationManager {


    @Autowired
    private GroupTagRelationMapper groupTagRelationMapper;


    @Transactional(rollbackFor = Exception.class)
    public Long addGroupTagRelation(Long groupId, Long tagId, Long createUser){

        GroupTagRelation groupTagRelation = new GroupTagRelation();

        groupTagRelation.setGroupId(groupId);
        groupTagRelation.setTagId(tagId);
        groupTagRelation.setCreateUser(createUser);

        groupTagRelationMapper.insert(groupTagRelation);

        return groupTagRelation.getGroupTagRelationId();
    }

    public Integer getTagCountByGroup(String tagName, Long groupId){
        Integer checkCount = groupTagRelationMapper.getTagCountByGroup(tagName, groupId);

        return checkCount;
    }

    public Long getRelationIdByGroupIdAndTagId(Long groupId, Long tagId){
        GroupTagRelation groupTagRelation = groupTagRelationMapper.selectOne(
                new LambdaQueryWrapper<GroupTagRelation>()
                        .eq(GroupTagRelation::getGroupId, groupId)
                        .eq(GroupTagRelation::getTagId, tagId)
        );


        return groupTagRelation.getGroupTagRelationId();
    }


    public void removeByTagIdAndGroupId(Long groupId, Long tagId){
        groupTagRelationMapper.delete(
                new LambdaQueryWrapper<GroupTagRelation>()
                        .eq(GroupTagRelation::getGroupId, groupId)
                        .eq(GroupTagRelation::getTagId, tagId)
        );
    }


}
