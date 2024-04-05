package com.mall4j.cloud.group.controller.group.app;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.OrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.manager.ConfirmOrderManager;
import com.mall4j.cloud.api.order.manager.SubmitOrderManager;
import com.mall4j.cloud.api.product.dto.SpuSkuRDTO;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.manager.ShopCartAdapter;
import com.mall4j.cloud.api.product.manager.ShopCartItemAdapter;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.util.OrderLangUtil;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTransactionTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.bo.GroupOrderBO;
import com.mall4j.cloud.group.constant.GroupActivityStatusEnum;
import com.mall4j.cloud.group.constant.TeamStatusEnum;
import com.mall4j.cloud.group.dto.GroupOrderDTO;
import com.mall4j.cloud.group.dto.SubmitGroupOrderDTO;
import com.mall4j.cloud.group.model.GroupSku;
import com.mall4j.cloud.group.service.GroupActivityService;
import com.mall4j.cloud.group.service.GroupOrderService;
import com.mall4j.cloud.group.service.GroupSkuService;
import com.mall4j.cloud.group.service.GroupTeamService;
import com.mall4j.cloud.group.vo.GroupTeamVO;
import com.mall4j.cloud.group.vo.app.AppGroupActivityVO;
import com.mall4j.cloud.group.vo.app.AppGroupSkuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * 团购的代码其实可以和订单的代码一起写的，不过
 *
 * @author FrozenWatermelon
 * @date 2021-04-07 10:39:32
 */
@Slf4j
@RestController("appGroupOrderController")
@RequestMapping("/group_order")
@Api(tags = "拼团订单表")
public class GroupOrderController {

    @Autowired
    private GroupOrderService groupOrderService;

    @Autowired
    private GroupTeamService groupTeamService;

    @Autowired
    private GroupActivityService groupActivityService;

    @Autowired
    private ShopCartAdapter shopCartAdapter;

    @Autowired
    private ShopCartItemAdapter shopCartItemAdapter;

    @Autowired
    private DeliveryFeignClient deliveryFeignClient;

    @Autowired
    private ConfirmOrderManager confirmOrderManager;

    @Autowired
    private SubmitOrderManager submitOrderManager;

    @Autowired
    private OnsMQTransactionTemplate groupOrderCreateTemplate;

    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private GroupSkuService groupSkuService;


