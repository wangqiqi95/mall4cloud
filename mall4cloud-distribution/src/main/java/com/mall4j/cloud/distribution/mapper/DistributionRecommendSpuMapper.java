package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionRecommendSpuQueryDTO;
import com.mall4j.cloud.distribution.model.DistributionRecommendSpu;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 分销推广-推荐商品
 *
 * @author gww
 * @date 2021-12-24 16:01:22
 */
public interface DistributionRecommendSpuMapper {

	/**
	 * 通过商品id集合查询推荐商品列表
	 * @param spuIdList  商品id集合
	 * @return 分销推广-推荐商品列表
	 */
	List<DistributionRecommendSpu> listBySpuIdList(@Param("spuIdList") List<Long> spuIdList);


	/**
	 * 通过查询条件返回推荐商品id集合
	 * @param distributionRecommendSpuQueryDTO
	 * @return
	 */
	List<Long> listSpuIdListByParam(@Param("spuQueryDTO") DistributionRecommendSpuQueryDTO distributionRecommendSpuQueryDTO);

	/**
	 * 根据分销推广-推荐商品id获取分销推广-推荐商品
	 *
	 * @param id 分销推广-推荐商品id
	 * @return 分销推广-推荐商品
	 */
	DistributionRecommendSpu getById(@Param("id") Long id);

	/**
	 * 保存分销推广-推荐商品
	 * @param distributionRecommendSpu 分销推广-推荐商品
	 */
	void save(@Param("distributionRecommendSpu") DistributionRecommendSpu distributionRecommendSpu);

	/**
	 * 批量保存分销推广-推荐商品
	 * @param distributionRecommendSpuList
	 */
	void saveBatch(@Param("distributionRecommendSpuList") List<DistributionRecommendSpu> distributionRecommendSpuList);

	/**
	 * 更新分销推广-推荐商品
	 * @param distributionRecommendSpu 分销推广-推荐商品
	 */
	void update(@Param("distributionRecommendSpu") DistributionRecommendSpu distributionRecommendSpu);

	/**
	 * 批量更新分销推广-推荐商品
	 * @param idList
	 * @param status
	 */
	void updateStatus(@Param("idList") List<Long> idList, @Param("status") Integer status);

	/**
	 * 根据分销推广-推荐商品id删除分销推广-推荐商品
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<DistributionRecommendSpu> listSpuIdListByParamClash(@Param("beginTime") Date beginTime,
												 @Param("endTime") Date endTime,
												 @Param("commodityIds")List<Long> commodityIds,
												 @Param("storeIds")List<Long> storeIds);
}
