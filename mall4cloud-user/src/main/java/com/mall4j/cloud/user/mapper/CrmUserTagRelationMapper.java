package com.mall4j.cloud.user.mapper;

import com.mall4j.cloud.api.user.dto.CRMUserTagDto;
import com.mall4j.cloud.user.model.CrmUserTagRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数云维护 用户标签关系表
 *
 * @author FrozenWatermelon
 * @date 2023-11-25 10:41:27
 */
public interface CrmUserTagRelationMapper {

	/**
	 * 获取数云维护 用户标签关系表列表
	 * @return 数云维护 用户标签关系表列表
	 */
	List<CrmUserTagRelation> list();

	List<CrmUserTagRelation> userTagList(@Param("originId") String originId,@Param("unionId") String unionId);

	List<CrmUserTagRelation> userTagListByDTO(@Param("dto") CRMUserTagDto dto);

	List<String> listUnionIdByTagId(@Param("tagIds") List<String> tagIds);

	/**
	 * 根据数云维护 用户标签关系表id获取数云维护 用户标签关系表
	 *
	 * @param id 数云维护 用户标签关系表id
	 * @return 数云维护 用户标签关系表
	 */
	CrmUserTagRelation getById(@Param("id") Long id);

	/**
	 * 保存数云维护 用户标签关系表
	 * @param crmUserTagRelation 数云维护 用户标签关系表
	 */
	void save(@Param("crmUserTagRelation") CrmUserTagRelation crmUserTagRelation);

	/**
	 * 更新数云维护 用户标签关系表
	 * @param crmUserTagRelation 数云维护 用户标签关系表
	 */
	void update(@Param("crmUserTagRelation") CrmUserTagRelation crmUserTagRelation);

	/**
	 * 根据数云维护 用户标签关系表id删除数云维护 用户标签关系表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
