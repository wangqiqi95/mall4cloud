package com.mall4j.cloud.seckill.mapper;

import com.mall4j.cloud.seckill.dto.SeckillSkuDTO;
import com.mall4j.cloud.seckill.model.SeckillSku;
import com.mall4j.cloud.seckill.vo.SeckillSkuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀活动sku
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public interface SeckillSkuMapper {

	/**
	 * 获取秒杀活动sku列表
	 *
	 * @return 秒杀活动sku列表
	 */
	List<SeckillSkuVO> list();

	/**
	 * 根据秒杀活动skuid获取秒杀活动sku
	 *
	 * @param seckillSkuId 秒杀活动skuid
	 * @return 秒杀活动sku
	 */
	SeckillSkuVO getBySeckillSkuId(@Param("seckillSkuId") Long seckillSkuId);

	/**
	 * 更新秒杀活动sku
	 *
	 * @param seckillSku 秒杀活动sku
	 */
	void update(@Param("seckillSku") SeckillSku seckillSku);

	/**
	 * 根据秒杀活动skuid删除秒杀活动sku
	 *
	 * @param seckillSkuId
	 */
	void deleteById(@Param("seckillSkuId") Long seckillSkuId);

	/**
	 * 根据秒杀活动id获取秒杀商品规格信息
	 *
	 * @param seckillId 秒杀活动id
	 * @return 秒杀商品规格信息
	 */
    List<SeckillSkuVO> selectListBySeckillId(@Param("seckillId") Long seckillId);

	/**
	 * 批量保存秒杀活动sku
	 *
	 * @param seckillSkuList 秒杀活动sku
	 */
	void saveBatch(@Param("seckillSkuList") List<SeckillSkuDTO> seckillSkuList);

	/**
	 * 更新商品秒杀库存
	 *
	 * @param seckillSkuId
	 * @param count
	 * @return
	 */
    int updateStocks(@Param("seckillSkuId") Long seckillSkuId, @Param("count") Integer count);

	/**
	 * 还原秒杀库存
	 * @param orderId 订单id
	 */
	void returnStocksByOrderId(@Param("orderId") Long orderId);
}
