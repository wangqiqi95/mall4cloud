package com.mall4j.cloud.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.dto.UserGroupSelectDTO;
import com.mall4j.cloud.user.model.UserGroup;

import java.util.List;

/**
 * 用户分组阶段配置
 *
 * @author gmq
 * @date 2023-11-10 11:01:57
 */
public interface UserGroupService extends IService<UserGroup> {

	/**
	 * 分页获取用户分组阶段配置列表
	 * @param pageDTO 分页参数
	 * @return 用户分组阶段配置列表分页数据
	 */
	PageVO<UserGroup> page(PageDTO pageDTO, UserGroupSelectDTO dto);

	List<UserGroup> getGroupList(String groupName);

	/**
	 * 根据用户分组阶段配置id获取用户分组阶段配置
	 *
	 * @param id 用户分组阶段配置id
	 * @return 用户分组阶段配置
	 */
	UserGroup getById(Long id);

	/**
	 * 保存用户分组阶段配置
	 * @param userGroup 用户分组阶段配置
	 */
	void saveUserGroup(List<UserGroup> userGroup);

	/**
	 * 更新用户分组阶段配置
	 * @param userGroup 用户分组阶段配置
	 */
	void updateUserGroup(UserGroup userGroup);

	/**
	 * 根据用户分组阶段配置id删除用户分组阶段配置
	 * @param id 用户分组阶段配置id
	 */
	void deleteById(Long id);
}
