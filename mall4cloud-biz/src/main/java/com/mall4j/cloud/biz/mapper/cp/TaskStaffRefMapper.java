package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.cp.CpTaskStaffRef;
import com.mall4j.cloud.biz.vo.cp.CpTaskStaffRefVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群发任务人工关联表
 *
 * @author hwy
 * @date 2022-02-18 18:17:52
 */
public interface TaskStaffRefMapper extends BaseMapper<CpTaskStaffRef> {

	/**
	 * 获取群发任务人工关联表列表
	 * @return 群发任务人工关联表列表
	 */
	List<CpTaskStaffRef> list();

	/**
	 * 根据群发任务人工关联表id获取群发任务人工关联表
	 *
	 * @param id 群发任务人工关联表id
	 * @return 群发任务人工关联表
	 */
	CpTaskStaffRef getById(@Param("id") Long id);

	/**
	 * 根据群发任务人工关联表id删除群发任务人工关联表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据任务id删除员工关联关系
	 * @param taskId 任务id
	 * @param type  任务类型
	 */
    void deleteByTaskId(@Param("taskId")Long taskId, @Param("type")int type);
	/**
	 * 根据任务id去查询员工信息
	 * @param taskId 任务id
	 * @param type 类型
	 * @return
	 */
	List<CpTaskStaffRef> listByTaskId(@Param("taskId")Long taskId, @Param("type") Integer type);

	/**
	 * 定时任务刷新状态数据
	 * @return
	 */
	List<CpTaskStaffRefVO> selectTaskRefeshList();

	/**
	 * 移动端首页待办事项统计
	 * @param staffId
	 * @return
	 */
	Integer waitMatterCountByStaffId(@Param("staffId") Long staffId);
}
