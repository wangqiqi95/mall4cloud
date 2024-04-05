package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.dto.LotteryPrizeStockChangeDTO;
import com.mall4j.cloud.group.model.LotteryDrawActivityPrize;
import org.apache.ibatis.annotations.Param;

public interface LotteryDrawActivityPrizeMapper extends BaseMapper<LotteryDrawActivityPrize> {
    void changeStockNum(LotteryPrizeStockChangeDTO param);

    void reduceStockNum(@Param("prizeId") Integer prizeId);
}
