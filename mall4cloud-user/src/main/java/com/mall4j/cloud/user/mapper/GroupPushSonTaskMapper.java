package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.vo.TaskSonItemVO;
import com.mall4j.cloud.user.vo.TaskSonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.user.model.GroupPushSonTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 群发任务子任务表 Mapper 接口
 * </p>
 *
 * @author ben
 * @since 2023-02-20
 */
public interface GroupPushSonTaskMapper extends BaseMapper<GroupPushSonTask> {

    /**
     * 导购端获取代办任务列表
     *
     * @param staffId 导购ID
     * @return 代办任务列表
     */
    List<TaskSonVO> taskList(@Param("staffId") Long staffId, @Param("taskMode") Integer taskMode);

    /**
     * 根据子任务ID获取子任务详情
     * @param sonTaskId 子任务ID
     * @return
     */
    TaskSonItemVO getSonTaskDetailBySonTaskId(@Param("sonTaskId") Long sonTaskId);

    /**
     * 查询需要删除的子任务
     * @return 查询需要删除的子任务列表
     */
    List<Long> getExpiredGroupPushTaskList();

    /**
     * 查询未结束的子任务
     * @return 查询未结束的子任务列表
     */
    List<Long> getNotFinishedGroupPushTaskList();

    /**
     *
     * @return
     */
    List<Long> getFinishedGroupPushTaskList();

    /**
     * 导购获取自己群发任务数量
     * @param staffId 导购ID
     * @return
     */
    Integer staffGetGroupPushTaskCount(@Param("staffId") Long staffId);

}
