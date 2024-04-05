package com.mall4j.cloud.group.feign;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.group.feign.GroupFeignClient;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftInfoAppDTO;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftReduceAppDTO;
import com.mall4j.cloud.api.group.feign.dto.UserFollowWechatActivityDTO;
import com.mall4j.cloud.api.group.feign.dto.UserPerfectDataActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.OrderGiftInfoAppVO;
import com.mall4j.cloud.common.order.vo.OrderGiftInfoVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.product.vo.GroupActivitySpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.constant.GroupActivityStatusEnum;
import com.mall4j.cloud.group.service.*;
import com.mall4j.cloud.group.vo.GroupActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
@RestController
public class GroupFeignController implements GroupFeignClient {

    @Autowired
    private GroupActivityService groupActivityService;
    @Resource
    private RegisterActivityBizService registerActivityBizService;
    @Resource
    private PerfectDataActivityBizService perfectDataActivityBizService;
    @Resource
    private FollowWechatBizService followWechatBizService;
    @Resource
    private OrderGiftBizService orderGiftBizService;

    @Override
    public ServerResponseEntity<Date> getActivityStartTime(Long activityId) {
        GroupActivityVO groupActivity = groupActivityService.getByGroupActivityId(activityId);
        if (!Objects.equals(groupActivity.getStatus(), GroupActivityStatusEnum.ENABLE.value())) {
            return ServerResponseEntity.success();
        }
        Date date = new Date();
        Date startTime = null;
        // 活动结束时间小于等于当前时间，
        if (groupActivity.getEndTime().getTime() <= System.currentTimeMillis()) {
            return ServerResponseEntity.success(startTime);
        }
        // 预热活动的开始时间设为当前
        if (Objects.equals(groupActivity.getIsPreheat(), 1)) {
            startTime = new Date();
        }
        // 非预热活动的开始时间为活动开始时间
        else if (date.getTime() < groupActivity.getEndTime().getTime()) {
            startTime = groupActivity.getStartTime();
        }
        return ServerResponseEntity.success(startTime);
    }

    @Override
    public ServerResponseEntity<List<GroupActivitySpuVO>> groupSpuListBySpuIds(List<Long> spuIds) {
        if (CollUtil.isEmpty(spuIds)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<GroupActivitySpuVO> groupActivitySpuList = groupActivityService.groupSpuListBySpuIds(spuIds);
        return ServerResponseEntity.success(groupActivitySpuList);
    }

    @Override
    public ServerResponseEntity<Void> offlineGroupBySpuIds(List<Long> spuIds) {
        groupActivityService.offlineGroupBySpuIds(spuIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> userPerfectDataActivity(UserPerfectDataActivityDTO param) {
        perfectDataActivityBizService.userPerfectDataActivity(param);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> userFollowWechatActivity(UserFollowWechatActivityDTO params) {
        followWechatBizService.userFollowWechatActivity(params);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<OrderGiftInfoAppVO>> giftInfo(List<OrderGiftInfoAppDTO> param) {
        List<OrderGiftInfoAppVO> orderGiftInfoAppVOS = new ArrayList<>();
        param.forEach(temp -> {
            ServerResponseEntity<OrderGiftInfoAppVO> info = orderGiftBizService.info(temp);
            OrderGiftInfoAppVO data = info.getData();
            orderGiftInfoAppVOS.add(data);
        });
        return ServerResponseEntity.success(orderGiftInfoAppVOS);
    }

    @Override
    public ServerResponseEntity<Void> reduceStock(List<OrderGiftReduceAppDTO> param) {
        return orderGiftBizService.reduce(param);
    }

    @Override
    public ServerResponseEntity<Void> unLockStock(List<OrderGiftReduceAppDTO> param) {
        return orderGiftBizService.unlockStock(param);
    }

    @Override
    public ServerResponseEntity<ShopCartOrderMergerVO> orderGiftInfo(ShopCartOrderMergerVO shopCartOrderMerger) {
        //查询可参与赠品活动并封装
        List<Long> spuIdList = new ArrayList<>();
        shopCartOrderMerger.getShopCartOrders().forEach(shopCartOrderVO -> {
            shopCartOrderVO.getShopCartItemDiscounts().forEach(shopCartItemDiscountVO -> {
                shopCartItemDiscountVO.getShopCartItems().forEach(shopCartItemVO -> {
                    spuIdList.add(shopCartItemVO.getSpuId());
                });
            });
        });
        //下单门店信息
        Long storeId = shopCartOrderMerger.getStoreId();

        //查询商品列表
        List<OrderGiftInfoVO>  giftInfoAppVOList = orderGiftBizService.giftInfoBySpuIdAndStoreId(spuIdList, storeId);

        shopCartOrderMerger.setGiftInfoAppVOList(giftInfoAppVOList);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }
}
