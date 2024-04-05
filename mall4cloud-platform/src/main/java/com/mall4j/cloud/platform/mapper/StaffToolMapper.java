package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.StaffTool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工企微工具信息
 *
 * @author ZengFanChang
 * @date 2022-02-12 00:13:55
 */
public interface StaffToolMapper {

	/**
	 * 获取员工企微工具信息列表
	 * @return 员工企微工具信息列表
	 */
	List<StaffTool> list();

	/**
	 * 根据员工企微工具信息id获取员工企微工具信息
	 *
	 * @param id 员工企微工具信息id
	 * @return 员工企微工具信息
	 */
	StaffTool getById(@Param("id") Long id);

	/**
	 * 保存员工企微工具信息
	 * @param staffTool 员工企微工具信息
	 */
	void save(@Param("staffTool") StaffTool staffTool);

	/**
	 * 更新员工企微工具信息
	 * @param staffTool 员工企微工具信息
	 */
	void update(@Param("staffTool") StaffTool staffTool);

	void updateByStaff(@Param("staffTool") StaffTool staffTool);

	/**
	 * 根据员工企微工具信息id删除员工企微工具信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	StaffTool getStaffToolByStaff(@Param("staffId") Long staffId);
}
