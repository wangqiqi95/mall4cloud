package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionSpuLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销商品浏览记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionSpuLogMapper {

	/**
	 * 获取分销商品浏览记录信息列表
	 * @return 分销商品浏览记录信息列表
	 */
	List<DistributionSpuLog> list();

	/**
	 * 根据分销商品浏览记录信息id获取分销商品浏览记录信息
	 *
	 * @param distributionSpuLogId 分销商品浏览记录信息id
	 * @return 分销商品浏览记录信息
	 */
	DistributionSpuLog getByDistributionSpuLogId(@Param("distributionSpuLogId") Long distributionSpuLogId);

	/**
	 * 保存分销商品浏览记录信息
	 * @param distributionSpuLog 分销商品浏览记录信息
	 */
	void save(@Param("distributionSpuLog") DistributionSpuLog distributionSpuLog);

	/**
	 * 更新分销商品浏览记录信息
	 * @param distributionSpuLog 分销商品浏览记录信息
	 */
	void update(@Param("distributionSpuLog") DistributionSpuLog distributionSpuLog);

	/**
	 * 根据分销商品浏览记录信息id删除分销商品浏览记录信息
	 * @param distributionSpuLogId
	 */
	void deleteById(@Param("distributionSpuLogId") Long distributionSpuLogId);
}
