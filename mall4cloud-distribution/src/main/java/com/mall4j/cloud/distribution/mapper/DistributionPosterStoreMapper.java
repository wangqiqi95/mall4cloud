package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionPosterStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-海报门店
 *
 * @author ZengFanChang
 * @date 2022-01-03 20:00:28
 */
public interface DistributionPosterStoreMapper {

	/**
	 * 获取分销推广-海报门店列表
	 * @return 分销推广-海报门店列表
	 */
	List<DistributionPosterStore> list();

	/**
	 * 根据分销推广-海报门店id获取分销推广-海报门店
	 *
	 * @param id 分销推广-海报门店id
	 * @return 分销推广-海报门店
	 */
	DistributionPosterStore getById(@Param("id") Long id);

	/**
	 * 保存分销推广-海报门店
	 * @param distributionPosterStore 分销推广-海报门店
	 */
	void save(@Param("distributionPosterStore") DistributionPosterStore distributionPosterStore);

	/**
	 * 更新分销推广-海报门店
	 * @param distributionPosterStore 分销推广-海报门店
	 */
	void update(@Param("distributionPosterStore") DistributionPosterStore distributionPosterStore);

	/**
	 * 根据分销推广-海报门店id删除分销推广-海报门店
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	DistributionPosterStore getByPosterAndStore(@Param("posterId") Long posterId, @Param("storeId") Long storeId);

	List<DistributionPosterStore> listByPosterId(@Param("posterId") Long posterId);

	void deleteByPosterId(@Param("posterId") Long posterId);

	void deleteByPosterAndNotInStore(@Param("posterId") Long posterId, @Param("storeIds") List<Long> storeIds);

	List<DistributionPosterStore> listByStoreId(@Param("storeId") Long storeId);


	List<DistributionPosterStore> listByPosterIdList(@Param("posterIdList") List<Long> posterIdList);
}
