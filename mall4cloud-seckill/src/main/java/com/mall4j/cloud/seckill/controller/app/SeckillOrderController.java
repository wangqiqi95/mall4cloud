
package com.mall4j.cloud.seckill.controller.app;

import cn.hutool.core.collection.CollectionUtil;
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
import com.mall4j.cloud.common.constant.StatusEnum;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.util.OrderLangUtil;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.seckill.dto.app.SeckillOrderDTO;
import com.mall4j.cloud.seckill.dto.app.SubmitSeckillOrderDTO;
import com.mall4j.cloud.seckill.manager.SeckillCacheManager;
import com.mall4j.cloud.seckill.service.SeckillOrderService;
import com.mall4j.cloud.seckill.service.SeckillService;
import com.mall4j.cloud.seckill.service.SeckillSkuService;
import com.mall4j.cloud.seckill.vo.SeckillSkuVO;
import com.mall4j.cloud.seckill.vo.SeckillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * 秒杀信息
 *
 * @author lhd
 * @date 2021-04-19 09:36:59
 */
@Slf4j
@RestController
@RequestMapping("/seckill_order" )
@Api(tags = "秒杀订单接口")
public class SeckillOrderController {

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private SeckillSkuService seckillSkuService;
    @Autowired
    private SeckillCacheManager seckillCacheManager;
    @Autowired
    private ConfirmOrderManager confirmOrderManager;
    @Autowired
    private ShopCartAdapter shopCartAdapter;
    @Autowired
    private ShopCartItemAdapter shopCartItemAdapter;
    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private SubmitOrderManager submitOrderManager;
    @Autowired
    private SeckillOrderService seckillOrderService;
    @Autowired
    private OnsMQTemplate seckillOrderSubmitTemplate;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;

    @ApiOperation(value = "获取秒杀订单路径")
    @GetMapping(value="/order_path")
    public ServerResponseEntity<String> getOrderPath(Long spuId) {
        Long userId = AuthUserContext.get().getUserId();
        SeckillVO seckill = seckillService.getBySpuId(spuId);
        // 前端看到这个状态码的时候，不用渲染活动页面了
        if (seckill == null || !Objects.equals(seckill.getStatus(), StatusEnum.ENABLE.value()) || seckill.getEndTime().getTime() < System.currentTimeMillis()) {
            return ServerResponseEntity.showFailMsg("活动已结束");
        }
        if (seckill.getStartTime().getTime() > System.currentTimeMillis()) {
            return ServerResponseEntity.showFailMsg("活动尚未开始");
        }
        String orderPath = seckillCacheManager.createOrderPath(userId + spuId);

        return ServerResponseEntity.success(orderPath);
    }


    @ApiOperation(value = "确认订单")
    @PostMapping(value="/{orderPath}/confirm")
    public ServerResponseEntity<ShopCartOrderMergerVO> confirm(@PathVariable("orderPath") String orderPath, @Valid @RequestBody SeckillOrderDTO orderParam,@RequestParam(value = "storeId", required = false,defaultValue = "1") Long storeId) {

        Long userId = AuthUserContext.get().getUserId();

        Long spuId = orderParam.getShopCartItem().getSpuId();
        seckillCacheManager.checkOrderPath(userId + spuId,orderPath);

        SeckillSkuVO appSeckillSkuVO = seckillSkuService.getBySeckillSkuId(orderParam.getSeckillSkuId());
        if (appSeckillSkuVO == null || !Objects.equals(orderParam.getShopCartItem().getSkuId(), appSeckillSkuVO.getSkuId())) {
            throw new LuckException("sku 信息有误");
        }

        log.info("秒杀订单-确认订单 getStoreId:{} getStationId:{} storeId:{}",orderParam.getStoreId(),orderParam.getStationId(),storeId);
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
                log.info("拼团活动id【{}】商品id【{}】为R渠道 不参与",appSeckillSkuVO.getSeckillId(),spuId);
                appSeckillSkuVO.setSeckillPrice(0L);
            }
        }


