package com.mall4j.cloud.biz.mapper.cp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTask;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 引流手机号任务
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
public interface CpPhoneTaskMapper extends BaseMapper<CpPhoneTask> {

	/**
	 * 获取引流手机号任务列表
	 * @return 引流手机号任务列表
	 */
	List<CpPhoneTask> list(@Param("dto")SelectCpPhoneTaskDTO dto);

	List<CpPhoneTask> mobileList(@Param("dto")SelectCpPhoneTaskDTO dto);

	List<CpPhoneTask> selectDistributeTask(@Param("taskId") Long taskId);

	List<CpPhoneTask> selectFinishList(@Param("endTime") Date endTime);

	/**
	 * 根据引流手机号任务id获取引流手机号任务
	 *
	 * @param id 引流手机号任务id
	 * @return 引流手机号任务
	 */
	CpPhoneTask getById(@Param("id") Long id);

	/**
	 * 根据引流手机号任务id删除引流手机号任务
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
