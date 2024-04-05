package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.group.mapper.OrderGiftStockMapper;
import com.mall4j.cloud.group.model.OrderGift;
import com.mall4j.cloud.group.model.OrderGiftStock;
import com.mall4j.cloud.group.service.OrderGiftStockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderGiftStockServiceImpl extends ServiceImpl<OrderGiftStockMapper, OrderGiftStock> implements OrderGiftStockService {
    @Resource
    private OrderGiftStockMapper orderGiftStockMapper;
    @Override
    public void reduceStock(Integer orderGiftId, Long commodityId, Integer reduceNum) {
        Integer result = orderGiftStockMapper.reduceStock(orderGiftId,commodityId,reduceNum);
        if(result<=0){
            Assert.faild("库存不足，扣减库存失败。");
        }
    }

    @Override
    public void unlockStock(Integer orderGiftId, Long commodityId, Integer reduceNum) {
        orderGiftStockMapper.unlockStock(orderGiftId,commodityId,reduceNum);
    }
}
