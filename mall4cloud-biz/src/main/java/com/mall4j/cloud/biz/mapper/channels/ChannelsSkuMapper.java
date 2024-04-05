package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.vo.channels.excel.ChannelsSpuSkuExcelVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
public interface ChannelsSkuMapper extends BaseMapper<ChannelsSku> {

	/**
	 * 获取视频号4.0商品列表
	 * @return 视频号4.0商品列表
	 */
	List<ChannelsSku> list();

	/**
	 * 根据视频号4.0商品id获取视频号4.0商品
	 *
	 * @param id 视频号4.0商品id
	 * @return 视频号4.0商品
	 */
	ChannelsSku getById(@Param("id") Long id);


	/**
	 * 根据视频号4.0商品id删除视频号4.0商品
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 更新库存
	 * @param channelsSkuId
	 * @param stock
	 */
	int updateStockBySkuId(@Param("channelsSkuId") Long channelsSkuId, @Param("stock") Integer stock);

	/**
	 * 更新库存
	 * @param skuId
	 * @param stock
	 */
	int updateStockByProductSkuId(@Param("skuId") Long skuId, @Param("stock") Integer stock);

	/**
	 * 设置库存为0
	 * @param channelsSkuId
	 * @return
	 */
	int setZeroStockBySkuId(@Param("channelsSkuId") Long channelsSkuId);

	List<ChannelsSpuSkuExcelVO> selectChannelsSpuSkuExcelVO(@Param("channelsSpuIds") List<Long> channelsSpuIds, @Param("lastSql") String lastSql);


	/**
	 * 库存扣减
	 */
	void reduceChannelsStockBySkuId(@Param("skuId") Long skuId, @Param("stock") Integer stock);
}
