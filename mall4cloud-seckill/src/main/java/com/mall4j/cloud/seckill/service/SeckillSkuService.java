package com.mall4j.cloud.seckill.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.seckill.dto.SeckillSkuDTO;
import com.mall4j.cloud.seckill.vo.SeckillSkuVO;

import java.util.List;

/**
 * 秒杀活动sku
 *
 * @author lhd
 * @date 2021-03-30 14:53:09
 */
public interface SeckillSkuService {

	/**
	 * 分页获取秒杀活动sku列表
	 * @param pageDTO 分页参数
	 * @return 秒杀活动sku列表分页数据
	 */
	PageVO<SeckillSkuVO> page(PageDTO pageDTO);

	/**
	 * 根据秒杀活动skuid获取秒杀活动sku
	 *
	 * @param seckillSkuId 秒杀活动skuid
	 * @return 秒杀活动sku
	 */
	SeckillSkuVO getBySeckillSkuId(Long seckillSkuId);

	/**
	 * 根据秒杀活动id获取秒杀商品规格信息
	 * @param seckillId 秒杀活动id
	 * @return 秒杀商品规格信息
	 */
    List<SeckillSkuVO> listSeckillSkuBySeckillId(Long seckillId);

	/**
	 * 批量保存秒杀活动sku
     * @param seckillSkuList 秒杀活动sku
     */
	void saveBatch(List<SeckillSkuDTO> seckillSkuList);

	/**
	 * 清除秒杀sku信息
	 * @param seckillId 秒杀id
	 */
	void removeSeckillSkuCacheBySeckillId(Long seckillId);

}
