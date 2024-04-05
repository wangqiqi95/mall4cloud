package com.mall4j.cloud.group.service;

import com.mall4j.cloud.api.group.feign.dto.UserFollowWechatActivityDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.FollowWechatDTO;
import com.mall4j.cloud.group.dto.FollowWechatPageDTO;
import com.mall4j.cloud.group.dto.ModifyShopDTO;
import com.mall4j.cloud.group.model.FollowWechatActivityShop;
import com.mall4j.cloud.group.vo.FollowWechatListVO;
import com.mall4j.cloud.group.vo.FollowWechatVO;

import java.util.List;

public interface FollowWechatBizService {
    ServerResponseEntity<Void> saveOrUpdateRegisterActivity(FollowWechatDTO param);

    ServerResponseEntity<FollowWechatVO> detail(Integer id);

    ServerResponseEntity<PageVO<FollowWechatListVO>> page(FollowWechatPageDTO param);

    ServerResponseEntity<Void> enable(Integer id);

    ServerResponseEntity<Void> disable(Integer id);

    ServerResponseEntity<Void> delete(Integer id);


    ServerResponseEntity<List<FollowWechatActivityShop>> getActivityShop(Integer activityId);

    ServerResponseEntity<Void> addActivityShop(ModifyShopDTO param);

    ServerResponseEntity<Void> deleteActivityShop(Integer activityId, Integer shopId);

    ServerResponseEntity<Void> deleteAllShop(Integer activityId);

    void userFollowWechatActivity(UserFollowWechatActivityDTO params);
}
