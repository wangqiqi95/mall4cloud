package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.dto.StaffToolDTO;
import com.mall4j.cloud.platform.model.StaffTool;

/**
 * 员工企微工具信息
 *
 * @author ZengFanChang
 * @date 2022-02-12 00:13:55
 */
public interface StaffToolService {

	/**
	 * 分页获取员工企微工具信息列表
	 * @param pageDTO 分页参数
	 * @return 员工企微工具信息列表分页数据
	 */
	PageVO<StaffTool> page(PageDTO pageDTO);

	/**
	 * 根据员工企微工具信息id获取员工企微工具信息
	 *
	 * @param id 员工企微工具信息id
	 * @return 员工企微工具信息
	 */
	StaffTool getById(Long id);

	/**
	 * 保存员工企微工具信息
	 * @param staffTool 员工企微工具信息
	 */
	void save(StaffToolDTO staffTool);

	/**
	 * 更新员工企微工具信息
	 * @param staffTool 员工企微工具信息
	 */
	void update(StaffTool staffTool);

	/**
	 * 根据员工企微工具信息id删除员工企微工具信息
	 * @param id 员工企微工具信息id
	 */
	void deleteById(Long id);

	StaffTool getStaffToolByStaff(Long staffId);
}
