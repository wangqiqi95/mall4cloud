package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionSubjectProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-专题门店
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionSubjectProductMapper {

	/**
	 * 获取分销推广-专题门店列表
	 * @return 分销推广-专题门店列表
	 */
	List<DistributionSubjectProduct> list();

	/**
	 * 根据分销推广-专题门店id获取分销推广-专题门店
	 *
	 * @param id 分销推广-专题门店id
	 * @return 分销推广-专题门店
	 */
	DistributionSubjectProduct getById(@Param("id") Long id);

	/**
	 * 保存分销推广-专题门店
	 * @param distributionSubjectProduct 分销推广-专题门店
	 */
	void save(@Param("distributionSubjectProduct") DistributionSubjectProduct distributionSubjectProduct);

	/**
	 * 更新分销推广-专题门店
	 * @param distributionSubjectProduct 分销推广-专题门店
	 */
	void update(@Param("distributionSubjectProduct") DistributionSubjectProduct distributionSubjectProduct);

	/**
	 * 根据分销推广-专题门店id删除分销推广-专题门店
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    List<DistributionSubjectProduct> listBySubjectId(@Param("subjectId") Long subjectId);

	void deleteBySubjectIdNotInProductIds(@Param("subjectId") Long subjectId, @Param("productIds") List<Long> productIds);

    void deleteBySubjectId(@Param("subjectId") Long subjectId);
}
