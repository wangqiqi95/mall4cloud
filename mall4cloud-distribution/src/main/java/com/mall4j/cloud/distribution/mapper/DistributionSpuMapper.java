package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionSpu;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销商品关联信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionSpuMapper {

	/**
	 * 获取分销商品关联信息列表
	 * @return 分销商品关联信息列表
	 */
	List<DistributionSpu> list();

	/**
	 * 根据分销商品关联信息id获取分销商品关联信息
	 *
	 * @param distributionSpuId 分销商品关联信息id
	 * @return 分销商品关联信息
	 */
	DistributionSpuVO getByDistributionSpuId(@Param("distributionSpuId") Long distributionSpuId);

	/**
	 * 保存分销商品关联信息
	 * @param distributionSpu 分销商品关联信息
	 */
	void save(@Param("distributionSpu") DistributionSpu distributionSpu);

	/**
	 * 更新分销商品关联信息
	 * @param distributionSpu 分销商品关联信息
	 */
	void update(@Param("distributionSpu") DistributionSpu distributionSpu);

	/**
	 * 根据分销商品关联信息id删除分销商品关联信息
	 * @param distributionSpuId
	 */
	void deleteById(@Param("distributionSpuId") Long distributionSpuId);

	/**
	 * 根据商品id获取分销信息
	 * @param spuId
	 * @return
	 */
    DistributionSpuVO getBySpuId(@Param("spuId") Long spuId);

	/**
	 * 根据主键id与店铺id删除分销信息
	 * @param distributionSpuId
	 * @param shopId
	 */
	void deleteByIdAndShopId(@Param("distributionSpuId") Long distributionSpuId, @Param("shopId") Long shopId);

	/**
	 * 更新分销商品状态
	 * @param distributionSpuId
	 * @param oldState
	 * @param newState
	 * @return
	 */
    int changeStateByDistributionId(@Param("distributionSpuId") Long distributionSpuId, @Param("oldState") Integer oldState, @Param("newState") Integer newState);

	/**
	 * 根据商品id与状态统计数量
	 * @param spuId
	 * @param state
	 * @return
	 */
	int countBySpuIdAndState(@Param("spuId") Long spuId, @Param("state") Integer state);
	/**
	 * 根据商品状态和商品id
	 * @param state 商品状态
	 * @param spuIds 商品id集合
	 * @return 分销商品
	 */
    List<DistributionSpu> getByStateAndSpuIds(@Param("state") Integer state, @Param("spuIds") List<Long> spuIds);
}
