package com.mall4j.cloud.user.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.biz.dto.cp.media.UploadUrlMediaDTO;
import com.mall4j.cloud.api.biz.feign.WeixinCpMediaFeignClient;
import com.mall4j.cloud.api.biz.vo.WeixinUploadMediaResultVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.constant.CommonDeleteFlagEnum;
import com.mall4j.cloud.user.bo.AddGroupPushSonTaskBO;
import com.mall4j.cloud.user.bo.GroupPushSonTaskBO;
import com.mall4j.cloud.user.mapper.GroupPushSonTaskMapper;
import com.mall4j.cloud.user.model.GroupPushSonTask;
import com.mall4j.cloud.user.vo.GroupPushSonTaskVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupPushSonTaskManager {


    @Autowired
    private GroupPushSonTaskMapper groupPushSonTaskMapper;

    @Autowired
    private MapperFacade mapperFacade;


    @Transactional(rollbackFor = Exception.class)
    public Long add(AddGroupPushSonTaskBO addGroupPushSonTaskBO){
        GroupPushSonTask groupPushSonTask = new GroupPushSonTask();
        BeanUtils.copyProperties(addGroupPushSonTaskBO, groupPushSonTask);
        groupPushSonTask.setDeleteFlag(1);
        groupPushSonTaskMapper.insert(groupPushSonTask);
        return groupPushSonTask.getGroupPushSonTaskId();
    }


    public List<GroupPushSonTaskVO> getTheSonTaskListByTaskId(Long pushTaskId){
        List<GroupPushSonTask> groupPushSonTaskList = groupPushSonTaskMapper.selectList(
                new LambdaQueryWrapper<GroupPushSonTask>()
                        .eq(GroupPushSonTask::getGroupPushTaskId, pushTaskId));

        List<GroupPushSonTaskVO> sonTaskVOList = groupPushSonTaskList.stream().map(groupPushSonTask -> {

            GroupPushSonTaskVO taskVO = new GroupPushSonTaskVO();

            BeanUtils.copyProperties(groupPushSonTask, taskVO);

            return taskVO;
        }).collect(Collectors.toList());
        return sonTaskVOList;
    }


    @Transactional(rollbackFor = Exception.class)
    public void removeBySonTaskIdList(List<Long> sonTaskIdList){

//        groupPushSonTaskMapper.delete(
//                new LambdaQueryWrapper<GroupPushSonTask>()
//                        .in(GroupPushSonTask::getGroupPushSonTaskId, sonTaskIdList)
//        );

      /*  List<GroupPushSonTask> groupPushSonTasks = groupPushSonTaskMapper.selectList(
                new LambdaQueryWrapper<GroupPushSonTask>()
                        .in(GroupPushSonTask::getGroupPushSonTaskId, sonTaskIdList)
        );

        groupPushSonTasks.stream().forEach(groupPushSonTask -> {
            groupPushSonTask.setDeleteFlag(CommonDeleteFlagEnum.IS_REMOVE.getDeleteFlag());
            groupPushSonTaskMapper.updateById(groupPushSonTask);
        });*/

    }


    @Transactional(rollbackFor = Exception.class)
    public void update(GroupPushSonTaskBO groupPushSonTaskBO){

        GroupPushSonTask groupPushSonTask = groupPushSonTaskMapper.selectById(groupPushSonTaskBO.getGroupPushSonTaskId());
        groupPushSonTask.setSonTaskName(groupPushSonTaskBO.getSonTaskName());
        groupPushSonTask.setPushContent(groupPushSonTaskBO.getPushContent());
        groupPushSonTask.setStartTime(groupPushSonTaskBO.getStartTime());
        groupPushSonTask.setEndTime(groupPushSonTaskBO.getEndTime());

        groupPushSonTaskMapper.updateById(groupPushSonTask);
    }


    public GroupPushSonTaskVO getById(Long sonTaskId){

        GroupPushSonTask groupPushSonTask = groupPushSonTaskMapper.selectById(sonTaskId);

        GroupPushSonTaskVO sonTaskVO = new GroupPushSonTaskVO();

        BeanUtils.copyProperties(groupPushSonTask, sonTaskVO);

        return sonTaskVO;
    }

    public List<Long> getExpiredGroupPushTaskList(){
        return groupPushSonTaskMapper.getExpiredGroupPushTaskList();
    }

    public List<Long> getNotFinishedGroupPushTaskList(){
        return groupPushSonTaskMapper.getNotFinishedGroupPushTaskList();
    }

    public List<Long> getFinishedGroupPushTaskList(){
        return groupPushSonTaskMapper.getFinishedGroupPushTaskList();
    }

    public List<GroupPushSonTaskVO> getUnfailedPushSonTask(){
        List<GroupPushSonTask> groupPushSonTasks = groupPushSonTaskMapper.selectList(
                new LambdaQueryWrapper<GroupPushSonTask>()
                        .lt(GroupPushSonTask::getEndTime, LocalDateTime.now())
        );

        List<GroupPushSonTaskVO> sonTaskVOList = mapperFacade.mapAsList(groupPushSonTasks, GroupPushSonTaskVO.class);
        return sonTaskVOList;
    }

}
