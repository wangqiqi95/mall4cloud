package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.model.cp.CpStaffCodeLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
public interface ChannelsSpuMapper extends BaseMapper<ChannelsSpu> {

	/**
	 * 获取视频号4.0商品列表
	 * @return 视频号4.0商品列表
	 */
	List<ChannelsSpu> list();

	/**
	 * 根据视频号4.0商品id获取视频号4.0商品
	 *
	 * @param id 视频号4.0商品id
	 * @return 视频号4.0商品
	 */
	ChannelsSpu getById(@Param("id") Long id);

	/**
	 * 保存视频号4.0商品
	 * @param channelsSpu 视频号4.0商品
	 */
	void save1(@Param("channelsSpu") ChannelsSpu channelsSpu);

	/**
	 * 更新视频号4.0商品
	 * @param channelsSpu 视频号4.0商品
	 */
	void update1(@Param("channelsSpu") ChannelsSpu channelsSpu);

	/**
	 * 根据视频号4.0商品id删除视频号4.0商品
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
