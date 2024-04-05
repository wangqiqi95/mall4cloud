package com.mall4j.cloud.biz.vo.channels;

import lombok.Data;

import java.util.List;

@Data
public class ChannelsBrandStoreVO {
	
	//品牌库中的品牌信息
	private List<ChannelsBasicBrandVO> brands;
}
