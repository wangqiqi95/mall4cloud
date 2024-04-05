package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.dto.channels.ChannelsBrandPageDTO;
import com.mall4j.cloud.biz.dto.channels.event.BrandAuditDTO;
import com.mall4j.cloud.biz.model.channels.ChannelsBrandQualification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频号4.0品牌资质表
 *
 */
public interface ChannelsBrandQualificationMapper extends BaseMapper<ChannelsBrandQualification> {
	
	void updateBrandEvent(@Param("param") BrandAuditDTO param);
}
