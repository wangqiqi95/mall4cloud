package com.mall4j.cloud.biz.service.cp;

import com.mall4j.cloud.biz.dto.cp.TagDTO;
import com.mall4j.cloud.biz.model.cp.TagGroup;
import com.mall4j.cloud.biz.vo.cp.TagGroupVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

import java.lang.management.LockInfo;
import java.util.List;


/**
 * 标签组配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface TagGroupService {

	/**
	 * 分页获取标签组配置列表
	 * @return 标签组配置列表分页数据
	 */
	List<TagGroupVO> list(Long id);

	/**
	 * 根据标签组配置id获取标签组配置
	 *
	 * @param id 标签组配置id
	 * @return 标签组配置
	 */
	TagGroup getById(Long id);

	/**
	 * 保存标签组配置
	 * @param tagGroup 标签组配置
	 */
	void save(TagGroup tagGroup);

	/**
	 * 更新标签组配置
	 * @param tagGroup 标签组配置
	 */
	void update(TagGroup tagGroup);

	/**
	 * 根据标签组配置id删除标签组配置
	 * @param id 标签组配置id
	 */
	void deleteById(Long id);

	/**
	 * 添加标签和标签组
	 * @param tagGroup
	 * @param tags
	 */
	void addAndUpdate(TagGroup tagGroup, List<TagDTO> tags);

	/**
	 * 组排序
	 * @param id 当前组id
	 * @param newSort 新位置
	 * @param oldSort 旧位置
	 * @param front 向前 向后
	 */
    void sort(Long id, int newSort,int oldSort, int front);
}
