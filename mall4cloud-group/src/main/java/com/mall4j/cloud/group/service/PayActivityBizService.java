package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.PayActivityDTO;
import com.mall4j.cloud.group.dto.PayActivityDrawAppDTO;
import com.mall4j.cloud.group.dto.PayActivityPageDTO;
import com.mall4j.cloud.group.model.PayActivityShop;
import com.mall4j.cloud.group.vo.PayActivityListVO;
import com.mall4j.cloud.group.vo.PayActivityVO;
import com.mall4j.cloud.group.vo.app.PayActivityInfoAppVO;

import java.util.List;

public interface PayActivityBizService {
    ServerResponseEntity<Void> saveOrUpdatePayActivity(PayActivityDTO param);

    ServerResponseEntity<PayActivityVO> detail(Integer id);

    ServerResponseEntity<PageVO<PayActivityListVO>> page(PayActivityPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    ServerResponseEntity<PayActivityInfoAppVO> info(String shopId,Long orderId);

    ServerResponseEntity<Void> draw(PayActivityDrawAppDTO param);

    ServerResponseEntity<List<PayActivityShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);
}
