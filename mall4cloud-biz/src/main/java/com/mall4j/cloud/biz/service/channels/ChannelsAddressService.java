package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.biz.dto.channels.ChannelsAddressDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsAddressPageDTO;
import com.mall4j.cloud.api.biz.vo.ChannelsAddressVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;

public interface ChannelsAddressService {
	/**
	 * 查询视频号4.0退货地址列表
	 * @param pageDTO
	 * @param channelsAddressPageDTO
	 * @return
	 */
	PageVO<ChannelsAddressVO> page(PageDTO pageDTO, ChannelsAddressPageDTO channelsAddressPageDTO);
	
	/**
	 * 新增视频号4.0退货地址
	 * @param channelsAddressDTO
	 */
	void save(ChannelsAddressDTO channelsAddressDTO);
	
	/**
	 * 更新视频号4.0退货地址
	 * @param channelsAddressDTO
	 */
	void update(ChannelsAddressDTO channelsAddressDTO);
	
	/**
	 * 删除视频号4.0退货地址
	 * @param addressId
	 */
	void delete(String addressId);
	
	/**
	 * 获取视频号4.0默认退货地址
	 */
	ChannelsAddressVO getDefaultAddress();

	ChannelsAddressVO getAddressById(Long id);
}
