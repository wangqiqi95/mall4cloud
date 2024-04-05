package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.GroupCreateTaskDetailDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskDTO;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskPageDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTask;
import com.mall4j.cloud.biz.vo.cp.GroupCreateTaskVO;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.List;

/**
 * 标签建群任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
public interface GroupCreateTaskService extends IService<CpGroupCreateTask> {

	/**
	 * 分页获取标签建群任务表列表
	 * @param pageDTO 分页参数
	 * @param request 查询条件
	 * @return 标签建群任务表列表分页数据
	 */
	PageVO<GroupCreateTaskVO> page(PageDTO pageDTO, GroupCreateTaskPageDTO request);

	PageVO<GroupCreateTaskVO> mobilePage(PageDTO pageDTO, GroupCreateTaskPageDTO request);

	/**
	 * 根据标签建群任务表id获取标签建群任务表
	 *
	 * @param id 标签建群任务表id
	 * @return 标签建群任务表
	 */
	CpGroupCreateTask getById(Long id);

	GroupCreateTaskDetailDTO getDetailById(Long id);

	/**
	 * 提醒员工
	 * @param id
	 */
	void warnStaff(Long id);

	/**
	 * 根据标签建群任务表id删除标签建群任务表
	 * @param id 标签建群任务表id
	 */
	void deleteById(Long id);

	/**
	 * 新增标签群活码任务
	 */
    void createOrUpdateTask(GroupCreateTaskDTO groupCreateTaskDTO);


	/**
	 * 提供给望伟的小程序端
	 * @param id 任务id
	 * @param staffId 员工id
	 * @param userId 员工userId
	 * @param status 状态 0 未处理  1 已处理
	 * @return List<TaskAttachmentVO>
	 */
	List<TaskAttachmentVO> queryTaskList(Long  id,Long staffId, String userId,Integer status);

	/**
	 * 小程序去执行进行闭环
	 * @param id 群活码任务id
	 */
	void complete(Long id);
}
