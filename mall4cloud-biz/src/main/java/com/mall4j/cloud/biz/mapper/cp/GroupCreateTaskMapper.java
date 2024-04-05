package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.GroupCreateTaskPageDTO;
import com.mall4j.cloud.biz.model.cp.CpGroupCreateTask;
import com.mall4j.cloud.biz.vo.cp.GroupCreateTaskVO;
import com.mall4j.cloud.biz.vo.cp.TaskAttachmentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签建群任务表
 *
 * @author hwy
 * @date 2022-02-18 18:17:51
 */
public interface GroupCreateTaskMapper extends BaseMapper<CpGroupCreateTask> {

	/**
	 * 获取标签建群任务表列表
	 * @param request 查询条件 任务名称
	 * @return 标签建群任务表列表
	 */
	List<GroupCreateTaskVO> list(@Param("et") GroupCreateTaskPageDTO request);

	/**
	 * 根据标签建群任务表id获取标签建群任务表
	 *
	 * @param id 标签建群任务表id
	 * @return 标签建群任务表
	 */
	CpGroupCreateTask getById(@Param("id") Long id);

	/**
	 * 保存标签建群任务表
	 * @param groupCreateTask 标签建群任务表
	 */
	void save(@Param("groupCreateTask") CpGroupCreateTask groupCreateTask);

	/**
	 * 更新标签建群任务表
	 * @param groupCreateTask 标签建群任务表
	 */
	void update(@Param("groupCreateTask") CpGroupCreateTask groupCreateTask);

	/**
	 * 根据标签建群任务表id删除标签建群任务表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 提供给望伟的小程序端
	 * @param id 任务id
	 * @param staffId 员工id
	 * @param userId 员工userId
	 * @param status 状态 0 未处理  1 已处理
	 * @return List<TaskAttachmentVO>
	 */
    List<TaskAttachmentVO> queryTaskList(@Param("id")Long id,@Param("staffId") Long staffId, @Param("userId")String userId, @Param("status")Integer status);
}
