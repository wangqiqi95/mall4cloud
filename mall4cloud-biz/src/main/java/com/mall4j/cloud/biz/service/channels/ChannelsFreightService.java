package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.biz.dto.channels.ChannelsFreightDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsFreightUpdateDTO;
import com.mall4j.cloud.biz.vo.channels.ChannelsFreightVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

public interface ChannelsFreightService {
	/**
	 * 查询运费模板列表
	 * @param pageDTO
	 * @param templateName
	 * @return
	 */
	PageVO<ChannelsFreightVO> list(PageDTO pageDTO, String templateName);
	
	/**
	 * 视频号新增运费模板
	 * @param channelsFreightDTO
	 */
	void save(ChannelsFreightDTO channelsFreightDTO);
	
	/**
	 * 视频号更新运费模板
	 * @param channelsFreightUpdateDTO
	 */
	void update(ChannelsFreightUpdateDTO channelsFreightUpdateDTO);
	
	/**
	 * 视频号删除运费模板
	 * @param transportId
	 */
	void delete(Long transportId);
}
