package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionPosterStore;

import java.util.List;

/**
 * 分销推广-海报门店
 *
 * @author ZengFanChang
 * @date 2022-01-03 20:00:28
 */
public interface DistributionPosterStoreService {

	/**
	 * 分页获取分销推广-海报门店列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-海报门店列表分页数据
	 */
	PageVO<DistributionPosterStore> page(PageDTO pageDTO);

	/**
	 * 根据分销推广-海报门店id获取分销推广-海报门店
	 *
	 * @param id 分销推广-海报门店id
	 * @return 分销推广-海报门店
	 */
	DistributionPosterStore getById(Long id);

	/**
	 * 保存分销推广-海报门店
	 * @param distributionPosterStore 分销推广-海报门店
	 */
	void save(DistributionPosterStore distributionPosterStore);

	/**
	 * 更新分销推广-海报门店
	 * @param distributionPosterStore 分销推广-海报门店
	 */
	void update(DistributionPosterStore distributionPosterStore);

	/**
	 * 根据分销推广-海报门店id删除分销推广-海报门店
	 * @param id 分销推广-海报门店id
	 */
	void deleteById(Long id);

	DistributionPosterStore getByPosterAndStore(Long posterId, Long storeId);

	List<DistributionPosterStore> listByPosterId(Long posterId);

	void deleteByPosterId(Long posterId);

	void deleteByPosterAndNotInStore(Long posterId, List<Long> storeIds);

    List<DistributionPosterStore> listByStoreId(Long storeId);

	List<DistributionPosterStore> listByPosterIdList(List<Long> posterIdList);
}
