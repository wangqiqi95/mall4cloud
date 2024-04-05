package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSpuBind;

/**
 * 用户商品绑定信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionSpuBindService {

	/**
	 * 分页获取用户商品绑定信息列表
	 * @param pageDTO 分页参数
	 * @return 用户商品绑定信息列表分页数据
	 */
	PageVO<DistributionSpuBind> page(PageDTO pageDTO);

	/**
	 * 根据用户商品绑定信息id获取用户商品绑定信息
	 *
	 * @param id 用户商品绑定信息id
	 * @return 用户商品绑定信息
	 */
	DistributionSpuBind getById(Long id);

	/**
	 * 保存用户商品绑定信息
	 * @param distributionSpuBind 用户商品绑定信息
	 */
	void save(DistributionSpuBind distributionSpuBind);

	/**
	 * 更新用户商品绑定信息
	 * @param distributionSpuBind 用户商品绑定信息
	 */
	void update(DistributionSpuBind distributionSpuBind);

	/**
	 * 根据用户商品绑定信息id删除用户商品绑定信息
	 * @param id 用户商品绑定信息id
	 */
	void deleteById(Long id);
}
