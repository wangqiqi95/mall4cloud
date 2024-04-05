package com.mall4j.cloud.order.controller.app;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.livestore.response.LiveRoomResponse;
import com.mall4j.cloud.api.biz.dto.livestore.response.OrderAddResponse;
import com.mall4j.cloud.api.biz.feign.LiveRoomClient;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.coupon.dto.ChooseCouponDTO;
import com.mall4j.cloud.api.coupon.feign.CouponOrderFeignClient;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.discount.feign.DiscountFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCouponFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponSingleGetResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponStoreResultResp;
import com.mall4j.cloud.api.group.feign.GroupFeignClient;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.OrderGiftReduceAppDTO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.manager.ConfirmOrderManager;
import com.mall4j.cloud.api.order.manager.SubmitOrderManager;
import com.mall4j.cloud.api.payment.bo.MemberOrderInfoBO;
import com.mall4j.cloud.api.payment.feign.PayConfigFeignClient;
import com.mall4j.cloud.api.payment.vo.OrderPayTypeVO;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.dto.SpuSkuPriceDTO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.manager.ShopCartAdapter;
import com.mall4j.cloud.api.product.manager.ShopCartItemAdapter;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.UserLevelAndScoreOrderFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.OrderSource;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.dto.GiftProdDTO;
import com.mall4j.cloud.common.order.dto.OrderDTO;
import com.mall4j.cloud.common.order.dto.SubmitOrderDTO;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.bo.SubmitOrderPayAmountInfoBO;
import com.mall4j.cloud.order.config.OrderCancelConfigProperties;
import com.mall4j.cloud.order.model.OrderAddr;
import com.mall4j.cloud.order.service.OrderAddrService;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.SubmitOrderPayInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@RestController("appOrderController")
@RequestMapping("/order")
@Api(tags = "app-订单信息")
@Slf4j
@RefreshScope
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopCartAdapter shopCartAdapter;

    @Autowired
    private ConfirmOrderManager confirmOrderManager;

    @Autowired
    private ShopCartItemAdapter shopCartItemAdapter;

    @Autowired
    private DiscountFeignClient discountFeignClient;

    @Autowired
    private LiveRoomClient liveRoomClient;

    @Autowired
    private ThreadPoolExecutor orderThreadPoolExecutor;

    @Autowired
    private DeliveryFeignClient deliveryFeignClient;

    @Autowired
    private CouponOrderFeignClient couponOrderFeignClient;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderAddrService orderAddrService;

    @Autowired
    private SubmitOrderManager submitOrderManager;

    @Autowired
    private UserLevelAndScoreOrderFeignClient userLevelAndScoreOrderFeignClient;

    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private GroupFeignClient groupFeignClient;
    @Autowired
    StoreFeignClient storeFeignClient;
    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Value("${mall4cloud.sysconfig.spu.errordiscount:3.0}")
    @Setter
    private Double errordiscount=3.0;
    @Autowired
    TCouponFeignClient tCouponFeignClient;
    @Autowired
    private CrmCouponFeignClient crmCouponFeignClient;
    @Autowired
    private OrderCancelConfigProperties orderCancelConfigProperties;
    @Autowired
    private PayConfigFeignClient payConfigFeignClient;


    /**
     * 生成订单
     */
    @PostMapping("/confirm")
    @ApiOperation(value = "结算，生成订单信息", notes = "传入下单所需要的参数进行下单")
    public ServerResponseEntity<ShopCartOrderMergerVO> confirm(@RequestParam(value = "storeId", defaultValue = "1", required = false) Long storeId, @Valid @RequestBody OrderDTO orderParam) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Long userId = AuthUserContext.get().getUserId();
        String couponCode = orderParam.getCouponCode();
        log.info("入参数据1--->{},storeId:{} userId:{}",JSONObject.toJSONString(orderParam),storeId,userId);
        ServerResponseEntity<UserApiVO> userApiVOServerResponseEntity = userFeignClient.getUserData(userId);
        if (userApiVOServerResponseEntity == null && !userApiVOServerResponseEntity.isSuccess()) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        }
        /**
         * 校验门店是否可用，不可用重置成官店。
         */
        if( storeId != Constant.MAIN_SHOP){
            StoreVO storeVO = storeFeignClient.findByStoreId(storeId);
            if (storeVO ==null || storeVO.getStatus() != 1) {
                storeId = Constant.MAIN_SHOP;
            }
        }

        UserApiVO userApiVO = userApiVOServerResponseEntity.getData();
        log.info("入参数据2--->{},storeId:{} userId:{} userApiVO:{}",JSONObject.toJSONString(orderParam),storeId,userId,JSONObject.toJSONString(userApiVO));
        // 标识优惠券是否基于正价的打折券
        boolean flag = true;
        /**
         * 校验优惠券是否为正价基于吊牌价，如果是，当前订单不参与其他活动。
         */
        if (orderParam.getPriceType() != null && orderParam.getCouponType() != null
                && (orderParam.getPriceType() == 0 )) {
            flag = false;
        }

        // 校验纸质券
        if(StrUtil.isNotEmpty(couponCode)){
            
            //校验券码录入券项目类型是否是纸质券  "A"或"B"开头的券码代表纸质券码
            if(!couponCode.startsWith("A") && !couponCode.startsWith("B") ){
                Assert.faild("请输入正确券码。温馨提示：目前仅支持A或B开头优惠券券码兑换");
            }
            
            ServerResponseEntity<CouponSingleGetResp> couponSingleGetEntity = crmCouponFeignClient.couponSingleGet(couponCode);
            log.info("券码:{} 查询到单个优惠券信息为: {}",couponCode, JSONObject.toJSONString(couponSingleGetEntity));

            if (couponSingleGetEntity.isFail() || Objects.isNull(couponSingleGetEntity.getData())){
                String couponCodeMsg = "优惠券"+couponCode+"不存在!";
                if(couponCodeMsg.equals(couponSingleGetEntity.getMsg())){
                    Assert.faild("请输入正确券码。温馨提示：目前仅支持A或B开头优惠券券码兑换");
                }
                
                Assert.faild("网络繁忙，请稍后再试");
            }
            
            CouponSingleGetResp couponSingleGetResp = couponSingleGetEntity.getData();
    
            //校验有效时间,begin_time为空表示纸质券未激活
            String beginTime = couponSingleGetResp.getBegin_time();
            String endTime = couponSingleGetResp.getEnd_time();
    
            if(StrUtil.isNotEmpty(endTime) &&  DateUtil.parse(endTime).getTime() < System.currentTimeMillis() ) {
                Assert.faild("优惠券已过期");
            }
            
            if(("EXPIRED").equals(couponSingleGetResp.getStatus())){
                Assert.faild("优惠券已过期");
            }
            
            if(StrUtil.isEmpty(beginTime)){
                Assert.faild("该优惠券未激活，请联系客服");
            }
            
            //校验优惠券状态
            if(!("VALID").equals(couponSingleGetResp.getStatus())){
                Assert.faild("优惠券已使用");
            }
            
            //纸质券码录入,需要后端判断是否为正价基于吊牌价，如果是，当前订单不参与其他活动。 默认非正价
            if(StrUtil.isNotEmpty(couponSingleGetResp.getIs_price()) && "Y".equals(couponSingleGetResp.getIs_price()) ){
                flag = false;
            }
        }

