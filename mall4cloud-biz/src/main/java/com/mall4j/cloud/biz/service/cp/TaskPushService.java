package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.MaterialMsgDTO;
import com.mall4j.cloud.biz.dto.cp.SendTaskPageDTO;
import com.mall4j.cloud.biz.dto.cp.TaskPushDTO;
import com.mall4j.cloud.biz.model.cp.SendTask;
import com.mall4j.cloud.biz.model.cp.TaskPush;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 推送任务任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface TaskPushService {

	/**
	 * 根据任务id获取推送任务
	 * @param taskId 任务id
	 * @return 推送任务任务表列表分页数据
	 */
	List<TaskPush> listPushTaskByTaskId(Long taskId);

	/**
	 * 根据推送任务任务表id获取推送任务任务表
	 * @param id 推送任务任务表id
	 * @return 推送任务任务表
	 */
	TaskPush getById(Long id);

	/**
	 * 保存推送任务任务表
	 * @param taskPush 推送任务任务表
	 */
	void save(TaskPush taskPush);

	/**
	 * 更新推送任务任务表
	 * @param taskPush 推送任务任务表
	 */
	void update(TaskPush taskPush);

	/**
	 * 根据推送任务任务表id删除推送任务任务表
	 * @param id 推送任务任务表id
	 */
	void deleteById(Long id);
	/**
	 * 根据任务表id删除推送任务表
	 * @param taskId 任务id
	 */
	void deleteByTaskId(Long taskId);

	/**
	 * 保存推送列表
	 * @param sendTask 任务
	 * @param staffList 员工列表
	 * @param pushList 推送列表
	 */
    void createBatchPush(SendTask sendTask, List<StaffVO> staffList, List<TaskPushDTO> pushList);
	/**
	 * 更新推送列表
	 * @param sendTask 任务
	 * @param staffList 员工列表
	 * @param pushList 推送列表
	 */
    void updateBatchPush(boolean isChange, SendTask sendTask, List<StaffVO> staffList, List<TaskPushDTO> pushList);

	/**
	 * 更新推送状态
	 * @param pushId
	 */
	void updateTaskPushCompleteSend(Long  pushId);
}
