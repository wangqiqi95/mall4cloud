package com.mall4j.cloud.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.platform.model.StaffOrgRel;

import java.util.List;

/**
 * 员工部门关联表
 *
 * @author gmq
 * @date 2023-10-27 10:44:14
 */
public interface StaffOrgRelService extends IService<StaffOrgRel> {


	/**
	 * 获取员工部门信息
	 * @param staffIds
	 * @return
	 */
	List<StaffOrgVO> getStaffAndOrgs(List<Long> staffIds);

	/**
	 * 根据员工部门关联表id删除员工部门关联表
	 * @param staffId 员工部门关联表id
	 */
	void deleteByStaffId(Long staffId);

	List<Long> getOrgAndChildByStaffIds(List<Long> staffIds);
}
