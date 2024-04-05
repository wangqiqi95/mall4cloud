package com.mall4j.cloud.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.api.user.vo.GroupPushTaskVipRelationVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.user.constant.StaffCpFriendStateEnum;
import com.mall4j.cloud.user.dto.UpdateVipRelationFriendStateDTO;
import com.mall4j.cloud.user.mapper.GroupPushTaskVipRelationMapper;
import com.mall4j.cloud.user.model.GroupPushTaskVipRelation;
import com.mall4j.cloud.user.service.GroupPushTaskVipRelationService;
import com.mall4j.cloud.user.vo.GroupPushTaskVipRelationByFriendStateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 推送任务用户关联表 服务实现类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
@Service
public class GroupPushTaskVipRelationServiceImpl extends ServiceImpl<GroupPushTaskVipRelationMapper, GroupPushTaskVipRelation> implements GroupPushTaskVipRelationService {

    @Autowired
    private GroupPushTaskVipRelationMapper groupPushTaskVipRelationMapper;

    /**
     * 查询出 t_group_push_task_vip_relation 表中所有会员关系的数据
     * @return
     */
    @Override
    public List<GroupPushTaskVipRelationByFriendStateVO> getTaskVipRelationListByFriendState(List<Long> notFinishedGroupPushTaskList) {
        return groupPushTaskVipRelationMapper.getTaskVipRelationListByFriendState(notFinishedGroupPushTaskList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTaskVipRelationFriendStateByStaffIdAndUserId(Long pushTaskId, Long staffId, Long userId, Integer friendState, String vipCpUserId) {
        groupPushTaskVipRelationMapper.updateTaskVipRelationFriendStateByStaffIdAndUserId(pushTaskId, staffId, userId, friendState, vipCpUserId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBatchTaskVipRelationFriendStateByStaffIdAndUserId(List<GroupPushTaskVipRelationByFriendStateVO> vipRelationList, Integer friendState) {
        groupPushTaskVipRelationMapper.updateBatchTaskVipRelationFriendStateByStaffIdAndUserId(vipRelationList, friendState);
    }

    @Override
    public int updateBatchTaskVipRelationFriendStateByTaskIds(List<Long> pushTaskIds) {
        return groupPushTaskVipRelationMapper.updateBatchTaskVipRelationFriendStateByTaskIds(pushTaskIds);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponseEntity updateFriendState(UpdateVipRelationFriendStateDTO updateVipRelationFriendStateDTO) {

//        UserInfoInTokenBO tokenUser = AuthUserContext.get();

        //查询导购和用户当前仍在有效执行期间的任务
        List<GroupPushTaskVipRelationVO> groupPushTaskVipRelationVOList = groupPushTaskVipRelationMapper
                .selectUnfailedPushSonTaskVipByStaffIdAndUserId(updateVipRelationFriendStateDTO.getStaffId(), updateVipRelationFriendStateDTO.getVipUserId());

        if (CollectionUtil.isNotEmpty(groupPushTaskVipRelationVOList)){
            List<Long> relationIdList = groupPushTaskVipRelationVOList.stream()
                    .map(GroupPushTaskVipRelationVO::getGroupPushTaskVipRelationId)
                    .collect(Collectors.toList());
            //更新记录中的好友关系
            lambdaUpdate()
                    .in(GroupPushTaskVipRelation::getGroupPushTaskVipRelationId, relationIdList)
                    .set(GroupPushTaskVipRelation::getFriendState, StaffCpFriendStateEnum.YES.getFriendState())
                    .set(GroupPushTaskVipRelation::getVipCpUserId, updateVipRelationFriendStateDTO.getVipCpUserId());
        }


        return ServerResponseEntity.success();
    }

    @Async
    @Override
    public void asyncUpdateFriendState(UpdateVipRelationFriendStateDTO updateVipRelationFriendStateDTO) {
        this.updateFriendState(updateVipRelationFriendStateDTO);
    }
}