//        if(CollectionUtil.isNotEmpty(orderParam.getCouponIds()) && StrUtil.isNotEmpty(orderParam.getCouponIds().get(0))){
//            ServerResponseEntity<CouponDetailVO> couponDetailVOServerResponseEntity =
//                    tCouponFeignClient.getCouponDetailByCouponCode(orderParam.getCouponIds().get(0));
//            if(couponDetailVOServerResponseEntity.isFail() || couponDetailVOServerResponseEntity.getData()==null){
//                Assert.faild("所选优惠券异常，请稍后再试！");
//            }
//
//            CouponDetailVO couponDetailVO = couponDetailVOServerResponseEntity.getData();
//            if(couponDetailVO.getPriceType() == 0){
//                flag = false;
//            }
//        }
        log.info("正价折扣券标识--->{}",JSONObject.toJSONString(flag));
        // 将要返回给前端的完整的订单信息
        ShopCartOrderMergerVO shopCartOrderMerger = new ShopCartOrderMergerVO();
        shopCartOrderMerger.setIsScorePay(orderParam.getIsScorePay());
        shopCartOrderMerger.setDvyType(orderParam.getDvyType());
        shopCartOrderMerger.setUsableScore(orderParam.getUserUseScore());
        shopCartOrderMerger.setOrderType(OrderType.ORDINARY);
        log.info("商品入参数据--->{},{},{}",JSONObject.toJSONString(orderParam.getShopCartItem()),storeId,flag);
        List<ShopCartItemVO> shopCartItemsDb = shopCartItemAdapter.getShopCartItems(orderParam.getShopCartItem(), storeId,flag);
        log.info("商品数据--->{}",JSONObject.toJSONString(shopCartItemsDb));

        //增加限时调价逻辑
        if (flag){

            //增加价取价逻辑(小程序配置的活动价)
            List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(shopCartItemsDb,SpuSkuPriceDTO.class);
            ServerResponseEntity<List<SkuTimeDiscountActivityVO>> response=spuFeignClient.getStoreSpuAndSkuPrice(storeId,skuPriceDTOS);
            log.info("活动价配置，查询对象：{}",JSONObject.toJSONString(response));
            if(response.isSuccess() && response.getData().size()>0){
                Map<Long, SkuTimeDiscountActivityVO> timeDisCountPriceMap = response.getData().stream().collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO));
                shopCartItemsDb.forEach(shopCartItemVO -> {
                    SkuTimeDiscountActivityVO skuPrice = timeDisCountPriceMap.get(shopCartItemVO.getSkuId());
                    if (skuPrice != null) {
                        shopCartItemVO.setSkuPriceFee(skuPrice.getPrice());
                        shopCartItemVO.setPriceFee(skuPrice.getPrice());
                        long totalPrice = skuPrice.getPrice() * shopCartItemVO.getCount();
                        shopCartItemVO.setActualTotal(totalPrice);
                        shopCartItemVO.setTotalAmount(totalPrice);
                        shopCartItemVO.setFriendlyCouponUseFlag(skuPrice.getFriendlyCouponUseFlag());
                        shopCartItemVO.setFriendlyDiscountFlag(skuPrice.getFriendlyDiscountFlag());
                        shopCartItemVO.setFriendlyFlag(skuPrice.isMemberPriceFlag()==true?1:0);
                        shopCartItemVO.setInvateStorePriceFlag(skuPrice.isInvateStorePriceFlag());
                        shopCartItemVO.setInvateStoreActivityId(skuPrice.getActivityId());
                    }
                });
            }
        }

        //验证商品价格，3折兜底。
        checkPrice(shopCartItemsDb);

        log.info("生成订单，计算限时调价结束，耗时：{}ms ",System.currentTimeMillis() - startTime);
        // 筛选过滤掉不同配送的商品
        List<ShopCartItemVO> shopCartItems = confirmOrderManager.filterShopItemsByType(shopCartOrderMerger, shopCartItemsDb);