    @PostMapping("/confirm")
    @ApiOperation(value = "确认订单", notes = "传入参团/开团所需要的参数进行下单,如果用户为开团时拼团团队Id(groupTeamId)为0,如用户为开团则需要将拼团团队Id(groupTeamId)需要带上")
    public ServerResponseEntity<ShopCartOrderMergerVO> confirm(@Valid @RequestBody GroupOrderDTO orderParam,@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId) {
        Long userId = AuthUserContext.get().getUserId();
        Long spuId = orderParam.getShopCartItem().getSpuId();
        log.info("拼团confirm---> storeId:{} userId:{} 入参orderParam:{}",storeId, userId,JSON.toJSON(orderParam));
        GroupSku groupSku=groupSkuService.getByGroupSkuId(orderParam.getGroupSkuId());
        if(Objects.isNull(groupSku)){
            return ServerResponseEntity.showFailMsg("拼团活动信息配置有误，请联系客服");
        }
//        AppGroupActivityVO activityVO = groupActivityService.getBySpuId(spuId);
        AppGroupActivityVO activityVO = groupActivityService.getBySpuIdAndActivityid(spuId,groupSku.getGroupActivityId());

        // 校验活动
        if (Objects.isNull(activityVO) || !Objects.equals(activityVO.getStatus(), GroupActivityStatusEnum.ENABLE.value())  || activityVO.getEndTime().getTime() < System.currentTimeMillis()) {
            // 拼团活动不在进行中，请稍后重试
            return ServerResponseEntity.showFailMsg("拼团活动已结束");
        }


        AppGroupSkuVO appGroupSkuVO = null;
        List<AppGroupSkuVO> groupSkuList = activityVO.getGroupSkuList();
        for (AppGroupSkuVO dbAppGroupSkuVO : groupSkuList) {
            if (Objects.equals(dbAppGroupSkuVO.getGroupSkuId(), orderParam.getGroupSkuId())) {
                appGroupSkuVO = dbAppGroupSkuVO;
                break;
            }
        }
        if (appGroupSkuVO == null) {
            throw new LuckException("sku 信息有误");
        }
        log.info("拼团-确认订单 getStoreId:{} getStationId:{} storeId:{}",orderParam.getStoreId(),orderParam.getStationId(),storeId);
        if(Objects.isNull(orderParam.getStoreId())){
            orderParam.setStoreId(storeId);
        }
        //过滤R渠道不参与拼团活动价
        log.info("前置--> R渠道商品不参与限时调价活动(仅限官店) storeId:{} {}",orderParam.getStoreId(),(orderParam.getStoreId()==1?"官店不参与执行剔除逻辑":"门店参与"));
        if(Objects.nonNull(orderParam.getStoreId()) && orderParam.getStoreId()==1){
            List<Long> spuIds=new ArrayList<>();
            spuIds.add(spuId);
            ServerResponseEntity<List<Long>> serverResponseEntity=spuFeignClient.isSpuSkuChannel(new SpuSkuRDTO(SpuChannelEnums.CHANNEL_R.getCode(),spuIds));
            if(serverResponseEntity.isSuccess()
                    && CollectionUtil.isNotEmpty(serverResponseEntity.getData())
                    && serverResponseEntity.getData().size()>0){
                log.info("拼团活动id【{}】商品id【{}】为R渠道 不参与",activityVO.getGroupActivityId(),spuId);
                appGroupSkuVO.setPriceFee(0L);
            }
        }


//        List<ShopCartItemVO> shopCartItemsDb = shopCartItemAdapter.conversionShopCartItem(orderParam.getShopCartItem(), appGroupSkuVO.getPriceFee(), Constant.MAIN_SHOP);
        List<ShopCartItemVO> shopCartItemsDb = shopCartItemAdapter.conversionShopCartItem(orderParam.getShopCartItem(), appGroupSkuVO.getPriceFee(), orderParam.getStoreId());
        OrderLangUtil.shopCartItemList(shopCartItemsDb);

        // 将要返回给前端的完整的订单信息
        ShopCartOrderMergerVO shopCartOrderMerger = new ShopCartOrderMergerVO();
        shopCartOrderMerger.setDvyType(orderParam.getDvyType());
        shopCartOrderMerger.setOrderType(OrderType.GROUP);
        shopCartOrderMerger.setStoreId(orderParam.getStoreId());
        shopCartOrderMerger.setGroupActivityId(groupSku.getGroupActivityId());
        shopCartOrderMerger.setGroupSkuId(groupSku.getGroupSkuId());
        // 筛选过滤掉不同配送的商品
        List<ShopCartItemVO> shopCartItems = confirmOrderManager.filterShopItemsByType(shopCartOrderMerger,shopCartItemsDb);

        // 该商品不满足任何的配送方式
        if (CollectionUtil.isEmpty(shopCartItems)) {
            return ServerResponseEntity.fail(ResponseEnum.ORDER_DELIVERY_NOT_SUPPORTED, shopCartOrderMerger);
        }
        // 购物车
        List<ShopCartVO> shopCarts = shopCartAdapter.conversionShopCart(shopCartItems);

        // 计算运费，获取用户地址，自提信息
        ServerResponseEntity<UserDeliveryInfoVO> userDeliveryInfoResponseEntity = deliveryFeignClient.calculateAndGetDeliverInfo(new CalculateAndGetDeliverInfoDTO(orderParam.getAddrId(), orderParam.getDvyType(), orderParam.getStationId(), shopCartItems));

        // 当算完一遍店铺的各种满减活动时，重算一遍订单金额
        confirmOrderManager.recalculateAmountWhenFinishingCalculateShop(shopCartOrderMerger, shopCarts, userDeliveryInfoResponseEntity.getData());
        // 结束平台优惠的计算之后，还要重算一遍金额
        confirmOrderManager.recalculateAmountWhenFinishingCalculatePlatform(shopCartOrderMerger, userDeliveryInfoResponseEntity.getData());

        // 缓存计算新
        confirmOrderManager.cacheCalculatedInfo(userId, shopCartOrderMerger);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }

//    @PostMapping("/submit")
//    @ApiOperation(value = "提交订单，返回支付流水号", notes = "根据传入的参数判断是否为购物车提交订单，同时对购物车进行删除，用户开始进行支付")
//    public ServerResponseEntity<List<Long>> submitOrders(@RequestParam(value = "storeId", defaultValue = "1") Long storeId,
//                                                         @Valid @RequestBody SubmitGroupOrderDTO submitOrderParam) {
//        Long userId = AuthUserContext.get().getUserId();
//        // 在这里面已经生成了订单id了
//        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfo(submitOrderParam, userId);
//        if (!checkResult.isSuccess()) {
//            return ServerResponseEntity.transform(checkResult);
//        }
//        ShopCartOrderMergerVO mergerOrder = checkResult.getData();
//        mergerOrder.setUserId(userId);
//
//        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
//        ShopCartOrderVO shopCartOrderVO = shopCartOrders.get(0);
//        // 设置为团购订单
//        shopCartOrderVO.setOrderType(OrderType.GROUP.value());
//        // 上面已经校验过订单信息了，所以商品信息从缓存中获取没有错
//        ShopCartItemVO shopCartItemVO = shopCartOrderVO.getShopCartItemDiscounts().get(0).getShopCartItems().get(0);
//        Long spuId = shopCartItemVO.getSpuId();
//        Integer count = shopCartItemVO.getCount();
//
//
//        // 校验活动
//        log.info("拼团submit---> storeId:{} userId:{} 入参submitOrderParam:{} mergerOrder:{}",storeId, userId,JSON.toJSON(submitOrderParam),JSON.toJSON(mergerOrder));
//        GroupSku groupSku=groupSkuService.getByGroupSkuId(mergerOrder.getGroupSkuId());
//        if(Objects.isNull(groupSku)){
//            return ServerResponseEntity.showFailMsg("拼团活动信息配置有误，请联系客服");
//        }
////        AppGroupActivityVO activityVO = groupActivityService.getBySpuId(spuId);
//        AppGroupActivityVO activityVO = groupActivityService.getBySpuIdAndActivityid(spuId,mergerOrder.getGroupActivityId());
//        if (!Objects.equals(activityVO.getStatus(), GroupActivityStatusEnum.ENABLE.value()) || activityVO.getEndTime().getTime() < System.currentTimeMillis()) {
//            // 拼团活动不在进行中，不能参与拼团
//            return ServerResponseEntity.showFailMsg("拼团活动不在进行中，不能参与拼团");
//        }
//        Integer groupOrderCount = groupOrderService.getOrderBySpuIdAndUserId(activityVO.getGroupActivityId(), spuId, userId);
//        if (groupOrderCount > 0) {
//            throw new LuckException("您已有拼团中的订单，请耐心等待拼团成功");
//        }
//
//        // 购买数量限制
//        if (Objects.equals(activityVO.getHasMaxNum(), 1)) {
//            Integer dbSpuCount = groupOrderService.getUserHadSpuCountByGroupActivityId(userId, activityVO.getGroupActivityId());
//            if (dbSpuCount + count > activityVO.getMaxNum()) {
//                return ServerResponseEntity.showFailMsg("您购买数量已超过活动限购数量，无法提交订单");
//            }
//        }
//
//        // 校验活动商品是否处于失效状态
//        if (activityVO.getStatus() != 1) {
//            // 活动商品已失效或不存在
//            return ServerResponseEntity.showFailMsg("活动商品已失效或不存在");
//        }
//
//        GroupTeamVO groupTeam;
//        // 校验拼团团队是否可以参团
//        if (!Objects.equals(submitOrderParam.getGroupTeamId(),0L)) {
//            groupTeam = groupTeamService.getByGroupTeamId(submitOrderParam.getGroupTeamId());
//            if (Objects.isNull(groupTeam)) {
//                // 未找到拼团团队信息
//                return ServerResponseEntity.showFailMsg("未找到拼团团队信息");
//            }
//            if (!TeamStatusEnum.IN_GROUP.value().equals(groupTeam.getStatus())) {
//                // 拼团团队不在拼团中，不能参与拼团
//                return ServerResponseEntity.showFailMsg("拼团团队不在拼团中，不能参与拼团");
//            }
//            if (groupTeam.getEndTime() != null && System.currentTimeMillis() > groupTeam.getEndTime().getTime()) {
//                // 该拼团单不存在
//                return ServerResponseEntity.showFailMsg("该拼团单不存在");
//            }
//        }
//
//        // 锁定库存
//        submitOrderManager.tryLockStock(shopCartOrders);
//
//        GroupOrderBO groupOrderBO = new GroupOrderBO();
//        groupOrderBO.setGroupTeamId(submitOrderParam.getGroupTeamId());
//        groupOrderBO.setGroupActivityId(activityVO.getGroupActivityId());
//        groupOrderBO.setShopId(activityVO.getShopId());
//        groupOrderBO.setUserId(userId);
//        groupOrderBO.setShareUserId(userId);
//        groupOrderBO.setActivityProdPrice(mergerOrder.getTotal());
//        groupOrderBO.setPayPrice(mergerOrder.getActualTotal());
//        groupOrderBO.setOrderId(shopCartOrderVO.getOrderId());
//        groupOrderBO.setCount(shopCartOrderVO.getTotalCount());
//        groupOrderBO.setSpuId(shopCartItemVO.getSpuId());
//
//        // 开启事务消息，通知订单订单服务开始创建订单
//        SendResult sendResult = groupOrderCreateTemplate.sendMessageInTransaction(groupOrderBO, mergerOrder);
//        if (sendResult == null || sendResult.getMessageId() == null) {
//            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
//        }
//
//        return ServerResponseEntity.success(Collections.singletonList(shopCartOrderVO.getOrderId()));
//    }

