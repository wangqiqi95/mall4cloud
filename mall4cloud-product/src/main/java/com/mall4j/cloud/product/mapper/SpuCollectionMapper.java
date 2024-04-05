package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.model.SpuCollection;
import com.mall4j.cloud.product.vo.SpuCollectionVO;
import com.mall4j.cloud.common.product.vo.app.SpuAppVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品收藏信息
 *
 * @author FrozenWatermelon
 * @date 2020-11-21 14:43:16
 */
public interface SpuCollectionMapper {

	/**
	 * 获取商品收藏信息列表
	 *
	 * @return 商品收藏信息列表
	 */
	List<SpuCollectionVO> list();

	/**
	 * 根据商品收藏信息id获取商品收藏信息
	 *
	 * @param id 商品收藏信息id
	 * @return 商品收藏信息
	 */
	SpuCollectionVO getById(@Param("id") Long id);

	/**
	 * 保存商品收藏信息
	 *
	 * @param spuCollection 商品收藏信息
	 */
	void save(@Param("spuCollection") SpuCollection spuCollection);

	/**
	 * 更新商品收藏信息
	 *
	 * @param spuCollection 商品收藏信息
	 */
	void update(@Param("spuCollection") SpuCollection spuCollection);

	/**
	 * 根据商品收藏信息id删除商品收藏信息
	 *
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 获取用户的商品收藏数量
	 *
	 * @param spuId
	 * @param userId
	 * @return
	 */
    int userCollectionCount(@Param("spuId") Long spuId, @Param("userId") Long userId);

	/**
	 * 分页返回收藏数据
	 *
	 * @param userId 用户id
	 * @param spuName 筛选名称
	 * @param lang 语言
	 * @param prodType 收藏失效状态，2为失效，0为正常
     * @return 收藏数据
	 */
	List<SpuAppVO> getUserCollectionDtoPageByUserId(@Param("userId") Long userId, @Param("spuName") String spuName,
													@Param("lang") Integer lang, @Param("prodType") Integer prodType);


	/**
	 * 删除商品收藏
	 *
	 * @param spuId
	 * @param spuIds
	 * @param userId
	 */
	void deleteUserCollection(@Param("spuId") Long spuId, @Param("spuIds") List<Long> spuIds, @Param("userId") Long userId);

	/**
	 * 获取商品列表中已收藏的spuId
	 *
	 * @param spuIdList
	 * @param userId
	 * @return
	 */
    List<Long> hasCollection(@Param("spuIdList") List<Long> spuIdList, @Param("userId") Long userId);

	/**
	 * 批量删除商品收藏
	 * @param spuIdList
	 * @param userId
	 */
	void saveBatch(@Param("spuIdList") List<Long> spuIdList, @Param("userId") Long userId);

	/**
	 * 根据商品id删除关联收藏信息
	 * @param spuId 商品id
	 */
	void deleteBySpuId(@Param("spuId") Long spuId);
}
