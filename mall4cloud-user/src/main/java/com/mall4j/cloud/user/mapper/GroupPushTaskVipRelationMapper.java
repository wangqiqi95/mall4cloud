package com.mall4j.cloud.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.user.vo.GroupPushTaskVipRelationVO;
import com.mall4j.cloud.user.bo.AddGroupPushTaskVipRelationBO;
import com.mall4j.cloud.user.model.GroupPushTaskVipRelation;
import com.mall4j.cloud.user.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 推送任务用户关联表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskVipRelationMapper extends BaseMapper<GroupPushTaskVipRelation> {

    void insertBatch(@Param("relationList")List<AddGroupPushTaskVipRelationBO> relationBOList);

    List<StaffPushTaskIssueCountVO> selectIssueCount(@Param("pushTaskId") Long pushTaskId, @Param("staffIdList") List<Long> staffIdList);

    Integer selectTheAddFriendCountByPushTaskId(@Param("pushTaskId") Long pushTaskId);

    List<StaffPushTaskAddFriendCountVO> selectGroupByAddFriendCountByPushTaskId(@Param("pushTaskId") Long pushTaskId,
                                                                                @Param("staffIdList")List<Long> staffIdList);

    /**
     * 修改任务与用户关联表将好友状态改为【未加好友】
     * @param taskId 任务ID
     * @param staffId 导购ID
     * @param userId 用户ID
     */
    void updateStaffAndUserTaskRelation(@Param("taskId")Long taskId, @Param("staffId")Long staffId, @Param("userId")Long userId);

    /**
     * 查询出 t_group_push_task_vip_relation 表中所有会员关系的数据
     * @return
     */
    List<GroupPushTaskVipRelationByFriendStateVO> getTaskVipRelationListByFriendState(@Param("notFinishedGroupPushTaskList")List<Long> notFinishedGroupPushTaskList);

    /**
     * 根据企业微信ID修改任务与用户关联表将好友状态改为【已加好友】
     * @param taskId 推送任务ID
     * @param staffCpUserId 导购企业微信ID
     * @param vipCpUserId 用户企业微信ID
     */
    void updateTaskVipRelationFriendState(@Param("taskId")Long taskId, @Param("staffCpUserId")String staffCpUserId, @Param("vipCpUserId")String vipCpUserId, @Param("friendState")Integer friendState);

    void updateTaskVipRelationFriendStateByStaffIdAndUserId(@Param("taskId")Long taskId, @Param("staffId")Long staffId, @Param("userId")Long userId, @Param("friendState")Integer friendState, @Param("vipCpUserId")String vipCpUserId);

    void updateBatchTaskVipRelationFriendStateByStaffIdAndUserId(@Param("vipRelationList")List<GroupPushTaskVipRelationByFriendStateVO> vipRelationList, @Param("friendState")Integer friendState);

    int updateBatchTaskVipRelationFriendStateByTaskIds(@Param("pushTaskIds")List<Long> pushTaskIds);

    List<GroupPushTaskVipRelationVO> selectUnfailedPushSonTaskVipByStaffIdAndUserId(@Param("staffId") Long staffId, @Param("vipUserId") Long vipUserId);

    List<Long> selectTaskIdByStaffIdAndVipCpUserId(@Param("staffId") Long staffId, @Param("vipCpUserId") String vipCpUserId);


    List<UsableGroupPushVipVO> selectUsableGroupPushVipUserList(@Param("sonTaskId") Long sonTaskId, @Param("staffId") Long staffId);

    List<StaffPushTaskNotAddFriendCountVO> notAddFriendCountBySonTaskId(@Param("sonTaskId") Long sonTaskId, @Param("staffIdList") List<Long> staffIdList);

}
