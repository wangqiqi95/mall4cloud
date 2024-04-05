package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.biz.vo.TaskSonGroupVO;
import com.mall4j.cloud.api.biz.vo.WaitMatterCountVO;
import com.mall4j.cloud.api.user.dto.StaffSaveSonTaskSendRecordDTO;
import com.mall4j.cloud.api.user.vo.GroupPushTaskVipRelationVO;
import com.mall4j.cloud.user.dto.QueryChuDaRenQunDetailDTO;
import com.mall4j.cloud.user.model.GroupPushTask;
import com.mall4j.cloud.user.model.StaffBatchSendCpMsg;
import com.mall4j.cloud.user.vo.TaskSonItemVO;
import com.mall4j.cloud.user.vo.TaskSonUserVO;
import com.mall4j.cloud.user.vo.TaskSonVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.GroupPushSonTask;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 群发任务子任务表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushSonTaskService extends IService<GroupPushSonTask> {

    /**
     * 导购端获取代办任务列表
     * @param staffId 导购id
     * @param pageDTO 分页
     * @return 代办任务列表
     */
    ServerResponseEntity<PageVO<TaskSonVO>> getSonTaskPage(Long staffId, Integer taskMode, PageDTO pageDTO);

    /**
     * 根据子任务ID获取子任务详情
     * @param staffId 导购id
     * @param sonTaskId 群发子任务ID
     * @return 代办任务列表
     */
    ServerResponseEntity<TaskSonItemVO> getSonTaskDetailBySonTaskId(Long staffId, Long sonTaskId);

    ServerResponseEntity<List<com.mall4j.cloud.user.vo.GroupPushSonTaskVO>> getTheSonTaskByPushTaskId(Long pushTaskId);

    /**
     * 新增推送完成记录表数据
     * @param staffSaveSonTaskSendRecordDTO 新增推送完成记录表数据参数
     * @return
     */
    ServerResponseEntity staffSaveSonTaskSendRecord(@RequestBody StaffSaveSonTaskSendRecordDTO staffSaveSonTaskSendRecordDTO);

    /**
     * 根据任务ID和导购ID获取任务相关信息
     * @param taskId 主任务ID
     * @param staffId 导购ID
     * @return
     */
    ServerResponseEntity<List<GroupPushTaskVipRelationVO>> getTheVipListByTaskIdAndStaffId(Long taskId, Long staffId);

    /**
     * 修改任务与用户关联表将好友状态改为【未加好友】
     * @param taskId 任务ID
     * @param staffId 导购ID
     * @param userId 用户ID
     * @return
     */
    ServerResponseEntity updateStaffAndUserTaskRelation(Long taskId, Long staffId, Long userId);

    /**
     * 获取企业微信群发任务结果进行同步
     * @return
     */
    ServerResponseEntity syncGroupMessageSendResult();

    /**
     * 导购获取自己群发任务数量
     * @param staffId 导购ID
     * @return
     */
    ServerResponseEntity<Integer> staffGetGroupPushTaskCount(Long staffId);

    ServerResponseEntity<PageVO<TaskSonUserVO>> getSonTaskUserDetailBySonTaskId(Long staffId, QueryChuDaRenQunDetailDTO params, PageDTO pageDTO);

    ServerResponseEntity<PageVO<TaskSonGroupVO>> getSonTaskGroupDetailBySonTaskId(Long staffId, Long sonTaskId, String searchKey, PageDTO pageDTO);

    void pushTask(StaffBatchSendCpMsg staffBatchSendCpMsg, GroupPushSonTask sonTask, GroupPushTask mainTask);

    WaitMatterCountVO getStaffSendTaskCount();


}
