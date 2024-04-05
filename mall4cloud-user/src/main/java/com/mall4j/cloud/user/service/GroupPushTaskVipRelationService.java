package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.dto.UpdateVipRelationFriendStateDTO;
import com.mall4j.cloud.user.bo.ExportStaffSendRecordBO;
import com.mall4j.cloud.user.model.GroupPushTaskVipRelation;
import com.mall4j.cloud.user.vo.GroupPushTaskVipRelationByFriendStateVO;

import java.util.List;

/**
 * <p>
 * 推送任务用户关联表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskVipRelationService extends IService<GroupPushTaskVipRelation> {

    ServerResponseEntity updateFriendState(UpdateVipRelationFriendStateDTO updateVipRelationFriendStateDTO);

    void asyncUpdateFriendState(UpdateVipRelationFriendStateDTO updateVipRelationFriendStateDTO);

    /**
     * 查询出 t_group_push_task_vip_relation 表中所有不是会员关系的数据
     * @return
     */
    List<GroupPushTaskVipRelationByFriendStateVO> getTaskVipRelationListByFriendState(List<Long> notFinishedGroupPushTaskList);

    void updateTaskVipRelationFriendStateByStaffIdAndUserId(Long pushTaskId, Long staffId, Long userId, Integer friendState, String vipCpUserId);

    void updateBatchTaskVipRelationFriendStateByStaffIdAndUserId(List<GroupPushTaskVipRelationByFriendStateVO> vipRelationList, Integer friendState);

    int updateBatchTaskVipRelationFriendStateByTaskIds(List<Long> pusTaskId);

}