//        OrderLangUtil.shopCartItemList(shopCartItems);


        // 该商品不满足任何的配送方式
        if (CollectionUtil.isEmpty(shopCartItems)) {
            return ServerResponseEntity.fail(ResponseEnum.ORDER_DELIVERY_NOT_SUPPORTED, shopCartOrderMerger);
        }
        // 购物车
        List<ShopCartVO> shopCarts = shopCartAdapter.conversionShopCart(shopCartItems);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 异步计算运费，运费暂时和优惠券没啥关联，可以与优惠券异步计算，获取用户地址，自提信息
        CompletableFuture<ServerResponseEntity<UserDeliveryInfoVO>> deliveryFuture = CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            return deliveryFeignClient.calculateAndGetDeliverInfo(new CalculateAndGetDeliverInfoDTO(orderParam.getAddrId(), orderParam.getDvyType(), orderParam.getStationId(), shopCartItems));
        }, orderThreadPoolExecutor);


        // 计算满减，并重新组合购物车 (满减是从购物车的时候就已经计算好了，提交订单理所当然应该用相同的方法)
        ShopCartFlagVO shopCartFlagVO = new ShopCartFlagVO();
        shopCartFlagVO.setFlag(flag);
        shopCartFlagVO.setShopCarts(shopCarts);
        log.info("满减前入参数据：{},{}",storeId,JSONObject.toJSONString(shopCartFlagVO));
        ServerResponseEntity<List<ShopCartVO>> discountShopCartsResponseEntity = discountFeignClient.calculateDiscountAndMakeUpShopCartByStoreId(storeId, shopCartFlagVO);
        if (!discountShopCartsResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(discountShopCartsResponseEntity);
        }
        shopCarts = discountShopCartsResponseEntity.getData();
        log.info("满减后返回数据：{}",JSONObject.toJSONString(shopCarts));
        log.info("生成订单，计算满减结束，耗时：{}ms ",System.currentTimeMillis() - startTime);


        // 运费用异步计算，最后要等运费出结果
        ServerResponseEntity<UserDeliveryInfoVO> userDeliveryInfoResponseEntity = deliveryFuture.get();
        if (!userDeliveryInfoResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(userDeliveryInfoResponseEntity);
        }
        UserDeliveryInfoVO deliveryInfoResponseEntityData = userDeliveryInfoResponseEntity.getData();
        log.info("生成订单，计算运费结束，耗时：{}ms ",System.currentTimeMillis() - startTime);
        // 计算优惠券，并返回优惠券信息
        ServerResponseEntity<List<ShopCartVO>> couponShopCartsResponseEntity = couponOrderFeignClient.chooseShopCoupon(new ChooseCouponDTO(orderParam.getUserChangeCoupon(),storeId, orderParam.getCouponIds(), shopCarts, deliveryInfoResponseEntityData.getTotalTransfee(), couponCode));
        if (!couponShopCartsResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(couponShopCartsResponseEntity);
        }
        shopCarts = couponShopCartsResponseEntity.getData();
        log.info("生成订单，计算优惠券优惠金额，耗时：{}ms ",System.currentTimeMillis() - startTime);
//        deliveryInfoResponseEntityData.setTotalFreeTransfee(shopCarts.stream().mapToLong(ShopCartVO::getFreeTransfee).sum());
        deliveryInfoResponseEntityData.setTotalTransfee(shopCarts.stream().mapToLong(ShopCartVO::getTransfee).sum());
        // 当算完一遍店铺的各种满减活动时，重算一遍订单金额
        confirmOrderManager.recalculateAmountWhenFinishingCalculateShop(shopCartOrderMerger, shopCarts, deliveryInfoResponseEntityData);
        log.info("生成订单，重算满减结束，耗时：{}ms ",System.currentTimeMillis() - startTime);
        long orderShopReduce = shopCartOrderMerger.getOrderReduce();

        // ===============================================开始平台优惠的计算==================================================
        // 计算平台优惠券，并返回平台优惠券信息
