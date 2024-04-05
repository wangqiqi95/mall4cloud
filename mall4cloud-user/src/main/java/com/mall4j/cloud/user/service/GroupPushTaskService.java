package com.mall4j.cloud.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.user.dto.StartGroupPushDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.ExportGroupPushTaskStatisticsBO;
import com.mall4j.cloud.user.dto.*;
import com.mall4j.cloud.user.dto.openapi.SendTaskDTO;
import com.mall4j.cloud.user.model.GroupPushTask;
import com.mall4j.cloud.user.vo.*;
import com.mall4j.cloud.user.vo.openapi.ApiResponse;
import me.chanjar.weixin.cp.bean.external.msg.Attachment;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 群发任务组表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushTaskService extends IService<GroupPushTask> {

    ServerResponseEntity<Void> addTask(AddPushTaskDTO addPushTaskDTO);

    void wrappingGroupPushTask(GroupPushTask GroupPushTask);

    ServerResponseEntity createFail(GroupPushTask groupPushTask);

    ServerResponseEntity updateFail(EditPushTaskDTO editPushTaskDTO);

    ServerResponseEntity<PageVO<GroupPushTaskPageVO>> getPage(QueryGroupPushTaskPageDTO pageDTO);

    ServerResponseEntity editTask(AddPushTaskDTO editPushTaskDTO);

    ServerResponseEntity enableOrDisableTask(Long pushTaskId);

    ServerResponseEntity disableTaskTask(Long pushTaskId);

    void wrappingEditGroupPushTask(EditPushTaskDTO editPushTaskDTO, boolean changeTag);

    GroupPushTaskDetailVO getTheGroupPushTask(Long pushTaskId);

    ServerResponseEntity<List<GroupPushTaskStatisticVO>> getStatistic(GetGroupPushTaskStatisticDTO statisticDTO);

    ServerResponseEntity removeTask(Long pushTaskId);

    ServerResponseEntity downloadData(Long taskId, Long sonTaskId, HttpServletResponse response);

    ServerResponseEntity startGroupPush(StartGroupPushDTO startGroupPushDTO);

    //void startCpGroupPush(StartGroupPushDTO startGroupPushDTO,List<UsableGroupPushVipVO> vipList);

    List<Attachment> getAttachments(Long sonTaskId, String redisKey, Long staffId,boolean urlFlag);

    GroupPushTaskStatisticVO getSonTaskStatistic(Long pushTaskId, Long sonTaskId);

    ExportGroupPushTaskStatisticsBO getExportStatistic(Long pushTaskId, Long sonTaskId);

    ApiResponse crmSendTask(SendTaskDTO request);
    ApiResponse crmSendTaskNew(SendTaskDTO request);

    ServerResponseEntity<PageVO<GroupPushSonTaskDetailStatisticVO>> getSonTaskDetailStatistic(QueryGroupPushSonTaskPageDetailDTO params);

    List<SoldGroupPushSonTaskDetailStatisticVO> soldSonTaskDetailStatistic(QueryGroupPushSonTaskPageDetailDTO params);

    ServerResponseEntity<PageVO<GroupPushTaskDetailStatisticVO>> getTaskDetailStatistic(QueryGroupPushTaskPageDetailDTO params);

    List<GroupPushTaskRecordStatisticVO> getTaskRecordStatistic(QueryGroupPushRecordDTO params);

    Attachment getMaterialAttachment(Long materialId,Long staffId);
}
