package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.dto.channels.ChannelsSpuLogDTO;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.model.channels.ChannelsSpuUpdateLog;
import com.mall4j.cloud.biz.service.channels.ChannelsSpuUpdateLogService;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSpuUpdateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
@Slf4j
public class ChannelsSpuUpdateLogServiceImpl extends ServiceImpl<ChannelsSpuUpdateLogMapper, ChannelsSpuUpdateLog>
    implements ChannelsSpuUpdateLogService{

    @Override
    public void saveLog(ChannelsSpuLogDTO channelsSpuLogDTO) {
        Integer updateType = channelsSpuLogDTO.getUpdateType();

        ChannelsSpuUpdateLog spuUpdateLog = new ChannelsSpuUpdateLog();
        spuUpdateLog.setChannelsSpuId(channelsSpuLogDTO.getChannelsSpu().getSpuId());
        spuUpdateLog.setUpdateType(updateType);
        spuUpdateLog.setUpdateParam(channelsSpuLogDTO.toString());

        this.save(spuUpdateLog);
    }

    @Override
    public void saveLog(ChannelsSpu channelsSpu, Long skuId, Integer stock, Integer type) {
        Integer updateType = 2;

        ChannelsSpuUpdateLog spuUpdateLog = new ChannelsSpuUpdateLog();
        spuUpdateLog.setChannelsSpuId(channelsSpu.getSpuId());
        spuUpdateLog.setUpdateType(updateType);

        Map<String, String> map = new HashMap<>();
        map.put("ChannelsSkuId", StrUtil.toString(skuId));
        map.put("stockNum", StrUtil.toString(stock));
        map.put("type", StrUtil.toString(type));
        spuUpdateLog.setUpdateParam(map.toString());
        this.save(spuUpdateLog);
    }

}