//        ServerResponseEntity<ShopCartOrderMergerVO> couponOrderMergerResponseEntity = couponOrderFeignClient.choosePlatformCoupon(new PlatformChooseCouponDTO(orderParam.getUserChangeCoupon(), orderParam.getCouponIds(), shopCartOrderMerger));
//
//        if (!couponOrderMergerResponseEntity.isSuccess()) {
//            return ServerResponseEntity.transform(couponOrderMergerResponseEntity);
//        }
//
//        shopCartOrderMerger = couponOrderMergerResponseEntity.getData();

//        ServerResponseEntity<ShopCartOrderMergerVO> calculateLevelDiscountResponseEntity = userLevelAndScoreOrderFeignClient.calculateLevelAndScoreDiscount(shopCartOrderMerger);
//        if (!calculateLevelDiscountResponseEntity.isSuccess()) {
//            return ServerResponseEntity.transform(calculateLevelDiscountResponseEntity);
//        }
//        shopCartOrderMerger = calculateLevelDiscountResponseEntity.getData();
//
//        // ===============================================结束平台优惠的计算==================================================
//
//        // 结束平台优惠的计算之后，还要重算一遍金额
//        confirmOrderManager.recalculateAmountWhenFinishingCalculatePlatform(shopCartOrderMerger, deliveryInfoResponseEntityData);

        // 重新插入spu、sku
        Map<Long, ShopCartItemVO> shopCartItemMap = shopCartItemsDb.stream().collect(Collectors.toMap(ShopCartItemVO::getSkuId, s -> s));
        for (ShopCartOrderVO shopCartOrder : shopCartOrderMerger.getShopCartOrders()) {
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartOrder.getShopCartItemDiscounts()) {
                for (ShopCartItemVO shopCartItem : shopCartItemDiscount.getShopCartItems()) {
                    ShopCartItemVO shopCartItemVO = shopCartItemMap.get(shopCartItem.getSkuId());
                    shopCartItem.setSkuLangList(shopCartItemVO.getSkuLangList());
                    shopCartItem.setSpuLangList(shopCartItemVO.getSpuLangList());
                }
            }
        }
        shopCartOrderMerger.setOrderShopReduce(orderShopReduce);
        shopCartOrderMerger.setStoreId(storeId);

        //增加订单来源
        String borrowLivingRoomId = orderService.getBorrowLivingRoomId(userApiVO.getOpenId());
        log.info("redis获取到的直播间id:{}", borrowLivingRoomId);
        // 排除结束24小时的直播间id
        if(StrUtil.isNotEmpty(borrowLivingRoomId)){
            ServerResponseEntity<LiveRoomResponse> roomInfo = liveRoomClient.getRoomInfo(borrowLivingRoomId);
            if (!roomInfo.isSuccess() || roomInfo.getData() == null
                    || DateUtil.between(roomInfo.getData().getEndTime(), new Date(), DateUnit.HOUR) >= 24) {
                borrowLivingRoomId = "";
            }
        }

        log.info("用户id：{},openId:{},获取到的直播间id:{}", userApiVO.getUserId(), userApiVO.getOpenId(), borrowLivingRoomId);
        shopCartOrderMerger.setOrderSource(OrderSource.NORMAL.value());
        /**
         * 首先判断有没有traceId，如果有值代表是视频号订单，如果没有值再判断是不是直播间订单。
         * 都不是就是普通订单。
         */
        shopCartOrderMerger.setTraceId(orderParam.getTraceId());
        if(StrUtil.isNotEmpty(orderParam.getTraceId())){
            shopCartOrderMerger.setOrderSource(OrderSource.LIVE_SHOP.value());
        }else{
            if (StrUtil.isNotBlank(borrowLivingRoomId)) {
                shopCartOrderMerger.setSourceId(borrowLivingRoomId);
                shopCartOrderMerger.setOrderSource(OrderSource.LIVE.value());
            }
        }

        //调整订单类型
        List<CouponOrderVO> coupons = shopCartOrderMerger.getCoupons();
        if (CollectionUtil.isNotEmpty(coupons)) {
            List<CouponOrderVO> chooseCoupon = coupons.stream().filter(couponOrderVO -> Objects.equals(Short.valueOf("3"), couponOrderVO.getKind())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(chooseCoupon)) {
                shopCartOrderMerger.setOrderType(OrderType.ENTERPRISE);
            }
        }
        //添加赠品逻辑
        ServerResponseEntity<ShopCartOrderMergerVO> giftInfo = groupFeignClient.orderGiftInfo(shopCartOrderMerger);
        if (!giftInfo.isSuccess()) {
            return ServerResponseEntity.transform(giftInfo);
        }
        shopCartOrderMerger = giftInfo.getData();
        //封装已选择参数信息 ， 完善商品资料信息
        List<OrderGiftInfoVO> giftInfoAppVOList = shopCartOrderMerger.getGiftInfoAppVOList();
        if (CollectionUtil.isNotEmpty(giftInfoAppVOList)) {
            List<Long> spuIdList = giftInfoAppVOList.stream().map(OrderGiftInfoVO::getSpuId).collect(Collectors.toList());
            ServerResponseEntity<List<SpuVO>> spuResponseEntity = spuFeignClient.listGiftSpuBySpuIds(spuIdList);
            if (spuResponseEntity.isFail()) {
                throw new LuckException("赠品信息查询异常");
            }
            List<SpuVO> data = spuResponseEntity.getData();
            Map<Long, SpuVO> spuMap = data.stream().collect(Collectors.toMap(SpuVO::getSpuId, spuVO -> spuVO));
            GiftProdDTO giftProdDTO = orderParam.getGiftProdDTO();
            giftInfoAppVOList.forEach(orderGiftInfoVO -> {
                SpuVO spuVO = spuMap.get(orderGiftInfoVO.getSpuId());
                log.info("spuVO--:{}", JSONObject.toJSONString(spuVO));
                if (giftProdDTO != null && giftProdDTO.getSpuId().equals(orderGiftInfoVO.getSpuId())) {
                    orderGiftInfoVO.setIsChoose(1);
                    orderGiftInfoVO.setSkuId(giftProdDTO.getSkuId());
                }
                orderGiftInfoVO.setImgUrl(spuVO.getMainImgUrl());
                orderGiftInfoVO.setSpuName(spuVO.getName());
                orderGiftInfoVO.setPriceFee(spuVO.getPriceFee());
                List<SkuVO> skus = spuVO.getSkus();
                List<GiftSkuVO> giftSkuVOList = skus.stream().map(skuVO -> {
                    GiftSkuVO giftSkuVO = new GiftSkuVO();
                    giftSkuVO.setSkuName(skuVO.getSkuName());
                    giftSkuVO.setSkuId(skuVO.getSkuId());
//                    giftSkuVO.setStatus(skuVO.getStatus());
                    giftSkuVO.setStatus(spuVO.getStatus());
                    giftSkuVO.setProperties(skuVO.getProperties());
                    return giftSkuVO;
                }).collect(Collectors.toList());
                orderGiftInfoVO.setGiftSkuVOList(giftSkuVOList);
            });
            shopCartOrderMerger.setGiftInfoAppVOList(giftInfoAppVOList);
        }
        // 缓存计算新
        confirmOrderManager.cacheCalculatedInfo(userId, shopCartOrderMerger);
        log.info("生成订单全部结束，耗时：{}ms ",System.currentTimeMillis() - startTime);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }

    /**
     * 购物车/立即购买  提交订单,根据店铺拆单
     */
    @PostMapping("/submit")
    @ApiOperation(value = "提交订单，返回支付流水号", notes = "根据传入的参数判断是否为购物车提交订单，同时对购物车进行删除，用户开始进行支付")
    public ServerResponseEntity<List<Long>> submitOrders(@RequestParam(value = "storeId", defaultValue = "1") Long storeId, @Valid @RequestBody SubmitOrderDTO submitOrderParam) {
        Long userId = AuthUserContext.get().getUserId();

        // 在这里面已经生成了订单id了
        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfo(submitOrderParam, userId);
        if (!checkResult.isSuccess()) {
            return ServerResponseEntity.transform(checkResult);
        }
        ShopCartOrderMergerVO mergerOrder = checkResult.getData();
        mergerOrder.setUserId(userId);

        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
        List<OrderGiftInfoVO> giftInfoAppVOList = mergerOrder.getGiftInfoAppVOList();
        if (CollectionUtil.isNotEmpty(giftInfoAppVOList)) {
            OrderGiftInfoVO orderGiftInfoVO = giftInfoAppVOList.stream().filter(orderGiftInfoVO1 -> orderGiftInfoVO1.getIsChoose().equals(1)).findFirst().orElse(null);
            if (orderGiftInfoVO != null) {
                //扣减赠品库存
                ArrayList<OrderGiftReduceAppDTO> orderGiftReduceAppDTOS = new ArrayList<>();
                OrderGiftReduceAppDTO orderGiftReduceAppDTO = new OrderGiftReduceAppDTO();
                orderGiftReduceAppDTO.setActivityId(orderGiftInfoVO.getGiftActivityId());
                orderGiftReduceAppDTO.setCommodityId(orderGiftInfoVO.getSpuId());
                orderGiftReduceAppDTO.setNum(orderGiftInfoVO.getNum());
                orderGiftReduceAppDTOS.add(orderGiftReduceAppDTO);
                ServerResponseEntity<Void> giftStockServerResponseEntity = groupFeignClient.reduceStock(orderGiftReduceAppDTOS);
                if (giftStockServerResponseEntity.isFail()) {
                    throw new LuckException(giftStockServerResponseEntity.getMsg());
                }
            }
        }
        // 锁定库存
        submitOrderManager.tryLockStock(shopCartOrders);
        //校验库存

        if (Objects.equals(mergerOrder.getOrderType(), OrderType.ORDINARY)) {
            // 锁定优惠券
            submitOrderManager.tryLockCoupon(mergerOrder);
        }


        // 锁积分
//        submitOrderManager.tryLockScore(mergerOrder);

        mergerOrder.setUserId(userId);
        mergerOrder.setTentacleNo(submitOrderParam.getTentacleNo());

        // 提交订单
        List<Long> orderIds = orderService.submit(mergerOrder);

        if(StrUtil.isNotEmpty(mergerOrder.getTraceId())){
            ServerResponseEntity<OrderAddResponse> orderAddResponse = liveStoreClient.orderInfoAdd(orderIds.get(0));
            if(orderAddResponse==null || orderAddResponse.isFail() || orderAddResponse.getData()==null || orderAddResponse.getData().getErrcode() >0){
                Assert.faild("添加视频号订单失败，请联系客服或者稍后再试。");
            }
            long wechatOrderId = orderAddResponse.getData().getData().getOrderId();
            orderService.syncWechatOrderId(orderIds.get(0),wechatOrderId);
        }



        return ServerResponseEntity.success(orderIds);
    }

    // todo 发版完删除老下单接口
    @PostMapping("/newSubmit")
    @ApiOperation(value = "提交订单，返回支付流水号", notes = "根据传入的参数判断是否为购物车提交订单，同时对购物车进行删除，用户开始进行支付")
        public ServerResponseEntity<List<EsOrderBO>> newSubmitOrders(@RequestParam(value = "storeId", defaultValue = "1") Long storeId, @Valid @RequestBody SubmitOrderDTO submitOrderParam) {
        long startTime = System.currentTimeMillis();
        Long userId = AuthUserContext.get().getUserId();

        // 在这里面已经生成了订单id了
        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfo(submitOrderParam, userId);
        if (!checkResult.isSuccess()) {
            return ServerResponseEntity.transform(checkResult);
        }
        ShopCartOrderMergerVO mergerOrder = checkResult.getData();
        mergerOrder.setUserId(userId);

        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();
        List<OrderGiftInfoVO> giftInfoAppVOList = mergerOrder.getGiftInfoAppVOList();
        if (CollectionUtil.isNotEmpty(giftInfoAppVOList)) {
            OrderGiftInfoVO orderGiftInfoVO = giftInfoAppVOList.stream().filter(orderGiftInfoVO1 -> orderGiftInfoVO1.getIsChoose().equals(1)).findFirst().orElse(null);
            if (orderGiftInfoVO != null) {
                //扣减赠品库存
                ArrayList<OrderGiftReduceAppDTO> orderGiftReduceAppDTOS = new ArrayList<>();
                OrderGiftReduceAppDTO orderGiftReduceAppDTO = new OrderGiftReduceAppDTO();
                orderGiftReduceAppDTO.setActivityId(orderGiftInfoVO.getGiftActivityId());
                orderGiftReduceAppDTO.setCommodityId(orderGiftInfoVO.getSpuId());
                orderGiftReduceAppDTO.setNum(orderGiftInfoVO.getNum());
                orderGiftReduceAppDTOS.add(orderGiftReduceAppDTO);
                ServerResponseEntity<Void> giftStockServerResponseEntity = groupFeignClient.reduceStock(orderGiftReduceAppDTOS);
                if (giftStockServerResponseEntity.isFail()) {
                    throw new LuckException(giftStockServerResponseEntity.getMsg());
                }
            }
        }
        // 锁定库存
        submitOrderManager.tryLockStock(shopCartOrders);
        //校验库存

        if (Objects.equals(mergerOrder.getOrderType(), OrderType.ORDINARY)) {
            // 锁定优惠券
            submitOrderManager.tryLockCoupon(mergerOrder);
        }


        // 锁积分
//        submitOrderManager.tryLockScore(mergerOrder);

        mergerOrder.setUserId(userId);
        mergerOrder.setTentacleNo(submitOrderParam.getTentacleNo());

        // 提交订单
        List<Long> orderIds = orderService.submit(mergerOrder);

        if(StrUtil.isNotEmpty(mergerOrder.getTraceId())){
            ServerResponseEntity<OrderAddResponse> orderAddResponse = liveStoreClient.orderInfoAdd(orderIds.get(0));
            if(orderAddResponse==null || orderAddResponse.isFail() || orderAddResponse.getData()==null || orderAddResponse.getData().getErrcode() >0){
                Assert.faild("添加视频号订单失败，请联系客服或者稍后再试。");
            }
            long wechatOrderId = orderAddResponse.getData().getData().getOrderId();
            orderService.syncWechatOrderId(orderIds.get(0),wechatOrderId);
        }
        List<EsOrderBO> esOrderList = orderService.getEsOrderList(orderIds);
        
        //设置付款类型
        EsOrderBO esOrderBO = esOrderList.get(0);
        MemberOrderInfoBO memberOrderInfoBO = new MemberOrderInfoBO();
        memberOrderInfoBO.setActualTotal(esOrderBO.getActualTotal());
        memberOrderInfoBO.setUserId(userId);
        
        ServerResponseEntity<OrderPayTypeVO> payTypeVOServerResponseEntity = payConfigFeignClient.queryOrderPayType(memberOrderInfoBO);
        if (Objects.isNull(payTypeVOServerResponseEntity)  || payTypeVOServerResponseEntity.isFail() || Objects.isNull(payTypeVOServerResponseEntity.getData())) {
            return ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
        }
        esOrderBO.setOrderPayType(payTypeVOServerResponseEntity.getData().getOrderPayType());
        
        log.info("提交订单全部结束，耗时：{}ms ",System.currentTimeMillis() - startTime);
        return ServerResponseEntity.success(esOrderList);
    }

    @PostMapping("/submit/test")
    @ApiOperation(value = "提交订单，返回支付流水号", notes = "根据传入的参数判断是否为购物车提交订单，同时对购物车进行删除，用户开始进行支付")
    public ServerResponseEntity<List<Long>> submitTest(@RequestParam(value = "storeId", defaultValue = "1") Long storeId, @Valid @RequestBody SubmitOrderDTO submitOrderParam) {
        Long userId = AuthUserContext.get().getUserId();

        // 在这里面已经生成了订单id了
        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfoTest(submitOrderParam, userId);
        if (!checkResult.isSuccess()) {
            return ServerResponseEntity.transform(checkResult);
        }
        ShopCartOrderMergerVO mergerOrder = checkResult.getData();

        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();

        // 锁定库存
        submitOrderManager.tryLockStock(shopCartOrders);
        //校验库存

        if (Objects.equals(mergerOrder.getOrderType(), OrderType.ORDINARY)) {
            // 锁定优惠券
            submitOrderManager.tryLockCoupon(mergerOrder);
        }


        // 锁积分
//        submitOrderManager.tryLockScore(mergerOrder);

        mergerOrder.setUserId(userId);
        mergerOrder.setTentacleNo(submitOrderParam.getTentacleNo());
        // 提交订单
        List<Long> orderIds = orderService.submit(mergerOrder);

        return ServerResponseEntity.success(orderIds);
    }

