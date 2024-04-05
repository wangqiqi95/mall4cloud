package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.common.order.bo.RefundReductionStockBO;
import com.mall4j.cloud.common.product.dto.SkuDTO;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.model.SkuStock;
import com.mall4j.cloud.product.vo.SkuStockVO;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.product.vo.SpuStockVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface SkuStockService extends IService<SkuStock> {

	/**
	 * 保存库存信息
	 * @param skuStock 库存信息
	 */
	void saveSkuStock(SkuStock skuStock);

	/**
	 * 更新库存信息
	 * @param skuStock 库存信息
	 */
	void updateSkuStock(SkuStock skuStock);

	/**
	 * 根据库存信息id删除库存信息
	 * @param stockId
	 */
	void deleteById(Long stockId);

	/**
	 * 批量保存库存信息
	 * @param skuStocks 库存信息
	 */
	void batchSave(List<SkuStock> skuStocks);

	/**
	 * 根据skuIds删除库存信息
	 * @param skuIds ids
	 */
	void deleteBySkuIds(List<Long> skuIds);

	/**
	 * 根据sku列表获取库存信息
	 * @param skuVOList sku列表
	 * @return 库存信息
	 */
	List<SkuStockVO> listBySkuList(List<SkuVO> skuVOList);

	/**
	 * 批量更新sku库存信息
	 * @param skuList
	 */
	void updateBatch(List<SkuDTO> skuList);

	/**
	 * 因为退款所以需要还原库存
	 * @param refundReductionStocks
	 */
    void reductionStock(List<RefundReductionStockBO> refundReductionStocks);

	/**
	 * 根据商品id获取库存信息
	 *
	 * @param spuId
	 * @return
	 */
	List<SkuStockVO> listStockBySpuId(@Param("spuId") Long spuId);

    void resetStock(List<Sku> skuSaveList);

	List<SpuStockVO> sumStockBySpuIds(List<Long> spuIds);

	void updateSkuChannelsStock(Long skuId, Integer stock);
}
