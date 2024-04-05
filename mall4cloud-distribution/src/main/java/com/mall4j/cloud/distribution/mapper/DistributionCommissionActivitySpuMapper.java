package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.CommissionActivitySpuSearchDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivitySpu;
import com.mall4j.cloud.distribution.vo.DistributionCommissionActivityCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金配置-活动佣金-商品
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionActivitySpuMapper {

	/**
	 * 获取佣金配置-活动佣金-商品列表
	 * @param searchDTO 查询参数
	 * @return 佣金配置-活动佣金-商品列表
	 */
	List<DistributionCommissionActivitySpu> list(@Param("searchDTO") CommissionActivitySpuSearchDTO searchDTO);

	/**
	 * 通过商品集合查询活动佣金-商品列表
	 * @param spuIdList
	 * @return
	 */
	List<DistributionCommissionActivitySpu> listBySpuIdList(@Param("spuIdList") List<Long> spuIdList);

	/**
	 * 获取佣金配置-活动佣金-商品列表
	 * @return 佣金配置-活动佣金-商品列表
	 */
	List<DistributionCommissionActivitySpu> listByActivityId(@Param("activityId") Long activityId);

	/**
	 * 根据佣金配置-活动佣金-商品id获取佣金配置-活动佣金-商品
	 *
	 * @param id 佣金配置-活动佣金-商品id
	 * @return 佣金配置-活动佣金-商品
	 */
	DistributionCommissionActivitySpu getById(@Param("id") Long id);

	/**
	 * 保存佣金配置-活动佣金-商品
	 * @param distributionCommissionActivitySpu 佣金配置-活动佣金-商品
	 */
	void save(@Param("distributionCommissionActivitySpu") DistributionCommissionActivitySpu distributionCommissionActivitySpu);

	/**
	 * 批量保存 佣金配置-活动佣金-商品
	 * @param distributionCommissionActivitySpuList
	 */
	void saveBatch(@Param("distributionCommissionActivitySpuList") List<DistributionCommissionActivitySpu> distributionCommissionActivitySpuList);

	/**
	 * 更新佣金配置-活动佣金-商品
	 * @param distributionCommissionActivitySpu 佣金配置-活动佣金-商品
	 */
	void update(@Param("distributionCommissionActivitySpu") DistributionCommissionActivitySpu distributionCommissionActivitySpu);

	/**
	 * 根据ID删除活动佣金-商品
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据ID集合删除活动佣金-商品
	 * @param idList
	 */
	void deleteByIdList(@Param("idList") List<Long> idList);

	/**
	 * 批量更新 佣金配置-活动佣金-商品
	 *
	 * @param distributionCommissionActivitySpuList
	 */
	void updateBatch(@Param("distributionCommissionActivitySpuList") List<DistributionCommissionActivitySpu> distributionCommissionActivitySpuList);

	/**
	 * 分组统计活动下商品数量
	 *
	 * @return
	 */
	List<DistributionCommissionActivityCountVO> countByActivityIdList(@Param("activityIdList") List<Long> activityIdList);

}
