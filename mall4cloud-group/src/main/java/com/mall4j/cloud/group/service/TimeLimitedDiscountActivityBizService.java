package com.mall4j.cloud.group.service;

import com.mall4j.cloud.group.dto.TimeLimitedDiscountActivityDTO;
import com.mall4j.cloud.group.dto.TimeLimitedDiscountAddShopDTO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityVO;

/**
 * 限时调价活动
 *
 * @luzhengxiang
 * @create 2022-03-10 2:37 PM
 **/
public interface TimeLimitedDiscountActivityBizService {

    void insertActivity(TimeLimitedDiscountActivityDTO timeLimitedDiscountActivityDTO);

    void updateActivity(TimeLimitedDiscountActivityDTO timeLimitedDiscountActivityDTO);

    TimeLimitedDiscountActivityVO detail(Integer id);

    void enable(Integer id);

    void disable(Integer id);

    String checkSpuSkuPrice(Integer id);

    void addShop(TimeLimitedDiscountAddShopDTO addShopDTO);

    void deleteShop(Integer activityId,Integer shopId);
}
