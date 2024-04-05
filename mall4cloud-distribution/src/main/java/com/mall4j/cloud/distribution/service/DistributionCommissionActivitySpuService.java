package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.CommissionActivitySpuBatchUpdateDTO;
import com.mall4j.cloud.distribution.dto.CommissionActivitySpuSearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySpuDTO;
import com.mall4j.cloud.distribution.vo.DistributionCommissionActivitySpuVO;

import java.util.List;

/**
 * 佣金配置-活动佣金-商品
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionActivitySpuService {

	/**
	 * 分页查询活动佣金商品列表
	 * @param pageDTO 分页参数
	 * @param searchDTO 查询参数
	 * @return 佣金配置-活动佣金-商品列表分页数据
	 */
	PageVO<DistributionCommissionActivitySpuVO> page(PageDTO pageDTO, CommissionActivitySpuSearchDTO searchDTO);

	/**
	 * 保存活动商品配置
	 * @param distributionCommissionActivitySpuDTO 佣金配置-活动佣金-商品
	 */
	void save(DistributionCommissionActivitySpuDTO distributionCommissionActivitySpuDTO);

	/**
	 * 删除活动佣金商品
	 * @param activitySpuBatchUpdateDTO
	 */
	void delete(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO);

	/**
	 * 更新活动商品佣金
	 * @param activitySpuBatchUpdateDTO
	 */
	void updateCommissionRate(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO);

	/**
	 * 更新活动商品佣金状态
	 * @param activitySpuBatchUpdateDTO
	 */
	void updateCommissionRateStatus(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO);

	/**
	 * 更新活动商品活动时间
	 * @param activitySpuBatchUpdateDTO
	 */
	void updateActivityTime(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO);

	/**
	 * 通过活动id获取商品id集合
	 *
	 * @param activityId
	 * @return
	 */
	List<Long> findSpuIdListByActivityId(Long activityId);

}
