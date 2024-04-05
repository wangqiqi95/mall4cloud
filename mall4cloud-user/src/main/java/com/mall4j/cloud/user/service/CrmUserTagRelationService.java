package com.mall4j.cloud.user.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.user.model.CrmUserTagRelation;

/**
 * 数云维护 用户标签关系表
 *
 * @author FrozenWatermelon
 * @date 2023-11-25 10:41:27
 */
public interface CrmUserTagRelationService {

	/**
	 * 分页获取数云维护 用户标签关系表列表
	 * @param pageDTO 分页参数
	 * @return 数云维护 用户标签关系表列表分页数据
	 */
	PageVO<CrmUserTagRelation> page(PageDTO pageDTO);

	/**
	 * 根据数云维护 用户标签关系表id获取数云维护 用户标签关系表
	 *
	 * @param id 数云维护 用户标签关系表id
	 * @return 数云维护 用户标签关系表
	 */
	CrmUserTagRelation getById(Long id);

	/**
	 * 保存数云维护 用户标签关系表
	 * @param crmUserTagRelation 数云维护 用户标签关系表
	 */
	void save(CrmUserTagRelation crmUserTagRelation);

	/**
	 * 更新数云维护 用户标签关系表
	 * @param crmUserTagRelation 数云维护 用户标签关系表
	 */
	void update(CrmUserTagRelation crmUserTagRelation);

	/**
	 * 根据数云维护 用户标签关系表id删除数云维护 用户标签关系表
	 * @param id 数云维护 用户标签关系表id
	 */
	void deleteById(Long id);
}
