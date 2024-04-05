package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.mapper.CommodityPoolMapper;
import com.mall4j.cloud.group.model.CommodityPool;
import com.mall4j.cloud.group.service.CommodityPoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommodityPoolServiceImpl extends ServiceImpl<CommodityPoolMapper, CommodityPool> implements CommodityPoolService {

    @Resource
    private CommodityPoolMapper commodityPoolMapper;

    @Override
    public List<Long> getOpenCommoditys(List<String> activityChannels, Long storeId) {
        return commodityPoolMapper.getOpenCommoditys(activityChannels,storeId).stream().map(CommodityPool::getCommodityId).collect(Collectors.toList());
    }
}
