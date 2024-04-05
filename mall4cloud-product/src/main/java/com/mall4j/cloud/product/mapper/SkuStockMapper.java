package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.common.order.bo.RefundReductionStockBO;
import com.mall4j.cloud.product.bo.SkuWithStockBO;
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
public interface SkuStockMapper extends BaseMapper<SkuStock> {

	/**
	 * 保存库存信息
	 *
	 * @param skuStock 库存信息
	 */
	void saveSkuStock(@Param("skuStock") SkuStock skuStock);

	/**
	 * 更新库存信息
	 *
	 * @param skuStock 库存信息
	 */
	void updateSkuStock(@Param("skuStock") SkuStock skuStock);

	/**
	 * 根据库存信息id删除库存信息
	 *
	 * @param stockId
	 */
	void deleteById(@Param("stockId") Long stockId);

	/**
	 * 批量保存
	 *
	 * @param skuStocks 库存信息集合
	 */
	void batchSave(@Param("skuStocks") List<SkuStock> skuStocks);

	/**
	 * 根据skuIds删除库存
	 *
	 * @param skuIds ids
	 */
	void deleteBySkuIds(@Param("skuIds") List<Long> skuIds);

	/**
	 * 根据列表中的库存数量，增加sku的库存
	 *
	 * @param skuStocks 修改信息
	 * @return 更新的数量
	 */
	int updateStock(@Param("skuStocks") List<SkuStock> skuStocks);

	/**
	 * 通过sku集合获取库存信息
	 *
	 * @param skuVOList
	 * @return 库存信息
	 */
	List<SkuStockVO> listBySkuList(@Param("skuVOList") List<SkuVO> skuVOList);

	/**
	 * 通过订单减少库存
	 *
	 * @param skuId 商品id
	 * @param count 数量
	 * @return
	 */
    int reduceStockByOrder(@Param("skuId") Long skuId, @Param("count") Integer count);

	/**
	 * 通过订单添加库存
	 *
	 * @param skuWithStocks 库存信息
	 */
    void addStockByOrder(@Param("skuWithStocks") List<SkuWithStockBO> skuWithStocks);

	/**
	 * 通过订单减少实际库存
	 *
	 * @param skuWithStocks 库存信息
	 */
    void reduceActualStockByOrder(@Param("skuWithStocks") List<SkuWithStockBO> skuWithStocks);

	void reduceEcChannelsStockByskuId(@Param("skuId") Long skuId,@Param("count") Integer count);


	/**
	 * 通过已经取消的订单减少实际库存
	 *
	 * @param skuWithStocks 库存信息
	 */
	void reduceActualStockByCancelOrder(@Param("skuWithStocks") List<SkuWithStockBO> skuWithStocks);

	/**
	 * 还原sku实际库存
	 * @param refundReductionStocks
	 */
    void reductionActualStockByCancelOrder(@Param("refundReductionStocks") List<RefundReductionStockBO> refundReductionStocks);

	/**
	 * 根据商品id获取库存信息
	 *
	 * @param spuId
	 * @return
	 */
	List<SkuStockVO> listStockBySpuId(@Param("spuId") Long spuId);

	List<SpuStockVO> sumStockBySpuIds(@Param("spuIds") List<Long> spuIds);

	/**
	 * 更新视频号库存
	 * @param skuId
	 * @param stock
	 */
	void updateSkuChannelsStock(@Param("skuId") Long skuId, @Param("stock") Integer stock);
}
