package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionSubjectStore;

import java.util.List;

/**
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionSubjectStoreService {

	/**
	 * 分页获取分销推广-专题门店列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-专题门店列表分页数据
	 */
	PageVO<DistributionSubjectStore> page(PageDTO pageDTO);

	/**
	 * 根据分销推广-专题门店id获取分销推广-专题门店
	 *
	 * @param id 分销推广-专题门店id
	 * @return 分销推广-专题门店
	 */
	DistributionSubjectStore getById(Long id);

	/**
	 * 保存分销推广-专题门店
	 * @param distributionSubjectStore 分销推广-专题门店
	 */
	void save(DistributionSubjectStore distributionSubjectStore);

	/**
	 * 更新分销推广-专题门店
	 * @param distributionSubjectStore 分销推广-专题门店
	 */
	void update(DistributionSubjectStore distributionSubjectStore);

	/**
	 * 根据分销推广-专题门店id删除分销推广-专题门店
	 * @param id 分销推广-专题门店id
	 */
	void deleteById(Long id);

	List<DistributionSubjectStore> listBySubjectId(Long subjectId);

	List<DistributionSubjectStore> listByStoreId(Long storeId);

	List<DistributionSubjectStore> listInStoreIds(List<Long> storeIds);

	void deleteBySubjectIdNotInStoreIds(Long subjectId, List<Long> storeIds);

	void deleteBySubjectId(Long subjectId);

	int countBySpecialSubjectId(Long subjectId);
}
