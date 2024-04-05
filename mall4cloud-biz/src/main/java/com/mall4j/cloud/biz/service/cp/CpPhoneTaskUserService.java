package com.mall4j.cloud.biz.service.cp;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.dto.cp.CpSelectPhoneTaskUserDTO;
import com.mall4j.cloud.biz.model.cp.CpPhoneTaskUser;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserExportVO;
import com.mall4j.cloud.biz.vo.cp.CpPhoneTaskUserVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;

/**
 * 引流手机号任务关联客户
 *
 * @author gmq
 * @date 2023-10-30 17:13:43
 */
public interface CpPhoneTaskUserService extends IService<CpPhoneTaskUser> {

	/**
	 * 分页获取引流手机号任务关联客户列表
	 * @param pageDTO 分页参数
	 * @return 引流手机号任务关联客户列表分页数据
	 */
	PageVO<CpPhoneTaskUserVO> page(PageDTO pageDTO, CpSelectPhoneTaskUserDTO dto);

	PageVO<CpPhoneTaskUserVO> pageMobile(PageDTO pageDTO, CpSelectPhoneTaskUserDTO dto);

	List<CpPhoneTaskUserExportVO> exportList(CpSelectPhoneTaskUserDTO dto);

	/**
	 * 根据引流手机号任务关联客户id获取引流手机号任务关联客户
	 *
	 * @param id 引流手机号任务关联客户id
	 * @return 引流手机号任务关联客户
	 */
	CpPhoneTaskUser getById(Long id);

	/**
	 * 根据引流手机号任务关联客户id删除引流手机号任务关联客户
	 * @param id 引流手机号任务关联客户id
	 */
	void deleteById(Long id);

	/**
	 * 任务员工分配客户
	 * @param taskId
	 */
	void distributeTaskUser(Long taskId);
}
