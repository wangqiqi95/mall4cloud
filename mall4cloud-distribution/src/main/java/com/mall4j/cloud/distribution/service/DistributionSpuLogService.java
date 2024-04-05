package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSpuLog;

/**
 * 分销商品浏览记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionSpuLogService {

	/**
	 * 分页获取分销商品浏览记录信息列表
	 * @param pageDTO 分页参数
	 * @return 分销商品浏览记录信息列表分页数据
	 */
	PageVO<DistributionSpuLog> page(PageDTO pageDTO);

	/**
	 * 根据分销商品浏览记录信息id获取分销商品浏览记录信息
	 *
	 * @param distributionSpuLogId 分销商品浏览记录信息id
	 * @return 分销商品浏览记录信息
	 */
	DistributionSpuLog getByDistributionSpuLogId(Long distributionSpuLogId);

	/**
	 * 保存分销商品浏览记录信息
	 * @param distributionSpuLog 分销商品浏览记录信息
	 */
	void save(DistributionSpuLog distributionSpuLog);

	/**
	 * 更新分销商品浏览记录信息
	 * @param distributionSpuLog 分销商品浏览记录信息
	 */
	void update(DistributionSpuLog distributionSpuLog);

	/**
	 * 根据分销商品浏览记录信息id删除分销商品浏览记录信息
	 * @param distributionSpuLogId 分销商品浏览记录信息id
	 */
	void deleteById(Long distributionSpuLogId);
}
