package com.mall4j.cloud.group.service;

import com.mall4j.cloud.api.group.feign.dto.OrderGiftInfoAppDTO;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftReduceAppDTO;
import com.mall4j.cloud.api.group.feign.vo.OrderGiftInfoAppVO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.vo.OrderGiftInfoVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.*;
import com.mall4j.cloud.group.model.OrderGiftShop;
import com.mall4j.cloud.group.vo.OrderGiftListVO;
import com.mall4j.cloud.group.vo.OrderGiftVO;

import java.util.List;

public interface OrderGiftBizService {
    ServerResponseEntity<Void> saveOrUpdateOrderGiftActivity(OrderGiftDTO param);

    ServerResponseEntity<OrderGiftVO> detail(Integer id);

    ServerResponseEntity<PageVO<OrderGiftListVO>> page(OpenScreenAdPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    ServerResponseEntity<OrderGiftInfoAppVO> info(OrderGiftInfoAppDTO param);

    ServerResponseEntity<Void> reduce(List<OrderGiftReduceAppDTO> param);

    ServerResponseEntity<Void> unlockStock(List<OrderGiftReduceAppDTO> param);

    ServerResponseEntity<List<OrderGiftShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);

    List<OrderGiftInfoVO>  giftInfoBySpuIdAndStoreId(List<Long> spuIdList, Long storeId);
}
