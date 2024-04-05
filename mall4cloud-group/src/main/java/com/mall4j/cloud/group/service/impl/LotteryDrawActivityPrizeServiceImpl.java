package com.mall4j.cloud.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.group.dto.LotteryPrizeStockChangeDTO;
import com.mall4j.cloud.group.mapper.LotteryDrawActivityPrizeMapper;
import com.mall4j.cloud.group.model.LotteryDrawActivityPrize;
import com.mall4j.cloud.group.service.LotteryDrawActivityPrizeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LotteryDrawActivityPrizeServiceImpl extends ServiceImpl<LotteryDrawActivityPrizeMapper, LotteryDrawActivityPrize> implements LotteryDrawActivityPrizeService {
    @Resource
    private LotteryDrawActivityPrizeMapper lotteryDrawActivityPrizeMapper;
    @Override
    public void changeStockNum(LotteryPrizeStockChangeDTO param) {
        lotteryDrawActivityPrizeMapper.changeStockNum(param);
    }

    @Override
    public void reduceStock(Integer prizeId) {
        lotteryDrawActivityPrizeMapper.reduceStockNum(prizeId);
    }
}
