package com.mall4j.cloud.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.group.model.CommodityPool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommodityPoolService extends IService<CommodityPool> {

    List<Long> getOpenCommoditys(List<String> activityChannels,Long storeId);
}
