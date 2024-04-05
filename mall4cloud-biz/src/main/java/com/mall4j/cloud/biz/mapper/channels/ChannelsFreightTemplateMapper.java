package com.mall4j.cloud.biz.mapper.channels;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsFreightTemplate;
import com.mall4j.cloud.biz.vo.channels.ChannelsFreightVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 视频号4.0运费模板
 *
 */
public interface ChannelsFreightTemplateMapper extends BaseMapper<ChannelsFreightTemplate> {
	
	List<ChannelsFreightVO> list(@Param("templateName") String templateName);
}
