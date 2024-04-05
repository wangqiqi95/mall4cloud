package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.common.order.bo.RefundReductionStockBO;
import com.mall4j.cloud.product.bo.SkuWithStockBO;
import com.mall4j.cloud.product.dto.SpuExtensionStockDTO;
import com.mall4j.cloud.product.model.SpuExtension;
import com.mall4j.cloud.product.vo.SpuExtensionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author FrozenWatermelon
 * @date 2020-11-11 13:49:06
 */
public interface SpuExtensionMapper  {

	/**
	 * 获取列表
	 *
	 * @return 列表
	 */
	List<SpuExtensionVO> list();

	/**
	 * 根据id获取
	 *
	 * @param spuExtendId id
	 * @return
	 */
	SpuExtensionVO getBySpuExtendId(@Param("spuExtendId") Long spuExtendId);

	/**
	 * 保存
	 *
	 * @param spuExtension
	 */
	void save(@Param("spuExtension") SpuExtension spuExtension);

	/**
	 * 根据id删除
	 *
	 * @param spuId id
	 */
	void deleteById(@Param("spuId") Long spuId);

	/**
	 * 更新库存
	 *
	 * @param spuId 商品id
	 */
    void updateStock(@Param("spuId") Long spuId);

	/**
	 * 通过订单减少库存
	 *
	 * @param spuId 商品id
	 * @param count 数量
	 * @return
	 */
	int reduceStockByOrder(@Param("spuId") Long spuId, @Param("count") Integer count);

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

	/**
	 * 扣减视频号库存
	 * @param spuId
	 * @param count
	 */
	void reduceEcChannelsStockBySpuId(@Param("spuId") Long spuId,@Param("count") Integer count);

	/**
	 * 通过已经取消的订单减少实际库存
	 *
	 * @param skuWithStocks 库存信息
	 */
	void reduceActualStockByCancelOrder(@Param("skuWithStocks") List<SkuWithStockBO> skuWithStocks);

	void updateStocks(@Param("spuStocks") List<SpuExtensionStockDTO> spuStocks);

	/**
	 * 获取spu扩展信息
	 *
	 * @param spuId
	 * @return
	 */
    SpuExtension getBySpuId(Long spuId);

	/**
	 * 还原商品实际库存，减少销量
	 *
	 * @param refundReductionStocks
	 */
	void reductionActualStockByRefund(@Param("refundReductionStocks") List<RefundReductionStockBO> refundReductionStocks);

	/**
	 * 更新商品扩展信息
	 *
	 * @param spuExtension
	 */
    void update(@Param("spuExtension") SpuExtension spuExtension);

	/**
	 * 根据商品id更新商品注水销量
	 * @param spuId
	 * @param waterSoldNum
	 */
	void updateWaterSoldNumBySpuId(@Param("spuId") Long spuId, @Param("waterSoldNum") Integer waterSoldNum);

	/**
	 * 根据商品id修改商品评价数量
	 * @param spuId
	 */
    void changeCommentNum(@Param("spuId") Long spuId);

	/**
	 * 设置视频库存为零
	 * @param spuId spuId
	 */
	void zeroSetChannelsStock(@Param("spuId")Long spuId);

	/**
	 * 更新视频号库存
	 * @param spuId spuId
	 * @param countStock 更新的库存
	 */
	void updateChannelsStock(@Param("spuId") Long spuId, @Param("countStock") int countStock);
}