    @PostMapping("/new/submit")
    @ApiOperation(value = "提交订单，返回支付流水号", notes = "根据传入的参数判断是否为购物车提交订单，同时对购物车进行删除，用户开始进行支付")
    public ServerResponseEntity<List<OrderBO>> newSubmitOrders(@RequestParam(value = "storeId", defaultValue = "1") Long storeId,
                                                               @Valid @RequestBody SubmitGroupOrderDTO submitOrderParam) {
        Long userId = AuthUserContext.get().getUserId();

        // 在这里面已经生成了订单id了
        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfo(submitOrderParam, userId);
        if (!checkResult.isSuccess()) {
            return ServerResponseEntity.transform(checkResult);
        }
        ShopCartOrderMergerVO mergerOrder = checkResult.getData();
        mergerOrder.setUserId(userId);

        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
        ShopCartOrderVO shopCartOrderVO = shopCartOrders.get(0);
        // 设置为团购订单
        shopCartOrderVO.setOrderType(OrderType.GROUP.value());
        // 上面已经校验过订单信息了，所以商品信息从缓存中获取没有错
        ShopCartItemVO shopCartItemVO = shopCartOrderVO.getShopCartItemDiscounts().get(0).getShopCartItems().get(0);
        Long spuId = shopCartItemVO.getSpuId();
        Integer count = shopCartItemVO.getCount();


        // 校验活动
        log.info("拼团submit---> storeId:{} userId:{} 入参submitOrderParam:{} mergerOrder:{}",storeId, userId,JSON.toJSON(submitOrderParam),JSON.toJSON(mergerOrder));
        GroupSku groupSku=groupSkuService.getByGroupSkuId(mergerOrder.getGroupSkuId());
        if(Objects.isNull(groupSku)){
            return ServerResponseEntity.showFailMsg("拼团活动信息配置有误，请联系客服");
        }
//        AppGroupActivityVO activityVO = groupActivityService.getBySpuId(spuId);
        AppGroupActivityVO activityVO = groupActivityService.getBySpuIdAndActivityid(spuId,mergerOrder.getGroupActivityId());
        if (!Objects.equals(activityVO.getStatus(), GroupActivityStatusEnum.ENABLE.value()) || activityVO.getEndTime().getTime() < System.currentTimeMillis()) {
            // 拼团活动不在进行中，不能参与拼团
            return ServerResponseEntity.showFailMsg("拼团活动不在进行中，不能参与拼团");
        }
        Integer groupOrderCount = groupOrderService.getOrderBySpuIdAndUserId(activityVO.getGroupActivityId(), spuId, userId);
        if (groupOrderCount > 0) {
            throw new LuckException("您已有拼团中的订单，请耐心等待拼团成功");
        }

        // 购买数量限制
        if (Objects.equals(activityVO.getHasMaxNum(), 1)) {
            Integer dbSpuCount = groupOrderService.getUserHadSpuCountByGroupActivityId(userId, activityVO.getGroupActivityId());
            if (dbSpuCount + count > activityVO.getMaxNum()) {
                return ServerResponseEntity.showFailMsg("您购买数量已超过活动限购数量，无法提交订单");
            }
        }

        // 校验活动商品是否处于失效状态
        if (activityVO.getStatus() != 1) {
            // 活动商品已失效或不存在
            return ServerResponseEntity.showFailMsg("活动商品已失效或不存在");
        }

        GroupTeamVO groupTeam;
        // 校验拼团团队是否可以参团
        if (!Objects.equals(submitOrderParam.getGroupTeamId(),0L)) {
            groupTeam = groupTeamService.getByGroupTeamId(submitOrderParam.getGroupTeamId());
            if (Objects.isNull(groupTeam)) {
                // 未找到拼团团队信息
                return ServerResponseEntity.showFailMsg("未找到拼团团队信息");
            }
            if (!TeamStatusEnum.IN_GROUP.value().equals(groupTeam.getStatus())) {
                // 拼团团队不在拼团中，不能参与拼团
                return ServerResponseEntity.showFailMsg("拼团团队不在拼团中，不能参与拼团");
            }
            if (groupTeam.getEndTime() != null && System.currentTimeMillis() > groupTeam.getEndTime().getTime()) {
                // 该拼团单不存在
                return ServerResponseEntity.showFailMsg("该拼团单不存在");
            }
        }

        // 锁定库存
        submitOrderManager.tryLockStock(shopCartOrders);

        GroupOrderBO groupOrderBO = new GroupOrderBO();
        groupOrderBO.setGroupTeamId(submitOrderParam.getGroupTeamId());
        groupOrderBO.setGroupActivityId(activityVO.getGroupActivityId());
        groupOrderBO.setShopId(activityVO.getShopId());
        groupOrderBO.setUserId(userId);
        groupOrderBO.setShareUserId(userId);
        groupOrderBO.setActivityProdPrice(mergerOrder.getTotal());
        groupOrderBO.setPayPrice(mergerOrder.getActualTotal());
        groupOrderBO.setOrderId(shopCartOrderVO.getOrderId());
        groupOrderBO.setCount(shopCartOrderVO.getTotalCount());
        groupOrderBO.setSpuId(shopCartItemVO.getSpuId());

        // 开启事务消息，通知订单订单服务开始创建订单
        SendResult sendResult = groupOrderCreateTemplate.sendMessageInTransaction(groupOrderBO, mergerOrder);
        if (sendResult == null || sendResult.getMessageId() == null) {
            return ServerResponseEntity.fail(ResponseEnum.EXCEPTION);
        }
        OrderBO esOrderBO = new OrderBO();
        esOrderBO.setOrderId(shopCartOrderVO.getOrderId());
        esOrderBO.setOrderNumber(shopCartOrderVO.getOrderNumber());
        List<OrderBO> esOrderList = new ArrayList<>();
        esOrderList.add(esOrderBO);
        return ServerResponseEntity.success(esOrderList);
    }

}
