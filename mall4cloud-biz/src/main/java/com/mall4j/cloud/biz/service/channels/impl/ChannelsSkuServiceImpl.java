package com.mall4j.cloud.biz.service.channels.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.biz.mapper.channels.ChannelsSkuMapper;
import com.mall4j.cloud.biz.model.channels.ChannelsSku;
import com.mall4j.cloud.biz.service.channels.ChannelsSkuService;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 视频号4.0商品
 *
 * @author FrozenWatermelon
 * @date 2023-02-07 15:01:48
 */
@Service
public class ChannelsSkuServiceImpl extends ServiceImpl<ChannelsSkuMapper, ChannelsSku> implements ChannelsSkuService{

    @Autowired
    private ChannelsSkuMapper channelsSkuMapper;

    @Override
    public PageVO<ChannelsSku> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> channelsSkuMapper.list());
    }

    @Override
    public ChannelsSku getById(Long id) {
        return channelsSkuMapper.getById(id);
    }


    @Override
    public void deleteById(Long id) {
        channelsSkuMapper.deleteById(id);
    }

    @Override
    public ChannelsSku getByOutSkuId(Long outSkuId) {
        return channelsSkuMapper.selectOne(Wrappers.lambdaQuery(ChannelsSku.class)
                .eq(ChannelsSku::getOutSkuId, outSkuId));
    }

    @Override
    public void reduceChannelsStockBySkuId(Long skuId, Integer stock) {
        channelsSkuMapper.reduceChannelsStockBySkuId(skuId, stock);
    }
}
