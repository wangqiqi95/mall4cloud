package com.mall4j.cloud.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.group.model.CommodityPool;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CommodityPoolMapper extends BaseMapper<CommodityPool> {

    List<CommodityPool> getCommodityPoolsByStore(@Param("beginTime") Date beginTime,
                                          @Param("endTime") Date endTime,
                                          @Param("commodityIds")List<Long> commodityIds,
                                          @Param("storeIds")List<Long> storeIds);

    List<CommodityPool> getOpenCommoditys(@Param("activityChannels")List<String> activityChannels,
                                          @Param("storeId")Long storeId);
}
