package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.user.model.UserTagStaff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导购标签关系信息
 *
 * @author ZengFanChang
 * @date 2022-02-13 22:25:55
 */
public interface UserTagStaffMapper {

	/**
	 * 获取导购标签关系信息列表
	 * @return 导购标签关系信息列表
	 */
	List<UserTagStaff> list();

	/**
	 * 根据导购标签关系信息id获取导购标签关系信息
	 *
	 * @param id 导购标签关系信息id
	 * @return 导购标签关系信息
	 */
	UserTagStaff getById(@Param("id") Long id);

	/**
	 * 保存导购标签关系信息
	 * @param userTagStaff 导购标签关系信息
	 */
	void save(@Param("userTagStaff") UserTagStaff userTagStaff);

	/**
	 * 更新导购标签关系信息
	 * @param userTagStaff 导购标签关系信息
	 */
	void update(@Param("userTagStaff") UserTagStaff userTagStaff);

	/**
	 * 根据导购标签关系信息id删除导购标签关系信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<UserTagStaff> listByStaff(@Param("staffId") Long staffId);

    UserTagStaff getStaffTagByStaffAndTag(@Param("staffId") Long staffId, @Param("tagId") Long tagId);
}
