package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpPhoneTaskDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTask;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 引流手机号任务
 *
 * @author gmq
 * @date 2023-10-30 17:13:30
 */
public interface CpPhoneTaskService extends IService<CpPhoneTask> {

	/**
	 * 分页获取引流手机号任务列表
	 * @param pageDTO 分页参数
	 * @return 引流手机号任务列表分页数据
	 */
	PageVO<CpPhoneTask> page(PageDTO pageDTO, SelectCpPhoneTaskDTO dto);

	PageVO<CpPhoneTaskVO> pageMobile(PageDTO pageDTO, SelectCpPhoneTaskDTO dto);

	/**
	 * 根据引流手机号任务id获取引流手机号任务
	 *
	 * @param id 引流手机号任务id
	 * @return 引流手机号任务
	 */
	CpPhoneTask getById(Long id);

	void closeTask(Long id);

	/**
	 * 保存引流手机号任务
	 * @param phoneTaskDTO 引流手机号任务
	 */
	void createAndUpdate(CpPhoneTaskDTO phoneTaskDTO);

	/**
	 * 根据引流手机号任务id删除引流手机号任务
	 * @param id 引流手机号任务id
	 */
	void deleteById(Long id);

	void refeshFinishTaskStatus();
}
