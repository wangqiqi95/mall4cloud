package com.mall4j.cloud.biz.service.channels;

import com.mall4j.cloud.biz.dto.channels.ChannelsSpuLogDTO;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.model.channels.ChannelsSpuUpdateLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface ChannelsSpuUpdateLogService extends IService<ChannelsSpuUpdateLog> {

    void saveLog(ChannelsSpuLogDTO channelsSpuLogDTO);

    void saveLog(ChannelsSpu channelsSpu, Long skuId, Integer stock, Integer type);
}
