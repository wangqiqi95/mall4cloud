package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户标签配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface TagMapper {

	/**
	 * 获取客户标签配置表列表
	 * @return 客户标签配置表列表
	 */
	List<Tag> list();

	/**
	 * 根据客户标签配置表id获取客户标签配置表
	 *
	 * @param id 客户标签配置表id
	 * @return 客户标签配置表
	 */
	Tag getById(@Param("id") Long id);

	/**
	 * 保存客户标签配置表
	 * @param tag 客户标签配置表
	 */
	void save(@Param("tag") Tag tag);

	/**
	 * 更新客户标签配置表
	 * @param tag 客户标签配置表
	 */
	void update(@Param("tag") Tag tag);

	/**
	 * 根据客户标签配置表id删除客户标签配置表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
