package com.mall4j.cloud.group.service;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.AdInfoDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdDTO;
import com.mall4j.cloud.group.dto.OpenScreenAdPageDTO;
import com.mall4j.cloud.group.model.OpenScreenAdShop;
import com.mall4j.cloud.group.vo.OpenScreenAdListVO;
import com.mall4j.cloud.group.vo.OpenScreenAdVO;
import com.mall4j.cloud.group.vo.app.AppAdInfoVO;

import java.util.List;

public interface OpenScreenAdBizService {
    ServerResponseEntity<Void> saveOrUpdateOpenScreenAdActivity(OpenScreenAdDTO param);

    ServerResponseEntity<OpenScreenAdVO> detail(Integer id);

    ServerResponseEntity<PageVO<OpenScreenAdListVO>> page(OpenScreenAdPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);

    ServerResponseEntity<AppAdInfoVO> adInfo(AdInfoDTO param);

    ServerResponseEntity<List<OpenScreenAdShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);
}
