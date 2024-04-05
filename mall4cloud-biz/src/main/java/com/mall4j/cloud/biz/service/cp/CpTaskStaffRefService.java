package com.mall4j.cloud.biz.service.cp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.cp.CpTaskStaffRef;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 群发任务人工关联表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface CpTaskStaffRefService extends IService<CpTaskStaffRef> {

	/**
	 * 分页获取群发任务人工关联表列表
	 * @param pageDTO 分页参数
	 * @return 群发任务人工关联表列表分页数据
	 */
	PageVO<CpTaskStaffRef> page(PageDTO pageDTO);

	/**
	 * 根据群发任务人工关联表id获取群发任务人工关联表
	 *
	 * @param id 群发任务人工关联表id
	 * @return 群发任务人工关联表
	 */
	CpTaskStaffRef getById(Long id);

	/**
	 * 根据群发任务人工关联表id删除群发任务人工关联表
	 * @param id 群发任务人工关联表id
	 */
	void deleteById(Long id);

	/**
	 * 根据任务id删除员工关联关系
	 * @param taskId 任务id
	 * @param type  任务类型
	 */
    void deleteByTaskId(Long taskId, int type);

	/**
	 * 根据任务id去查询员工信息
	 * @param taskId 任务id
	 * @param type 类型
	 * @return
	 */
	List<CpTaskStaffRef> listByTaskId(Long taskId, Integer type);
}
