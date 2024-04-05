package com.mall4j.cloud.user.service;

import com.mall4j.cloud.api.user.dto.StartGroupPushDTO;
import com.mall4j.cloud.user.dto.QueryGroupPushSonTaskPageDetailDTO;
import com.mall4j.cloud.user.dto.TerminateGroupPushSonTaskDTO;
import com.mall4j.cloud.user.model.StaffBatchSendCpMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ben
 * @since 2023-03-18
 */
public interface StaffBatchSendCpMsgService extends IService<StaffBatchSendCpMsg> {


    /**
     * 根据被删除的子任务ID列表，查询所有相关的未发送的企微群发任务进行关闭操作
     * */
    void cancelExternalContactCpMsg(List<Long> sonTaskIdList);

    void editFailSend(StartGroupPushDTO startGroupPushDTO);

    List<StaffBatchSendCpMsg> getBySonTaskId(Long sonTaskId);


    /**
     * 终止字任务
     */
    void terminateSonTask(TerminateGroupPushSonTaskDTO detailDTO);
}
