package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.TaskSendDetailDTO;
import com.mall4j.cloud.biz.model.cp.TaskSendDetail;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailCountVO;
import com.mall4j.cloud.biz.vo.cp.TaskSendDetailVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 群发任务明细表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface TaskSendDetailService {

	/**
	 * 分页获取群发任务明细表列表
	 * @param pageDTO 分页参数
	 * @return 群发任务明细表列表分页数据
	 */
	PageVO<TaskSendDetailVO> page(PageDTO pageDTO, TaskSendDetailDTO request);

	/**
	 * 根据群发任务明细表id获取群发任务明细表
	 *
	 * @param id 群发任务明细表id
	 * @return 群发任务明细表
	 */
	TaskSendDetail getById(Long id);

	/**
	 * 保存群发任务明细表
	 * @param taskSendDetail 群发任务明细表
	 */
	void save(TaskSendDetail taskSendDetail);

	/**
	 * 更新群发任务明细表
	 * @param taskSendDetail 群发任务明细表
	 */
	void update(TaskSendDetail taskSendDetail);

	/**
	 * 根据群发任务明细表id删除群发任务明细表
	 * @param id 群发任务明细表id
	 */
	void deleteById(Long id);

	/**
	 * 根据推送id删除详情
	 * @param taskId
	 */
    void deleteByPushId(Long taskId);

	/**
	 * 提供给望伟的小程序端
	 * @param staffId 员工id
	 * @param userId 员工userId
	 * @param status 状态 0 未处理  1 已处理
	 * @return List<TaskAttachmentVO>
	 */
	List<TaskAttachmentVO> queryTaskList(Long staffId, String userId, Integer status);

	/**
	 * 提供给望伟的小程序端，点去执行进行闭环
	 * @param id 任务详情id
	 * @param type 闭环哪里任务 0 群活码 1 群发
	 */
	void completeTask(Long id,int type );

	/**
	 * 提供给望伟的小程序端，点去执行进行闭环
	 * @param id 任务详情id
	 * @param type 闭环哪里任务 0 群活码 1 群发
	 */
	TaskAttachmentVO getTaskDetail(Long id,int type );

	/**
	 * 根据 推送id 统计发送率
	 * @param pushId 推送id
	 * @return TaskSendDetailCountVO
	 */
	TaskSendDetailCountVO count(Long pushId);

	/**
	 * 获取待推送的消息任务
	 * @return
	 */
    List<TaskSendDetail> listSendTask();

	/**
	 * 推送消息
	 * @param detail
	 */
	void send(TaskSendDetail detail);
}
