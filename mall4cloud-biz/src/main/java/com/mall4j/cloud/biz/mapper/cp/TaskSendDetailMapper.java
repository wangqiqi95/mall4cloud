package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.TaskSendDetailDTO;
import com.mall4j.cloud.biz.model.cp.TaskSendDetail;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailCountVO;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发任务明细表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface TaskSendDetailMapper {

	/**
	 * 获取群发任务明细表列表
	 * @param request 查询条件
	 * @return 群发任务明细表列表
	 */
	List<TaskSendDetailVO> list(@Param("et")TaskSendDetailDTO request);

	/**
	 * 根据群发任务明细表id获取群发任务明细表
	 *
	 * @param id 群发任务明细表id
	 * @return 群发任务明细表
	 */
	TaskSendDetail getById(@Param("id") Long id);

	/**
	 * 保存群发任务明细表
	 * @param taskSendDetail 群发任务明细表
	 */
	void save(@Param("taskSendDetail") TaskSendDetail taskSendDetail);

	/**
	 * 更新群发任务明细表
	 * @param taskSendDetail 群发任务明细表
	 */
	void update(@Param("taskSendDetail") TaskSendDetail taskSendDetail);

	/**
	 * 根据群发任务明细表id删除群发任务明细表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据任务id删除详情
	 * @param pushId
	 */
    void deleteByPushId(@Param("pushId")Long pushId);
	/**
	 * 提供给望伟的小程序端
	 * @param staffId 员工id
	 * @param userId 员工userId
	 * @param status 状态 0 未处理  1 已处理
	 * @return List<TaskAttachmentVO>
	 */
    List<TaskAttachmentVO> queryTaskList(@Param("id")Long id,@Param("staffId") Long staffId, @Param("userId")String userId, @Param("status")Integer status);
	/**
	 * 根据 推送id 统计发送率
	 * @param pushId 推送id
	 * @return TaskSendDetailCountVO
	 */
    TaskSendDetailCountVO count(@Param("pushId")Long pushId);
	/**
	 * 获取待推送的消息任务
	 * @return
	 */
    List<TaskSendDetail> listSendTask();
}
