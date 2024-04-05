package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.dto.LotteryPrizeStockChangeDTO;
import com.mall4j.cloud.group.model.LotteryDrawActivityPrize;

public interface LotteryDrawActivityPrizeService extends IService<LotteryDrawActivityPrize> {
    void changeStockNum(LotteryPrizeStockChangeDTO param);
    void reduceStock(Integer prizeId);
}