//        List<ShopCartItemVO> shopCartItemsDb = shopCartItemAdapter.conversionShopCartItem(orderParam.getShopCartItem(), appSeckillSkuVO.getSeckillPrice(), Constant.MAIN_SHOP);
        List<ShopCartItemVO> shopCartItemsDb = shopCartItemAdapter.conversionShopCartItem(orderParam.getShopCartItem(), appSeckillSkuVO.getSeckillPrice(), orderParam.getStoreId());
        // 将要返回给前端的完整的订单信息
        ShopCartOrderMergerVO shopCartOrderMerger = new ShopCartOrderMergerVO();
        shopCartOrderMerger.setDvyType(orderParam.getDvyType());
        shopCartOrderMerger.setOrderType(OrderType.SECKILL);
        shopCartOrderMerger.setSeckillSkuId(orderParam.getSeckillSkuId());
        shopCartOrderMerger.setSeckillId(appSeckillSkuVO.getSeckillId());
        shopCartOrderMerger.setStoreId(orderParam.getStoreId());
        // 筛选过滤掉不同配送的商品
        List<ShopCartItemVO> shopCartItems = confirmOrderManager.filterShopItemsByType(shopCartOrderMerger,shopCartItemsDb);
        OrderLangUtil.shopCartItemList(shopCartItems);
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

        shopCartOrderMerger.getShopCartOrders().forEach(shopCartOrderVO -> shopCartOrderVO.setOrderType(OrderType.SECKILL.value()));
        // 缓存计算新
        confirmOrderManager.cacheCalculatedInfo(userId, shopCartOrderMerger);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }

    /**
     * 购物车/立即购买  提交订单,根据店铺拆单
     */
    @PostMapping("/{orderPath}/submit")
    @ApiOperation(value = "提交订单", notes = "提交之后返回WebSocket路径，请求对应的WebSocket路径，开始等待响应")
    public ServerResponseEntity<List<Long>> submitOrders(@PathVariable("orderPath") String orderPath, @Valid @RequestBody SubmitSeckillOrderDTO submitOrderParam) {
        Long userId = AuthUserContext.get().getUserId();

        // 验证订单路径

        // 在这里面已经生成了订单id了
        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfo(submitOrderParam, userId);
        if (!checkResult.isSuccess()) {
            return ServerResponseEntity.transform(checkResult);
        }
        ShopCartOrderMergerVO mergerOrder = checkResult.getData();
        mergerOrder.setUserId(userId);
        ShopCartOrderVO shopCartOrderVO = mergerOrder.getShopCartOrders().get(0);
        ShopCartItemVO shopCartItemVO = shopCartOrderVO.getShopCartItemDiscounts().get(0).getShopCartItems().get(0);

        seckillCacheManager.checkOrderPath(userId + shopCartItemVO.getSpuId(),orderPath);

        // 秒杀活动信息（来自缓存）
        SeckillVO seckill = seckillService.getBySpuId(shopCartItemVO.getSpuId());

        // 前端看到这个状态码的时候，不用渲染活动页面了
        if (seckill == null || !Objects.equals(seckill.getStatus(), StatusEnum.ENABLE.value()) || seckill.getEndTime().getTime() < System.currentTimeMillis()) {
            return ServerResponseEntity.showFailMsg("活动已结束");
        }

        // 判断之前秒杀的商品有没有超过限制，-1表示商品不限制秒杀数量
        if (seckill.getMaxNum() != -1 && seckill.getMaxNum() < shopCartItemVO.getCount()) {
            return ServerResponseEntity.showFailMsg("超过购买限制");
        }
        // 判断之前秒杀的商品有没有超过限制，-1表示商品不限制秒杀数量
        boolean isOverNum = seckillOrderService.checkOrderSpuNum(seckill, mergerOrder.getUserId(), shopCartItemVO.getCount());
        if (!isOverNum) {
            return ServerResponseEntity.showFailMsg("超过购买限制");
        }
        // 这里一进来就减库存，但是为了防止少卖(下了单不付款)，15秒会自己更新库存~因为缓存只有10秒
        seckillCacheManager.decrSeckillSkuStocks(mergerOrder.getSeckillSkuId(),shopCartItemVO.getCount());

        // 下单
        SendResult sendResult = seckillOrderSubmitTemplate.syncSend(mergerOrder);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        return ServerResponseEntity.success(Collections.singletonList(shopCartOrderVO.getOrderId()));
    }

    /**
     * 购物车/立即购买  提交订单,根据店铺拆单
     */
    @PostMapping("/{orderPath}/new/submit")
    @ApiOperation(value = "提交订单", notes = "提交之后返回WebSocket路径，请求对应的WebSocket路径，开始等待响应")
    public ServerResponseEntity<List<OrderBO>> newSubmitOrders(@PathVariable("orderPath") String orderPath, @Valid @RequestBody SubmitSeckillOrderDTO submitOrderParam) {
        Long userId = AuthUserContext.get().getUserId();

        // 验证订单路径

        // 在这里面已经生成了订单id了
        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfo(submitOrderParam, userId);
        if (!checkResult.isSuccess()) {
            return ServerResponseEntity.transform(checkResult);
        }
        ShopCartOrderMergerVO mergerOrder = checkResult.getData();
        mergerOrder.setUserId(userId);
        ShopCartOrderVO shopCartOrderVO = mergerOrder.getShopCartOrders().get(0);
        ShopCartItemVO shopCartItemVO = shopCartOrderVO.getShopCartItemDiscounts().get(0).getShopCartItems().get(0);

        seckillCacheManager.checkOrderPath(userId + shopCartItemVO.getSpuId(),orderPath);

        // 秒杀活动信息（来自缓存）
        SeckillVO seckill = seckillService.getBySpuId(shopCartItemVO.getSpuId());

        // 前端看到这个状态码的时候，不用渲染活动页面了
        if (seckill == null || !Objects.equals(seckill.getStatus(), StatusEnum.ENABLE.value()) || seckill.getEndTime().getTime() < System.currentTimeMillis()) {
            return ServerResponseEntity.showFailMsg("活动已结束");
        }

        // 判断之前秒杀的商品有没有超过限制，-1表示商品不限制秒杀数量
        if (seckill.getMaxNum() != -1 && seckill.getMaxNum() < shopCartItemVO.getCount()) {
            return ServerResponseEntity.showFailMsg("超过购买限制");
        }
        // 判断之前秒杀的商品有没有超过限制，-1表示商品不限制秒杀数量
        boolean isOverNum = seckillOrderService.checkOrderSpuNum(seckill, mergerOrder.getUserId(), shopCartItemVO.getCount());
        if (!isOverNum) {
            return ServerResponseEntity.showFailMsg("超过购买限制");
        }
        // 这里一进来就减库存，但是为了防止少卖(下了单不付款)，15秒会自己更新库存~因为缓存只有10秒
        seckillCacheManager.decrSeckillSkuStocks(mergerOrder.getSeckillSkuId(),shopCartItemVO.getCount());

        // 下单
        SendResult sendResult = seckillOrderSubmitTemplate.syncSend(mergerOrder);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        OrderBO esOrderBO = new OrderBO();
        esOrderBO.setOrderId(shopCartOrderVO.getOrderId());
        esOrderBO.setOrderNumber(shopCartOrderVO.getOrderNumber());
        List<OrderBO> esOrderList = new ArrayList<>();
        esOrderList.add(esOrderBO);
        return ServerResponseEntity.success(esOrderList);

    }

}
