package com.mall4j.cloud.group.service;

import com.mall4j.cloud.group.dto.ActivityCommodityAddDTO;

import java.util.Date;
import java.util.List;

public interface ActivityCommodityBizService {
    ActivityCommodityAddDTO addActivityCommodity(List<Long> commodityIds, Date beginTime, Date endTime, Integer activityChannel, Long activityId);

    /**
     * 商品池区分门店
     * @param commodityIds
     * @param beginTime
     * @param endTime
     * @param activityChannel
     * @param activityId
     * @param storeIds
     * @return
     */
    ActivityCommodityAddDTO addActivityCommodity(List<Long> commodityIds,
                                                 Date beginTime,
                                                 Date endTime,
                                                 Integer activityChannel,
                                                 Long activityId,
                                                 List<Long> storeIds);

    void removeActivityCommodity(List<String> commodityIds);

    void removeActivityCommodityByActivityId(Long activityId, Integer activityChannel);
}
