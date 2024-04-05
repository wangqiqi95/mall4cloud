package com.mall4j.cloud.group.service;

import com.mall4j.cloud.api.group.feign.dto.UserRegisterActivityDTO;
import com.mall4j.cloud.api.user.dto.UserRegisterGiftDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.RegisterActivityDTO;
import com.mall4j.cloud.group.dto.RegisterActivityPageDTO;
import com.mall4j.cloud.group.model.RegisterActivityShop;
import com.mall4j.cloud.group.vo.RegisterActivityListVO;
import com.mall4j.cloud.group.vo.RegisterActivityVO;

import java.util.List;

public interface RegisterActivityBizService {
    ServerResponseEntity<Void> saveOrUpdateRegisterActivity(RegisterActivityDTO param);

    ServerResponseEntity<RegisterActivityVO> detail(Integer id);

    ServerResponseEntity<PageVO<RegisterActivityListVO>> page(RegisterActivityPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    void userRegisterActivity(UserRegisterGiftDTO param);

    ServerResponseEntity<List<RegisterActivityShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);
}
