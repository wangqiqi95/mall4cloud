package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.dto.cp.SendTaskPageDTO;
import com.mall4j.cloud.biz.model.cp.SendTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface SendTaskMapper {

	/**
	 * 获取群发任务表列表
	 * @param request 查询条件
	 * @return 群发任务表列表
	 */
	List<SendTask> list(@Param("et") SendTaskPageDTO request);

	/**
	 * 根据群发任务表id获取群发任务表
	 *
	 * @param id 群发任务表id
	 * @return 群发任务表
	 */
	SendTask getById(@Param("id") Long id);

	/**
	 * 保存群发任务表
	 * @param sendTask 群发任务表
	 */
	void save(@Param("sendTask") SendTask sendTask);

	/**
	 * 更新群发任务表
	 * @param sendTask 群发任务表
	 */
	void update(@Param("sendTask") SendTask sendTask);

	/**
	 * 根据群发任务表id删除群发任务表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    void updateSendTaskCompleteSend(@Param("id")Long id);
}
