package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.dto.OrderDTO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.MealActivityDTO;
import com.mall4j.cloud.group.dto.MealActivityPageDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.model.MealActivityShop;
import com.mall4j.cloud.group.vo.MealActivityListVO;
import com.mall4j.cloud.group.vo.MealActivitySaveVO;
import com.mall4j.cloud.group.vo.MealActivityVO;
import com.mall4j.cloud.group.vo.app.MealInfoAppVO;

import java.util.List;

public interface MealActivityBizService {
    ServerResponseEntity<Void> saveOrUpdateMealActivity(MealActivityDTO param);

    ServerResponseEntity<MealActivityVO> detail(Integer id);

    ServerResponseEntity<PageVO<MealActivityListVO>> page(MealActivityPageDTO param);

    ServerResponseEntity<MealActivitySaveVO> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    ServerResponseEntity<MealInfoAppVO> info(String shopId);

    ServerResponseEntity<List<MealActivityShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);

    ServerResponseEntity<ShopCartOrderMergerVO> submit(Integer activityId, OrderDTO  param);
}
