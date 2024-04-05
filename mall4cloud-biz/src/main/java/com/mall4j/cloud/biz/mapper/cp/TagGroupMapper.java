package com.mall4j.cloud.biz.mapper.cp;

import com.mall4j.cloud.biz.model.cp.TagGroup;
import com.mall4j.cloud.biz.vo.cp.TagGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 标签组配置
 *
 * @author hwy
 * @date 2022-01-24 11:05:43
 */
public interface TagGroupMapper {

	/**
	 * 获取标签组配置列表
	 * @return 标签组配置列表
	 */
	List<TagGroupVO> list(@Param("id") Long id);

	/**
	 * 根据标签组配置id获取标签组配置
	 *
	 * @param id 标签组配置id
	 * @return 标签组配置
	 */
	TagGroup getById(@Param("id") Long id);

	/**
	 * 保存标签组配置
	 * @param tagGroup 标签组配置
	 */
	void save(@Param("tagGroup") TagGroup tagGroup);

	/**
	 * 更新标签组配置
	 * @param tagGroup 标签组配置
	 */
	void update(@Param("tagGroup") TagGroup tagGroup);

	/**
	 * 根据标签组配置id删除标签组配置
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
	/**
	 * 根据标签组配置id删除标签组配置
	 * @param id 组id
	 * @param  oldSort 旧位置
	 * @param newSort 新位置
	 * @param front 向前 向后
	 */
	void sort(@Param("id") Long id,@Param("newSort") int newSort,@Param("oldSort") int oldSort,@Param("front") int front);
}
