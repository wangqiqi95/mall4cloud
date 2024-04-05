package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.biz.dto.channels.ChannelsBrandDTO;
import com.mall4j.cloud.biz.dto.channels.ChannelsBrandPageDTO;
import com.mall4j.cloud.biz.dto.channels.event.BrandAuditDTO;
import com.mall4j.cloud.biz.vo.channels.ChannelsBasicBrandVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsBrandOneVO;
import com.mall4j.cloud.biz.vo.channels.ChannelsBrandVO;

import java.util.List;

public interface ChannelsBrandService {
	
	/**
	 * 视频号4.0获取品牌库列表
	 * @param query
	 * @return
	 */
	List<ChannelsBasicBrandVO> basicBrandList(String query);
	
	/**
	 * 视频号4.0添加品牌
	 * @param channelsBrandDTO
	 */
	void save(ChannelsBrandDTO channelsBrandDTO);
	
	/**
	 * 视频号4.0品牌列表
	 * @param param
	 * @return
	 */
	List<ChannelsBrandVO> page(ChannelsBrandPageDTO param);
	
	/**
	 * 查询视频号4.0审核成功的品牌列表
	 * @return
	 */
	List<ChannelsBrandVO> brandList();
	
	/**
	 * 视频号4.0更新品牌资质
	 * @param channelsBrandDTO
	 */
	void updateBrand(ChannelsBrandDTO channelsBrandDTO);
	
	/**
	 * 视频号4.0撤回品牌资质审核
	 * @param brandId
	 */
	void cancelBrand(String brandId);
	
	/**
	 * 获取视频号4.0单个品牌信息
	 * @param brandId
	 * @return
	 */
	ChannelsBrandOneVO getByBrandId(String brandId);
	
	void updateBrandEvent(BrandAuditDTO brandAuditDTO);
}
