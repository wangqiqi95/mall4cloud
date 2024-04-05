package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionMomentsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-朋友圈商品
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionMomentsProductMapper {

	/**
	 * 获取分销推广-朋友圈商品列表
	 * @return 分销推广-朋友圈商品列表
	 */
	List<DistributionMomentsProduct> list();

	/**
	 * 根据分销推广-朋友圈商品id获取分销推广-朋友圈商品
	 *
	 * @param id 分销推广-朋友圈商品id
	 * @return 分销推广-朋友圈商品
	 */
	DistributionMomentsProduct getById(@Param("id") Long id);

	/**
	 * 保存分销推广-朋友圈商品
	 * @param distributionMomentsProduct 分销推广-朋友圈商品
	 */
	void save(@Param("distributionMomentsProduct") DistributionMomentsProduct distributionMomentsProduct);

	/**
	 * 更新分销推广-朋友圈商品
	 * @param distributionMomentsProduct 分销推广-朋友圈商品
	 */
	void update(@Param("distributionMomentsProduct") DistributionMomentsProduct distributionMomentsProduct);

	/**
	 * 根据分销推广-朋友圈商品id删除分销推广-朋友圈商品
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    List<DistributionMomentsProduct> listByMomentsId(@Param("momentsId") Long momentsId);

	List<DistributionMomentsProduct> listByMomentsIdList(@Param("momentsIdList") List<Long> momentsIdList);

	void deleteByMomentsIdNotInProductIds(@Param("momentsId") Long momentsId, @Param("productIds") List<Long> productIds);

	void deleteByMomentsId(@Param("momentsId") Long momentsId);
}
