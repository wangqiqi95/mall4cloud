package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.model.OrderGiftStock;

public interface OrderGiftStockService extends IService<OrderGiftStock> {
    void reduceStock(Integer orderGiftId,Long commodityId,Integer reduceNum);

    void unlockStock(Integer orderGiftId,Long commodityId,Integer reduceNum);
}
