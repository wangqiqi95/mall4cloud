package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mall4j.cloud.user.bo.GroupSonTaskSendRecordBO;
import com.mall4j.cloud.user.bo.UserTagOperationBO;
import com.mall4j.cloud.user.bo.AddGroupSonTaskSendRecordBO;
import com.mall4j.cloud.user.dto.QueryGroupPushRecordDTO;
import com.mall4j.cloud.user.dto.QuerySendRecordPageDTO;
import com.mall4j.cloud.user.model.GroupSonTaskSendRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.vo.GroupPushTaskRecordStatisticVO;
import com.mall4j.cloud.user.vo.StaffSendRecordVO;
import com.mall4j.cloud.user.vo.StaffSonTaskSendCountVO;
import com.mall4j.cloud.user.vo.StaffSonTaskSendDataVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 推送完成记录表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-03-01
 */
public interface GroupSonTaskSendRecordMapper extends BaseMapper<GroupSonTaskSendRecord> {

    /**
     * 查询发送成功的个数
     */
    List<StaffSonTaskSendCountVO> selectSendCountBySonTaskIdAndStaffList(@Param("sonTaskId") Long sonTaskId, @Param("staffIdList") List<Long> staffIdList);

    // 导购根据子任务ID和用户ID列表，获取其中推送成功的用户ID列表
    List<Long> selectSendUserIdsBySonTaskIdAndUserList(@Param("staffId") Long staffId, @Param("sonTaskId") Long sonTaskId, @Param("userIdList") List<Long> userIdList);

    // 根据导购ID获取该导购下任务完成情况信息
    List<StaffSonTaskSendDataVO> selectSendCountByStaff(@Param("staffId") Long staffId);

    IPage<StaffSendRecordVO> selectSendRecordBySonTaskIdAndStaffId(@Param("page") IPage<StaffSendRecordVO> page,
                                                                   @Param("param")QuerySendRecordPageDTO param);
    // 批量新增群发任务推送成功数据
    void insertBatch(@Param("recordList")List<AddGroupSonTaskSendRecordBO> recordList);

    /**
     * 根据群发子任务ID和导购ID修改 t_group_son_task_send_record 表中完成状态，改为已完成
     * @param sonTaskId 群发子任务ID
     * @param staffId 导购ID
     * @param sendModel 发送类型
     */
    void updateSendStatus(@Param("sonTaskId")Long sonTaskId, @Param("staffId")Long staffId
            , @Param("sendModel")Integer sendModel, @Param("sendStatus")Integer sendStatus
            , @Param("userId")Long userId, @Param("vipCpUserId")String vipCpUserId);

    List<GroupPushTaskRecordStatisticVO> getTaskRecordStatistic(@Param("params") QueryGroupPushRecordDTO params);
}
