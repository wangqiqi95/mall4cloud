package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.model.cp.Tag;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.util.List;


/**
 * 客户标签配置表
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface TagService {

	/**
	 * 分页获取客户标签配置表列表
	 * @return 客户标签配置表列表分页数据
	 */
	List<Tag> list();

	/**
	 * 根据客户标签配置表id获取客户标签配置表
	 *
	 * @param id 客户标签配置表id
	 * @return 客户标签配置表
	 */
	Tag getById(Long id);

	/**
	 * 保存客户标签配置表
	 * @param tag 客户标签配置表
	 */
	void save(Tag tag);

	/**
	 * 更新客户标签配置表
	 * @param tag 客户标签配置表
	 */
	void update(Tag tag);

	/**
	 * 根据客户标签配置表id删除客户标签配置表
	 * @param id 客户标签配置表id
	 */
	void deleteById(Long id);
}
