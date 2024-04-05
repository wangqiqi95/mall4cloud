package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpPhoneTaskStaffDTO;
import com.mall4j.cloud.biz.dto.cp.SelectCpPhoneTaskRelDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskStaff;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskStaffVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 引流手机号任务关联员工
 *
 * @author gmq
 * @date 2023-10-30 17:13:37
 */
public interface CpPhoneTaskStaffService extends IService<CpPhoneTaskStaff> {

	/**
	 * 分页获取引流手机号任务关联员工列表
	 * @param pageDTO 分页参数
	 * @return 引流手机号任务关联员工列表分页数据
	 */
	PageVO<CpPhoneTaskStaffVO> page(PageDTO pageDTO, SelectCpPhoneTaskRelDTO dto);

	/**
	 * 根据引流手机号任务关联员工id获取引流手机号任务关联员工
	 *
	 * @param id 引流手机号任务关联员工id
	 * @return 引流手机号任务关联员工
	 */
	CpPhoneTaskStaff getById(Long id);

	/**
	 * 根据引流手机号任务关联员工id删除引流手机号任务关联员工
	 * @param taskId 引流手机号任务关联员工id
	 */
	void deleteByTaskId(Long taskId);

	void saveStaffs(Long taskId,List<CpPhoneTaskStaffDTO> staffs);
}
