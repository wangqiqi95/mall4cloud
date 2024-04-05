package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSubjectProduct;

import java.util.List;

/**
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionSubjectProductService {

	/**
	 * 分页获取分销推广-专题门店列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-专题门店列表分页数据
	 */
	PageVO<DistributionSubjectProduct> page(PageDTO pageDTO);

	/**
	 * 根据分销推广-专题门店id获取分销推广-专题门店
	 *
	 * @param id 分销推广-专题门店id
	 * @return 分销推广-专题门店
	 */
	DistributionSubjectProduct getById(Long id);

	/**
	 * 保存分销推广-专题门店
	 * @param distributionSubjectProduct 分销推广-专题门店
	 */
	void save(DistributionSubjectProduct distributionSubjectProduct);

	/**
	 * 更新分销推广-专题门店
	 * @param distributionSubjectProduct 分销推广-专题门店
	 */
	void update(DistributionSubjectProduct distributionSubjectProduct);

	/**
	 * 根据分销推广-专题门店id删除分销推广-专题门店
	 * @param id 分销推广-专题门店id
	 */
	void deleteById(Long id);

	List<DistributionSubjectProduct> listBySubjectId(Long subjectId);

	void deleteBySubjectIdNotInProductIds(Long subjectId, List<Long> productIds);

	void deleteBySubjectId(Long subjectId);
}
