package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.SpuCollection;
import com.mall4j.cloud.product.vo.SpuCollectionVO;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;

import java.util.List;

/**
 * 商品收藏信息
 *
 * @author FrozenWatermelon
 * @date 2020-11-21 14:43:16
 */
public interface SpuCollectionService {

	/**
	 * 分页获取商品收藏信息列表
	 * @param pageDTO 分页参数
	 * @return 商品收藏信息列表分页数据
	 */
	PageVO<SpuCollectionVO> page(PageDTO pageDTO);

	/**
	 * 根据商品收藏信息id获取商品收藏信息
	 *
	 * @param id 商品收藏信息id
	 * @return 商品收藏信息
	 */
	SpuCollectionVO getById(Long id);

	/**
	 * 保存商品收藏信息
	 * @param spuCollection 商品收藏信息
	 */
	void save(SpuCollection spuCollection);

	/**
	 * 更新商品收藏信息
	 * @param spuCollection 商品收藏信息
	 */
	void update(SpuCollection spuCollection);

	/**
	 * 根据商品收藏信息id删除商品收藏信息
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 根据spuId，
	 * @param spuId
	 * @return
	 */
	SpuCollectionVO getBySpuId(Long spuId);

	/**
	 * 获取用户的商品收藏数量
	 *
	 * @param spuId
	 * @param userId
	 * @return
	 */
    int userCollectionCount(Long spuId, Long userId);

	/**
	 * 分页返回收藏数据
	 *
	 * @param userId 用户id
	 * @param spuName 筛选名称
	 * @param pageDTO 分页信息
	 * @param prodType 收藏失效状态，2为失效，0为正常
	 * @return 收藏数据
	 */
	PageVO<SpuAppVO> getUserCollectionDtoPageByUserId(PageDTO pageDTO, Long userId, String spuName, Integer prodType,Long storeId);

	/**
	 * 删除商品收藏
	 * @param spuId
	 * @param userId
	 */
	void deleteBySpuIdAndUserId(Long spuId, Long userId);

	/**
	 * 商品批量收藏
	 * @param spuIdList
	 */
	void spuBatchCollection(List<Long> spuIdList);

	/**
	 * 批量删除商品收藏
	 * @param spuIds 商品ids
	 * @param userId 用户id
	 * @return 返回结果
	 */
	Boolean deleteBatchBySpuIdsAndUserId(List<Long> spuIds, Long userId);

	/**
	 * 根据商品id删除关联收藏信息
	 * @param spuId 商品id
	 */
	void deleteBySpuId(Long spuId);
}
