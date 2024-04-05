package com.mall4j.cloud.biz.service.channels.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.api.biz.dto.channels.response.EcWindowAddProductResponse;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSpuMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsSpu;
import com.mall4j.cloud.biz.service.channels.ChannelsWindowService;
import com.mall4j.cloud.biz.service.channels.EcWindowService;
import com.mall4j.cloud.common.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 视频号4.0 橱窗
 * @date 2023/3/9
 */
@Service
@Slf4j
public class ChannelsWindowServiceImpl implements ChannelsWindowService {

    @Autowired
    private EcWindowService ecWindowService;

    @Autowired
    private ChannelsSpuMapper channelsSpuMapper;

    @Override
    public void addProduct(Long channelsSpuId, Boolean isHideForWindow) {
        ChannelsSpu channelsSpu = channelsSpuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSpu.class)
                .eq(ChannelsSpu::getSpuId, channelsSpuId));
        Assert.isNull(channelsSpu, "未找到商品信息");

        ecWindowService.addProduct(StrUtil.toString(channelsSpu.getOutSpuId()), isHideForWindow);

        channelsSpuMapper.update(null, Wrappers.lambdaUpdate(ChannelsSpu.class)
                        .set(ChannelsSpu::getInWinodws, 1)
                .eq(ChannelsSpu::getId, channelsSpu.getId()));
    }

    @Override
    public void offProduct(Long channelsSpuId) {
        ChannelsSpu channelsSpu = channelsSpuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSpu.class)
                .eq(ChannelsSpu::getSpuId, channelsSpuId));
        Assert.isNull(channelsSpu, "未找到商品信息");

        ecWindowService.offProduct(StrUtil.toString(channelsSpu.getOutSpuId()));

        channelsSpuMapper.update(null, Wrappers.lambdaUpdate(ChannelsSpu.class)
                .set(ChannelsSpu::getInWinodws, 0)
                .eq(ChannelsSpu::getId, channelsSpu.getId()));
    }
}
