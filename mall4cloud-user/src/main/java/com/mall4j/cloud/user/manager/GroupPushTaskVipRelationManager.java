package com.mall4j.cloud.user.manager;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.mall4j.cloud.user.bo.AddGroupPushTaskVipRelationBO;
import com.mall4j.cloud.user.constant.FriendStatusTypeEnum;
import com.mall4j.cloud.user.constant.StaffCpFriendStateEnum;
import com.mall4j.cloud.user.mapper.GroupPushTaskVipRelationMapper;
import com.mall4j.cloud.user.model.GroupPushTaskVipRelation;
import com.mall4j.cloud.api.user.vo.GroupPushTaskVipRelationVO;
import com.mall4j.cloud.user.vo.StaffPushTaskAddFriendCountVO;
import com.mall4j.cloud.user.vo.StaffPushTaskIssueCountVO;
import com.mall4j.cloud.user.vo.StaffPushTaskNotAddFriendCountVO;
import com.mall4j.cloud.user.vo.UsableGroupPushVipVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class GroupPushTaskVipRelationManager {


    @Autowired
    private GroupPushTaskVipRelationMapper groupPushTaskVipRelationMapper;

    @Autowired
    private MapperFacade mapperFacade;

    private static final Integer BATCH_SIZE = 500;

    @Transactional(rollbackFor = Exception.class)
    public void addBatch(List<AddGroupPushTaskVipRelationBO> relationBOList){

        List<List<AddGroupPushTaskVipRelationBO>> partition = Lists.partition(relationBOList, BATCH_SIZE);

        partition.stream().forEach(relationList ->
            groupPushTaskVipRelationMapper.insertBatch(relationList)
        );

    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByPushTaskId(Long pushTaskId){

        groupPushTaskVipRelationMapper.delete(
                new LambdaQueryWrapper<GroupPushTaskVipRelation>()
                        .eq(GroupPushTaskVipRelation::getGroupPushTaskId, pushTaskId)
        );

    }


    public List<StaffPushTaskIssueCountVO> getThePushIssueCountByPushTaskIdAndStaff(Long pushTaskId, List<Long> staffIdList){
        return groupPushTaskVipRelationMapper.selectIssueCount(pushTaskId, staffIdList);
    }


    public Integer getTheCountByPushTaskId(Long pushTaskId, Long staffId){
        LambdaQueryWrapper<GroupPushTaskVipRelation> queryWrapper = new LambdaQueryWrapper<GroupPushTaskVipRelation>()
                .eq(GroupPushTaskVipRelation::getGroupPushTaskId, pushTaskId);
        if (Objects.nonNull(staffId)){
            queryWrapper.eq(GroupPushTaskVipRelation::getStaffId, staffId);
        }else {
            queryWrapper.ne(GroupPushTaskVipRelation::getStaffId, 0);
        }
        Integer issueCount = groupPushTaskVipRelationMapper.selectCount(
              queryWrapper
        );

        return issueCount;
    }


    public Integer getTheAddFriendCountByPushTaskId(Long pushTaskId){
//        Integer addFriendCount = groupPushTaskVipRelationMapper
//                .selectTheAddFriendCountByPushTaskId(pushTaskId);
        Integer addFriendCount = groupPushTaskVipRelationMapper.selectCount(
                new LambdaQueryWrapper<GroupPushTaskVipRelation>()
                        .eq(GroupPushTaskVipRelation::getGroupPushTaskId, pushTaskId)
                        .eq(GroupPushTaskVipRelation::getFriendState, StaffCpFriendStateEnum.YES.getFriendState())
        );
        return addFriendCount;
    }

    public List<StaffPushTaskAddFriendCountVO> getGroupByIssueCountByPushTaskId(Long pushTaskId, List<Long> staffIdList){
        List<StaffPushTaskAddFriendCountVO> staffPushTaskAddFriendCountVOS = groupPushTaskVipRelationMapper
                .selectGroupByAddFriendCountByPushTaskId(pushTaskId, staffIdList);
        return staffPushTaskAddFriendCountVOS;
    }

    public List<GroupPushTaskVipRelationVO> getTheVipByTaskIdAndStaffId(Long taskId, Long staffId, Long userId){
        List<GroupPushTaskVipRelation> groupPushTaskVipRelationList = groupPushTaskVipRelationMapper.selectList(
                new LambdaQueryWrapper<GroupPushTaskVipRelation>()
                        .eq(GroupPushTaskVipRelation::getGroupPushTaskId, taskId)
                        .eq(GroupPushTaskVipRelation::getStaffId, staffId)
                        .eq(GroupPushTaskVipRelation::getVipUserId, userId)
        );
        List<GroupPushTaskVipRelationVO> groupPushTaskVipRelationVOList = mapperFacade.mapAsList(groupPushTaskVipRelationList, GroupPushTaskVipRelationVO.class);

        return groupPushTaskVipRelationVOList;
    }

    public List<GroupPushTaskVipRelationVO> getTheVipListByTaskIdAndStaffId(Long taskId, Long staffId){
        List<GroupPushTaskVipRelation> groupPushTaskVipRelationList = groupPushTaskVipRelationMapper.selectList(
                new LambdaQueryWrapper<GroupPushTaskVipRelation>()
                        .eq(GroupPushTaskVipRelation::getGroupPushTaskId, taskId)
                        .eq(GroupPushTaskVipRelation::getStaffId, staffId)
        );
        List<GroupPushTaskVipRelationVO> groupPushTaskVipRelationVOList = mapperFacade.mapAsList(groupPushTaskVipRelationList, GroupPushTaskVipRelationVO.class);

        return groupPushTaskVipRelationVOList;
    }

    public List<GroupPushTaskVipRelationVO> getTheFriendVipListByTaskIdAndStaffId(Long taskId, Long staffId){
        List<GroupPushTaskVipRelation> groupPushTaskVipRelationList = groupPushTaskVipRelationMapper.selectList(
                new LambdaQueryWrapper<GroupPushTaskVipRelation>()
                        .eq(GroupPushTaskVipRelation::getGroupPushTaskId, taskId)
                        .eq(GroupPushTaskVipRelation::getStaffId, staffId)
                        .eq(GroupPushTaskVipRelation::getFriendState, FriendStatusTypeEnum.IS_FRIEND.value())
        );
        List<GroupPushTaskVipRelationVO> groupPushTaskVipRelationVOList = mapperFacade.mapAsList(groupPushTaskVipRelationList, GroupPushTaskVipRelationVO.class);

        return groupPushTaskVipRelationVOList;
    }

    /**
     * 修改任务与用户关联表将好友状态改为【未加好友】
     * @param taskId 任务ID
     * @param staffId 导购ID
     * @param userId 用户ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStaffAndUserTaskRelation(Long taskId, Long staffId, Long userId){
        /*groupPushTaskVipRelationMapper.update(new LambdaUpdateWrapper<GroupPushTaskVipRelation>()
                        .set(GroupPushTaskVipRelation::getFriendState, 0)
                        .eq(GroupPushTaskVipRelation::getGroupPushTaskId, taskId)
                        .eq(GroupPushTaskVipRelation::getStaffId, staffId)
                        .eq(GroupPushTaskVipRelation::getVipUserId, userId)
        );*/
        groupPushTaskVipRelationMapper.updateStaffAndUserTaskRelation(taskId, staffId, userId);
    }

    /**
     * 根据taskVipRelationVO里的 pushTaskId 和 staffId 去查询 t_group_push_task_vip_relation 表中任务应该触达的用户数
     * @param pushTaskId 任务ID
     * @param staffId 导购ID
     * @return
     */
    public Integer getTheCountByPushTaskIdAndStaffId(Long pushTaskId, Long staffId){
        Integer taskUserRelationCount = groupPushTaskVipRelationMapper.selectCount(
                new LambdaQueryWrapper<GroupPushTaskVipRelation>()
                        .eq(GroupPushTaskVipRelation::getGroupPushTaskId, pushTaskId)
                        .eq(GroupPushTaskVipRelation::getStaffId, staffId)
        );
        return taskUserRelationCount;
    }

    /**
     * 根据企业微信ID修改任务与用户关联表将好友状态改为【未加好友】
     * @param taskId 推送任务ID
     * @param staffCpUserId 导购企业微信ID
     * @param vipCpUserId 用户企业微信ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserTaskRelationByCpUserId(Long taskId, String staffCpUserId, String vipCpUserId){
        groupPushTaskVipRelationMapper.updateTaskVipRelationFriendState(taskId, staffCpUserId, vipCpUserId, FriendStatusTypeEnum.NOT_FRIEND.value());
    }

    public List<Long> selectTaskIdByStaffIdAndVipCpUserId(Long staffId, String vipCpUserId){
        List<Long> taskIdListByStaffIdAndVipCpUserId = groupPushTaskVipRelationMapper.selectTaskIdByStaffIdAndVipCpUserId(staffId, vipCpUserId);
        return taskIdListByStaffIdAndVipCpUserId;
    }

    public List<UsableGroupPushVipVO> getUsableGroupPushVipUserList(Long sonTaskId, Long staffId){
        return groupPushTaskVipRelationMapper.selectUsableGroupPushVipUserList(sonTaskId, staffId);
    }

    public List<StaffPushTaskNotAddFriendCountVO> notAddFriendCountBySonTaskId(Long sonTaskId, List<Long> staffIdList){
        return groupPushTaskVipRelationMapper.notAddFriendCountBySonTaskId(sonTaskId, staffIdList);
    }

}
