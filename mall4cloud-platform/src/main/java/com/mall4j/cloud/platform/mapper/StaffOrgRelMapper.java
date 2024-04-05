package com.mall4j.cloud.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.api.platform.vo.StaffOrgVO;
import com.mall4j.cloud.platform.model.StaffOrgRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工部门关联表
 *
 * @author gmq
 * @date 2023-10-27 10:44:14
 */
public interface StaffOrgRelMapper extends BaseMapper<StaffOrgRel> {

	/**
	 * 获取员工部门信息
	 * @param staffIds
	 * @return
	 */
	List<StaffOrgVO> selectStaffAndOrgs(@Param("staffIds") List<Long> staffIds);

	/**
	 * 根据员工部门关联表id删除员工部门关联表
	 * @param staffId
	 */
	void deleteByStaffId(@Param("staffId") Long staffId);
}
