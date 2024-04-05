package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.Tentacle;

/**
 * 触点信息
 *
 * @author ZengFanChang
 * @date 2021-12-18 18:05:05
 */
public interface TentacleService {

	/**
	 * 分页获取触点信息列表
	 * @param pageDTO 分页参数
	 * @return 触点信息列表分页数据
	 */
	PageVO<Tentacle> page(PageDTO pageDTO);

	/**
	 * 根据触点信息id获取触点信息
	 *
	 * @param id 触点信息id
	 * @return 触点信息
	 */
	Tentacle getById(Long id);

	/**
	 * 保存触点信息
	 * @param tentacle 触点信息
	 */
	void save(Tentacle tentacle);

	/**
	 * 更新触点信息
	 * @param tentacle 触点信息
	 */
	void update(Tentacle tentacle);

	/**
	 * 根据触点信息id删除触点信息
	 * @param id 触点信息id
	 */
	void deleteById(Long id);

	Tentacle findOrCreate(Long uid, Integer tentacleType);
}
