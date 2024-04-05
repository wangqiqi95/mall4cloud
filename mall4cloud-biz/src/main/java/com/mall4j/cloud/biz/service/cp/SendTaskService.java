package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.biz.dto.cp.MaterialMsgDTO;
import com.mall4j.cloud.biz.dto.cp.SendTaskPageDTO;
import com.mall4j.cloud.biz.dto.cp.TaskPushDTO;
import com.mall4j.cloud.biz.model.cp.SendTask;
import com.mall4j.cloud.biz.model.cp.TaskSendDetail;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 群发任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface SendTaskService {

	/**
	 * 分页获取群发任务表列表
	 * @param pageDTO 分页参数
	 * @param request 查询条件
	 * @return 群发任务表列表分页数据
	 */
	PageVO<SendTask> page(PageDTO pageDTO, SendTaskPageDTO request);

	/**
	 * 根据群发任务表id获取群发任务表
	 *
	 * @param id 群发任务表id
	 * @return 群发任务表
	 */
	SendTask getById(Long id);

	/**
	 * 保存群发任务表
	 * @param sendTask 群发任务表
	 */
	void save(SendTask sendTask);

	/**
	 * 更新群发任务表
	 * @param sendTask 群发任务表
	 */
	void update(SendTask sendTask);

	/**
	 * 根据群发任务表id删除群发任务表
	 * @param id 群发任务表id
	 */
	void deleteById(Long id);

	/**
	 * 创建群发任务
	 * @param sendTask 群发信息
	 * @param staffList 员工信息
	 * @param pushList 附件信息
	 * @param isChange 执行对象是否修改
	 */
    void createOrUpdate(SendTask sendTask, List<StaffVO> staffList, List<TaskPushDTO> pushList,  boolean  isChange);

	/**
	 * 更新状态
	 * @param id
	 */
    void updateSendTaskCompleteSend(Long id);

	void send(TaskSendDetail detail);
}
