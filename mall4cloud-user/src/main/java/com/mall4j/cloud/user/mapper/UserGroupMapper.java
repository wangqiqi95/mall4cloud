package com.mall4j.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.user.dto.UserGroupSelectDTO;
import com.mall4j.cloud.user.model.UserGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户分组阶段配置
 *
 * @author gmq
 * @date 2023-11-10 11:01:57
 */
public interface UserGroupMapper extends BaseMapper<UserGroup> {

	/**
	 * 获取用户分组阶段配置列表
	 * @return 用户分组阶段配置列表
	 */
	List<UserGroup> list(@Param("dto") UserGroupSelectDTO dto);

	/**
	 * 根据用户分组阶段配置id获取用户分组阶段配置
	 *
	 * @param id 用户分组阶段配置id
	 * @return 用户分组阶段配置
	 */
	UserGroup getById(@Param("id") Long id);

}
