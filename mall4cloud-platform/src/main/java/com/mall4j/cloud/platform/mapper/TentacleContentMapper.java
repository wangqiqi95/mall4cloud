package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.TentacleContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 触点内容信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface TentacleContentMapper {

	/**
	 * 获取触点内容信息列表
	 * @return 触点内容信息列表
	 */
	List<TentacleContent> list();

	/**
	 * 根据触点内容信息id获取触点内容信息
	 *
	 * @param id 触点内容信息id
	 * @return 触点内容信息
	 */
	TentacleContent getById(@Param("id") Long id);

	/**
	 * 保存触点内容信息
	 * @param tentacleContent 触点内容信息
	 */
	void save(@Param("tentacleContent") TentacleContent tentacleContent);

	/**
	 * 更新触点内容信息
	 * @param tentacleContent 触点内容信息
	 */
	void update(@Param("tentacleContent") TentacleContent tentacleContent);

	/**
	 * 根据触点内容信息id删除触点内容信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);


    TentacleContent findByTentacleIdAndContentType(@Param("tentacleId") Long tentacleId, @Param("contentType") Integer contentType);

	TentacleContent findByTentacleNo(@Param("tentacleNo") String tentacleNo);
}
