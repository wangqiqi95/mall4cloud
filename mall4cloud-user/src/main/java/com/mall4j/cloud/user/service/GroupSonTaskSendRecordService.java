package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.bo.ExportStaffSendRecordBO;
import com.mall4j.cloud.user.dto.QueryGroupPushRecordDTO;
import com.mall4j.cloud.user.dto.QuerySendRecordPageDTO;
import com.mall4j.cloud.user.model.GroupSonTaskSendRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.user.vo.GroupPushTaskRecordStatisticVO;
import com.mall4j.cloud.user.vo.StaffSendRecordVO;


import java.util.List;

/**
 * <p>
 * 推送完成记录表 服务类
 * </p>
 *
 * @author ben
 * @since 2023-03-01
 */
public interface GroupSonTaskSendRecordService extends IService<GroupSonTaskSendRecord> {


    ServerResponseEntity<PageVO<StaffSendRecordVO>> getTheSendRecordBySonTaskAndStaff(QuerySendRecordPageDTO pageDTO);


    List<ExportStaffSendRecordBO> getDataByTaskAndSonTask(Long pushTaskId, Long sonTaskId);

    /**
     * 判断一下当前执行的任务是否执行过一键群发
     * @param staffId 导购ID
     * @param sonTaskId 子任务ID
     * @return
     */
    Integer getGroupPushStatusCountByStaffIdAndSonTaskId(Long staffId, Long sonTaskId);

    List<GroupPushTaskRecordStatisticVO> getTaskRecordStatistic(QueryGroupPushRecordDTO params);

}
