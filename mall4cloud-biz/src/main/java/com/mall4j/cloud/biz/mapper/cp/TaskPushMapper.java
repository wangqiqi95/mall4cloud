package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.SendTaskPageDTO;
import com.mall4j.cloud.biz.model.cp.SendTask;
import com.mall4j.cloud.biz.model.cp.TaskPush;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 推送任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface TaskPushMapper {

	/**
	 * 获取推送任务表列表
	 * @param taskId 任务id
	 * @return 推送任务表列表
	 */
	List<TaskPush> list(@Param("taskId") Long taskId);

	/**
	 * 根据推送任务表id获取推送任务表
	 *
	 * @param id 推送任务表id
	 * @return 推送任务表
	 */
	TaskPush getById(@Param("id") Long id);

	/**
	 * 保存推送任务表
	 * @param taskPush 推送任务表
	 */
	void save(@Param("et") TaskPush taskPush);

	/**
	 * 更新推送任务表
	 * @param taskPush 推送任务表
	 */
	void update(@Param("et") TaskPush taskPush);

	/**
	 * 根据推送任务表id删除推送任务表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据任务表id删除推送任务表
	 * @param taskId 任务id
	 */
	void deleteByTaskId(@Param("taskId") Long taskId);

    void updateTaskPushCompleteSend(@Param("pushId")Long pushId);
}
