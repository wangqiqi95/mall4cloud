package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.channels.ChannelsAddressPageDTO;
import com.mall4j.cloud.biz.model.channels.ChannelsAddress;
import com.mall4j.cloud.api.biz.vo.ChannelsAddressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 视频号4.0地址管理关联表
 * 
 */
@Mapper
public interface ChannelsAddressMapper extends BaseMapper<ChannelsAddress> {
	
	List<ChannelsAddressVO> list(@Param("param") ChannelsAddressPageDTO param);
	
	ChannelsAddressVO getDefaultAddress();

	ChannelsAddressVO getAddressById(@Param("id") Long id);
}