//    @PostMapping("/ua/submit/test2")
//    @ApiOperation(value = "test", notes = "test")
//    public ServerResponseEntity<List<Long>> test(@RequestParam(value = "storeId", defaultValue = "1") Long storeId) {
//        liveStoreClient.orderInfoAdd(storeId);
//
//        return ServerResponseEntity.success(null);
//    }



    @GetMapping("/order_pay_info")
    @ApiOperation(value = "获取订单支付信息", notes = "获取订单支付的商品/地址信息")
    @ApiImplicitParam(name = "orderIds", value = "订单流水号", required = true, dataType = "String")
    public ServerResponseEntity<SubmitOrderPayInfoVO> getOrderPayInfo(@RequestParam("orderIds") String orderIds) {
        long[] orderIdList = StrUtil.splitToLong(orderIds, ",");
        List<String> spuNameList = orderItemService.getSpuNameListByOrderIds(orderIdList);

        //获取订单信息
        SubmitOrderPayAmountInfoBO submitOrderPayAmountInfo = orderService.getSubmitOrderPayAmountInfo(orderIdList);

        if (Objects.isNull(submitOrderPayAmountInfo) || Objects.isNull(submitOrderPayAmountInfo.getCreateTime())) {
            return ServerResponseEntity.fail(ResponseEnum.ORDER_NOT_EXIST);
        }

        Date endTime = DateUtil.offsetMillisecond(submitOrderPayAmountInfo.getCreateTime(), RocketMqConstant.CANCEL_TIME_INTERVAL);
        SubmitOrderPayInfoVO orderPayInfoParam = new SubmitOrderPayInfoVO();
        orderPayInfoParam.setSpuNameList(spuNameList);
        orderPayInfoParam.setEndTime(endTime);
        orderPayInfoParam.setTotalFee(submitOrderPayAmountInfo.getTotalFee());
        orderPayInfoParam.setTotalScore(submitOrderPayAmountInfo.getTotalScore());
        orderPayInfoParam.setOrderScore(submitOrderPayAmountInfo.getOrderScore());
        orderPayInfoParam.setOrderType(submitOrderPayAmountInfo.getOrderType());

        // 地址
        if (Objects.nonNull(submitOrderPayAmountInfo.getOrderAddrId())) {
            OrderAddr orderAddr = orderAddrService.getByOrderAddrId(submitOrderPayAmountInfo.getOrderAddrId());
            //写入商品名、收货地址/电话
            String addr = orderAddr.getProvince() + orderAddr.getCity() + orderAddr.getArea() + orderAddr.getAddr();
            orderPayInfoParam.setUserAddr(addr);
            orderPayInfoParam.setConsignee(orderAddr.getConsignee());
            orderPayInfoParam.setMobile(orderAddr.getMobile());
        }
        return ServerResponseEntity.success(orderPayInfoParam);
    }

    // todo 发版后删除老接口
    @GetMapping("/new_order_pay_info")
    @ApiOperation(value = "获取订单支付信息", notes = "获取订单支付的商品/地址信息")
    @ApiImplicitParam(name = "orderIds", value = "订单流水号", required = true, dataType = "String")
    public ServerResponseEntity<SubmitOrderPayInfoVO> newGetOrderPayInfo(@RequestParam("orderIds") String orderIds) {
        if(StrUtil.isEmpty(orderIds)){
            Assert.faild("订单id不允许为空。");
        }
        long[] orderIdList = StrUtil.splitToLong(orderIds, ",");
        List<String> spuNameList = orderItemService.getSpuNameListByOrderIds(orderIdList);

        //获取订单信息
        SubmitOrderPayAmountInfoBO submitOrderPayAmountInfo = orderService.getSubmitOrderPayAmountInfo(orderIdList);

        if (Objects.isNull(submitOrderPayAmountInfo) || Objects.isNull(submitOrderPayAmountInfo.getCreateTime())) {
            return ServerResponseEntity.fail(ResponseEnum.ORDER_NOT_EXIST);
        }

        Date endTime = DateUtil.offsetMillisecond(submitOrderPayAmountInfo.getCreateTime(), orderCancelConfigProperties.fetchDelayTime());
        //Date endTime = DateUtil.offsetMillisecond(submitOrderPayAmountInfo.getCreateTime(), RocketMqConstant.CANCEL_TIME_INTERVAL);
        SubmitOrderPayInfoVO orderPayInfoParam = new SubmitOrderPayInfoVO();
        orderPayInfoParam.setSpuNameList(spuNameList);
        orderPayInfoParam.setEndTime(endTime);
        orderPayInfoParam.setCreateTime(submitOrderPayAmountInfo.getCreateTime());
        orderPayInfoParam.setTotalFee(submitOrderPayAmountInfo.getTotalFee());
        orderPayInfoParam.setTotalScore(submitOrderPayAmountInfo.getTotalScore());
        orderPayInfoParam.setOrderScore(submitOrderPayAmountInfo.getOrderScore());
        orderPayInfoParam.setOrderType(submitOrderPayAmountInfo.getOrderType());
        orderPayInfoParam.setOrderNumber(submitOrderPayAmountInfo.getOrderNumber());
        // 地址
        if (Objects.nonNull(submitOrderPayAmountInfo.getOrderAddrId())) {
            OrderAddr orderAddr = orderAddrService.getByOrderAddrId(submitOrderPayAmountInfo.getOrderAddrId());
            //写入商品名、收货地址/电话
            String addr = orderAddr.getProvince() + orderAddr.getCity() + orderAddr.getArea() + orderAddr.getAddr();
            orderPayInfoParam.setUserAddr(addr);
            orderPayInfoParam.setConsignee(orderAddr.getConsignee());
            orderPayInfoParam.setMobile(orderAddr.getMobile());
        }
        return ServerResponseEntity.success(orderPayInfoParam);
    }

    @GetMapping("/create_order_status")
    @ApiOperation(value = "根据订单id获取订单是否创建成功", notes = "订单数量")
    @ApiImplicitParam(name = "orderId", value = "订单流水号", required = true, dataType = "String")
    public ServerResponseEntity<Integer> getCreateOrderStatus(@RequestParam("orderId") Long orderId) {
        return ServerResponseEntity.success(orderService.countByOrderId(orderId));
    }

    /**
     * 购物车价格3折兜底判断
     * @param cartItems
     */
    private void checkPrice(List<ShopCartItemVO> cartItems){
        for (ShopCartItemVO cartItem : cartItems) {
            Long marketPriceFee = cartItem.getMarketPriceFee();
            Double bottomPrice = marketPriceFee * errordiscount / 10;
            log.info("下单价格兜底 折扣:{} 吊牌价:{} 下单价格:{} 兜底价:{}",errordiscount,marketPriceFee,cartItem.getPriceFee(),bottomPrice);
            //如果售价小于吊牌价三折 。抛出异常
            if(cartItem.getPriceFee() < bottomPrice){
                Assert.faild("当前订单异常，无法提交订单请联系客服处理！");
            }
        }
    }

//    @GetMapping("/ua/hystrixCommandTest")
//    @ApiOperation(value = "hystrixCommandTest", notes = "hystrixCommandTest")
//    @HystrixCommand
//    public ServerResponseEntity<Integer> getCreateOrderStatus() {
//        log.info("hystrixCommandTest");
//        discountFeignClient.hystrixCommandTest();
//        return ServerResponseEntity.success();
//    }


}
