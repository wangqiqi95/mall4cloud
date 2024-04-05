package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.api.platform.dto.OfflineHandleEventDTO;
import com.mall4j.cloud.api.platform.vo.OfflineHandleEventVO;
import com.mall4j.cloud.distribution.dto.DistributionSpuDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.distribution.model.DistributionSpu;

import java.util.List;

/**
 * 分销商品关联信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionSpuService {

	/**
	 * 根据分销商品关联信息id获取分销商品关联信息
	 *
	 * @param distributionSpuId 分销商品关联信息id
	 * @return 分销商品关联信息
	 */
	DistributionSpuVO getByDistributionSpuId(Long distributionSpuId);

	/**
	 * 保存分销商品关联信息
	 * @param distributionSpuDTO 分销商品关联信息
	 */
	void save(DistributionSpuDTO distributionSpuDTO);

	/**
	 * 更新分销商品关联信息
	 * @param distributionSpuDTO 分销商品关联信息
	 */
	void update(DistributionSpuDTO distributionSpuDTO);

	/**
	 * 根据分销商品关联信息id删除分销商品关联信息
	 * @param distributionSpuId 分销商品关联信息id
	 */
	void deleteById(Long distributionSpuId);

	/**
	 * 根据主键id与店铺id删除分销信息
	 * @param distributionSpuId
	 * @param shopId
	 */
	void deleteByIdAndShopId(Long distributionSpuId, Long shopId);

	/**
	 * 下线分销商品
	 * @param offlineHandleEventDTO
	 */
    void offline(OfflineHandleEventDTO offlineHandleEventDTO);

	/**
	 * 违规分销商品提交重新上线申请
	 * @param offlineHandleEventDTO
	 */
	void auditApply(OfflineHandleEventDTO offlineHandleEventDTO);

	/**
	 * 平台审核分销商品
	 * @param offlineHandleEventDTO
	 */
	void audit(OfflineHandleEventDTO offlineHandleEventDTO);

	/**
	 * 根据商品id获取分销信息
	 * @param spuId
	 * @return
	 */
	DistributionSpuVO getBySpuId(Long spuId);

	/**
	 * 根据主键id列表批量删除
	 * @param distributionSpuIds
	 * @param shopId
	 */
	void batchDeleteByIdsAndShopId(List<Long> distributionSpuIds, Long shopId);

	/**
	 * 获取下线信息
	 * @param distributionSpuId
	 * @return
	 */
    OfflineHandleEventVO getOfflineHandleEvent(Long distributionSpuId);

	/**
	 * 更新分销商品状态
	 * @param distributionSpuId
	 * @param state
	 */
	void updateState(Long distributionSpuId, Integer state);

	/**
	 * 根据商品id与状态查看该分销商品是处于该状态
	 * @param spuId
	 * @param  state
	 * @return
	 */
    Boolean isStateBySpuIdAndState(Long spuId, Integer state);

	/**
	 * 根据商品状态和商品id
	 * @param state 商品状态
	 * @param spuIds 商品id集合
	 * @return 分销商品
	 */
    List<DistributionSpu> getByStateAndSpuIds(Integer state, List<Long> spuIds);
}
