package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.channels.ChannelsCategoryDTO;
import com.mall4j.cloud.biz.model.channels.ChannelsCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频号4.0 类目
 *
 * @author FrozenWatermelon
 * @date 2023-02-15 16:01:16
 */
public interface ChannelsCategoryMapper extends BaseMapper<ChannelsCategory>{

	/**
	 * 获取视频号4.0 类目列表
	 * @return 视频号4.0 类目列表
	 */
	List<ChannelsCategory> list(@Param("categoryDTO") ChannelsCategoryDTO categoryDTO);

	/**
	 * 根据视频号4.0 类目id获取视频号4.0 类目
	 *
	 * @param id 视频号4.0 类目id
	 * @return 视频号4.0 类目
	 */
	ChannelsCategory getById(@Param("id") Long id);

	/**
	 * 根据视频号4.0 类目id删除视频号4.0 类目
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
