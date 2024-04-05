package com.mall4j.cloud.group.service;

import com.mall4j.cloud.api.group.feign.dto.UserPerfectDataActivityDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityDTO;
import com.mall4j.cloud.group.dto.PerfectDataActivityPageDTO;
import com.mall4j.cloud.group.model.PerfectDataActivityShop;
import com.mall4j.cloud.group.vo.PerfectDataActivityListVO;
import com.mall4j.cloud.group.vo.PerfectDataActivityVO;

import java.util.List;

public interface PerfectDataActivityBizService {
    ServerResponseEntity<Void> saveOrUpdatePerfectDataActivity(PerfectDataActivityDTO param);

    ServerResponseEntity<PerfectDataActivityVO> detail(Integer id);

    ServerResponseEntity<PageVO<PerfectDataActivityListVO>> page(PerfectDataActivityPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    void userPerfectDataActivity(UserPerfectDataActivityDTO param);

    ServerResponseEntity<List<PerfectDataActivityShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId,Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);
}
