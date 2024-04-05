package com.mall4j.cloud.biz.service.channels;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
public interface ChannelsSkuService extends IService<ChannelsSku> {

	/**
	 * 分页获取视频号4.0商品列表
	 * @param pageDTO 分页参数
	 * @return 视频号4.0商品列表分页数据
	 */
	PageVO<ChannelsSku> page(PageDTO pageDTO);

	/**
	 * 根据视频号4.0商品id获取视频号4.0商品
	 *
	 * @param id 视频号4.0商品id
	 * @return 视频号4.0商品
	 */
	ChannelsSku getById(Long id);



	/**
	 * 根据视频号4.0商品id删除视频号4.0商品
	 * @param id 视频号4.0商品id
	 */
	void deleteById(Long id);

	/**
	 * 根据outSkuId 获取
	 * @param outSkuId 视频号skuId
	 * @return bean
	 */
	ChannelsSku getByOutSkuId(Long outSkuId);

	/**
	 * 商品库存扣减
	 * @param skuId skuId
	 * @param stock stock
	 */
	void reduceChannelsStockBySkuId(Long skuId, Integer stock);
}
