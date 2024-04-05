package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionSubjectStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionSubjectStoreMapper {

	/**
	 * 获取分销推广-专题门店列表
	 * @return 分销推广-专题门店列表
	 */
	List<DistributionSubjectStore> list();

	/**
	 * 根据分销推广-专题门店id获取分销推广-专题门店
	 *
	 * @param id 分销推广-专题门店id
	 * @return 分销推广-专题门店
	 */
	DistributionSubjectStore getById(@Param("id") Long id);

	/**
	 * 保存分销推广-专题门店
	 * @param distributionSubjectStore 分销推广-专题门店
	 */
	void save(@Param("distributionSubjectStore") DistributionSubjectStore distributionSubjectStore);

	/**
	 * 更新分销推广-专题门店
	 * @param distributionSubjectStore 分销推广-专题门店
	 */
	void update(@Param("distributionSubjectStore") DistributionSubjectStore distributionSubjectStore);

	/**
	 * 根据分销推广-专题门店id删除分销推广-专题门店
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<DistributionSubjectStore> listBySubjectId(@Param("subjectId") Long subjectId);

	void deleteBySubjectIdNotInStoreIds(@Param("subjectId") Long subjectId, @Param("storeIds") List<Long> storeIds);

    List<DistributionSubjectStore> listByStoreId(@Param("storeId") Long storeId);

	void deleteBySubjectId(@Param("subjectId") Long subjectId);

    int countBySpecialSubjectId(@Param("subjectId") Long subjectId);

    List<DistributionSubjectStore> listInStoreIds(@Param("storeIds") List<Long> storeIds);

}
