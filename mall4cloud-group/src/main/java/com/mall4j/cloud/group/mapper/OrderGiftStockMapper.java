package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.model.OrderGiftStock;
import org.apache.ibatis.annotations.Param;

public interface OrderGiftStockMapper extends BaseMapper<OrderGiftStock> {
    Integer reduceStock(@Param("orderGiftId") Integer orderGiftId,@Param("commodityId") Long commodityId,@Param("reduceNum") Integer reduceNum);

    void unlockStock(@Param("orderGiftId") Integer orderGiftId,@Param("commodityId") Long commodityId,@Param("reduceNum") Integer reduceNum);
}
