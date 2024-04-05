package com.mall4j.cloud.user.manager;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.user.bo.AddPushSonTaskMediaBO;
import com.mall4j.cloud.user.bo.GroupPushSonTaskMediaBO;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMediaMapper;
import com.mall4j.cloud.user.model.GroupPushSonTaskMedia;
import com.mall4j.cloud.user.vo.GroupPushSonTaskMediaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class GroupPushSonTaskMediaManager {

    @Autowired
    private GroupPushSonTaskMediaMapper groupPushSonTaskMediaMapper;


    public void addBatch(List<AddPushSonTaskMediaBO> mediaBOList){
        groupPushSonTaskMediaMapper.insertBatch(mediaBOList);
    }

    public void updateBatch(List<GroupPushSonTaskMediaBO> mediaBOList){
        groupPushSonTaskMediaMapper.updateBatch(mediaBOList);
    }

    public List<GroupPushSonTaskMediaVO> getMediaListBySonTaskId(Long sonTaskId){
        return groupPushSonTaskMediaMapper.selectMediaListBySonTaskId(sonTaskId);
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeBySonTaskIdList(List<Long> sonTaskIdList){
        groupPushSonTaskMediaMapper.delete(
                new LambdaQueryWrapper<GroupPushSonTaskMedia>()
                        .in(GroupPushSonTaskMedia::getGroupPushSonTaskId, sonTaskIdList)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByMediaIdList(List<Long> mediaIdList){
        groupPushSonTaskMediaMapper.delete(
                new LambdaQueryWrapper<GroupPushSonTaskMedia>()
                        .in(GroupPushSonTaskMedia::getSonTaskMediaId, mediaIdList)
        );
    }
}
