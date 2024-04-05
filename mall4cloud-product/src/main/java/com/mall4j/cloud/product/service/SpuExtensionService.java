package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.SpuExtensionStockDTO;
import com.mall4j.cloud.product.model.SpuExtension;
import com.mall4j.cloud.product.vo.SpuExtensionVO;

import java.util.List;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2020-11-11 13:49:06
 */
public interface SpuExtensionService{

	/**
	 * 分页获取列表
	 * @param pageDTO 分页参数
	 * @return 列表分页数据
	 */
	PageVO<SpuExtensionVO> page(PageDTO pageDTO);

	/**
	 * 根据id获取
	 *
	 * @param spuExtendId id
	 * @return
	 */
	SpuExtensionVO getBySpuExtendId(Long spuExtendId);

	/**
	 * 保存
	 * @param spuExtension 商品扩展信息
	 */
	void save(SpuExtension spuExtension);

	/**
	 * 更新库存
	 * @param spuId 商品id
	 */
	void updateStock(Long spuId);

	/**
	 * 根据id删除
	 * @param spuId
	 */
	void deleteById(Long spuId);

	/**
	 * 获取spu扩展信息
	 * @param spuId
	 * @return
	 */
    SpuExtension getBySpuId(Long spuId);

	/**
	 * 更新商品扩展信息
	 * @param spuExtension
	 */
	void update(SpuExtension spuExtension);

	/**
	 * 根据商品id更新商品注水销量
	 * @param spuId
	 * @param waterSoldNum
	 */
    void updateWaterSoldNumBySpuId(Long spuId, Integer waterSoldNum);

	/**
	 * 根据商品id修改商品评价数量
	 * @param spuId
	 */
	void changeCommentNum(Long spuId);

	/**
	 * 批量修改总库存
	 * @param spuStocks
	 */
	void updateStocks(List<SpuExtensionStockDTO> spuStocks);

	void zeroSetChannelsStock(Long spuId);

	/**
	 * 更新spu视频号总库存
	 * @param spuId
	 * @param countStock
	 */
	void updateChannelsStock(Long spuId, int countStock);
}
