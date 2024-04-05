package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.Tentacle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 触点信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface TentacleMapper {

	/**
	 * 获取触点信息列表
	 * @return 触点信息列表
	 */
	List<Tentacle> list();

	/**
	 * 根据触点信息id获取触点信息
	 *
	 * @param id 触点信息id
	 * @return 触点信息
	 */
	Tentacle getById(@Param("id") Long id);

	/**
	 * 保存触点信息
	 * @param tentacle 触点信息
	 */
	void save(@Param("tentacle") Tentacle tentacle);

	/**
	 * 更新触点信息
	 * @param tentacle 触点信息
	 */
	void update(@Param("tentacle") Tentacle tentacle);

	/**
	 * 根据触点信息id删除触点信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	Tentacle findByBusinessIdAndType(@Param("uid") Long uid, @Param("tentacleType") Integer tentacleType);
}
