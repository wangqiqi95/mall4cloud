package com.mall4j.cloud.order.controller.staff;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.dto.ChooseCouponDTO;
import com.mall4j.cloud.api.coupon.dto.PlatformChooseCouponDTO;
import com.mall4j.cloud.api.coupon.feign.CouponOrderFeignClient;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.CouponDetailVO;
import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.discount.feign.DiscountFeignClient;
import com.mall4j.cloud.api.group.feign.TimeDiscountFeignClient;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.manager.ConfirmOrderManager;
import com.mall4j.cloud.api.order.manager.SubmitOrderManager;
import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.api.order.vo.OrderShopVO;
import com.mall4j.cloud.api.platform.dto.TentacleContentDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.product.dto.SpuSkuPriceDTO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.manager.ShopCartAdapter;
import com.mall4j.cloud.api.product.manager.ShopCartItemAdapter;
import com.mall4j.cloud.api.product.vo.SkuTimeDiscountActivityVO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.UserLevelAndScoreOrderFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.dto.OrderDTO;
import com.mall4j.cloud.common.order.dto.SubmitOrderDTO;
import com.mall4j.cloud.common.order.util.OrderLangUtil;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.bo.SubmitOrderPayAmountInfoBO;
import com.mall4j.cloud.order.config.OrderCancelConfigProperties;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.constant.RefundType;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderAddr;
import com.mall4j.cloud.order.service.OrderAddrService;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderRefundService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import com.mall4j.cloud.order.vo.SubmitOrderPayInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController("staffOrderController")
@RequestMapping("/s/order")
@Api(tags = "导购小程序-代客订单信息")
@Slf4j
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
    private OrderRefundService orderRefundService;

    @Autowired
    private UserLevelAndScoreOrderFeignClient userLevelAndScoreOrderFeignClient;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private TimeDiscountFeignClient timeDiscountFeignClient;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    TentacleContentFeignClient tentacleContentFeignClient;
    @Autowired
    TCouponFeignClient tCouponFeignClient;
    @Autowired
    private OrderCancelConfigProperties orderCancelConfigProperties;

    /**
     * 代客下单生成订单
     */
    @PostMapping("/confirm")
    @ApiOperation(value = "结算，代客下单生成订单", notes = "传入下单所需要的参数进行下单")
    public ServerResponseEntity<ShopCartOrderMergerVO> confirm(@Valid @RequestBody OrderDTO orderParam) throws ExecutionException, InterruptedException {
        log.info("结算，代客下单生成订单 orderParam:{}", orderParam);
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()) {
            throw new LuckException("当前导购不存在");
        }

        Long storeId = staffData.getData().getStoreId();
        log.info("当前下单门店:{}",storeId);
        if (storeId == null) {
            storeId = Constant.MAIN_SHOP;
        }
        Long userId;
        if (null == orderParam.getUserId()) {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(staffData.getData().getMobile());
            if (!userData.isSuccess() || null == userData.getData()) {
                throw new LuckException("当前导购未注册会员");
            }
            userId = userData.getData().getUserId();
        } else {
            userId = orderParam.getUserId();
        }
        // 标识优惠券是否基于正价的打折券
        boolean flag = true;
        /**
         * 校验优惠券是否为正价基于吊牌价，如果是，当前订单不参与其他活动。
         */
        if (orderParam.getPriceType() != null && orderParam.getCouponType() != null
                && orderParam.getPriceType() == 0) {
            flag = false;
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
        log.info("正价折扣券标识--->{}", JSONObject.toJSONString(flag));
        // 将要返回给前端的完整的订单信息
        ShopCartOrderMergerVO shopCartOrderMerger = new ShopCartOrderMergerVO();
        shopCartOrderMerger.setIsScorePay(orderParam.getIsScorePay());
        shopCartOrderMerger.setDvyType(orderParam.getDvyType());
        shopCartOrderMerger.setUsableScore(orderParam.getUserUseScore());
        shopCartOrderMerger.setOrderType(OrderType.DAIKE);
        List<ShopCartItemVO> shopCartItemsDb = shopCartItemAdapter.staffConversionShopCartItem(orderParam.getShopCartItemList(), null,flag,storeId);
        if (CollectionUtil.isEmpty(shopCartItemsDb)) {
            throw new LuckException(ResponseEnum.SHOP_CART_NOT_EXIST);
        }

        //增加限时调价逻辑
        if (flag){

            //增加价取价逻辑(小程序配置的活动价)
            List<SpuSkuPriceDTO> skuPriceDTOS = mapperFacade.mapAsList(shopCartItemsDb,SpuSkuPriceDTO.class);
            ServerResponseEntity<List<SkuTimeDiscountActivityVO>> response=spuFeignClient.getStoreSpuAndSkuPrice(storeId,skuPriceDTOS);
            if(response.isSuccess() && response.getData().size()>0){
                Map<Long, SkuTimeDiscountActivityVO> timeDisCountPriceMap = response.getData().stream().collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO));
//                Map<Long, Long> timeDisCountPriceMap = response.getData().stream().collect(Collectors.toMap(SkuTimeDiscountActivityVO::getSkuId, timeDiscountActivityVO -> timeDiscountActivityVO.getPrice()));
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
                    }
//                    Long skuPrice = timeDisCountPriceMap.get(shopCartItemVO.getSkuId());
//                    if (skuPrice != null) {
//                        shopCartItemVO.setSkuPriceFee(skuPrice);
//                        shopCartItemVO.setPriceFee(skuPrice);
//                        long totalPrice = skuPrice * shopCartItemVO.getCount();
//                        shopCartItemVO.setActualTotal(totalPrice);
//                        shopCartItemVO.setTotalAmount(totalPrice);
//                    }
                });
            }
        }

        //验证商品价格，3折兜底。
        checkPrice(shopCartItemsDb);

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
            CalculateAndGetDeliverInfoDTO infoDTO = new CalculateAndGetDeliverInfoDTO(orderParam.getAddrId(), orderParam.getDvyType(), orderParam.getStationId(), shopCartItems);
            infoDTO.setUserId(userId);
            return deliveryFeignClient.staffCalculateAndGetDeliverInfo(infoDTO);
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
        // 运费用异步计算，最后要等运费出结果
        ServerResponseEntity<UserDeliveryInfoVO> userDeliveryInfoResponseEntity = deliveryFuture.get();
        if (!userDeliveryInfoResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(userDeliveryInfoResponseEntity);
        }
        UserDeliveryInfoVO deliveryInfoResponseEntityData = userDeliveryInfoResponseEntity.getData();
        ChooseCouponDTO chooseCouponDTO = new ChooseCouponDTO(orderParam.getUserChangeCoupon(),storeId, orderParam.getCouponIds(), shopCarts, userDeliveryInfoResponseEntity.getData().getTotalTransfee(),null);
        chooseCouponDTO.setUserId(userId);
        // 计算优惠券，并返回优惠券信息
        ServerResponseEntity<List<ShopCartVO>> couponShopCartsResponseEntity = couponOrderFeignClient.staffChooseShopCoupon(chooseCouponDTO);
        if (!couponShopCartsResponseEntity.isSuccess()) {
            return ServerResponseEntity.transform(couponShopCartsResponseEntity);
        }
        shopCarts = couponShopCartsResponseEntity.getData();

        deliveryInfoResponseEntityData.setTotalTransfee(shopCarts.stream().mapToLong(ShopCartVO::getTransfee).sum());
        // 当算完一遍店铺的各种满减活动时，重算一遍订单金额
        confirmOrderManager.recalculateAmountWhenFinishingCalculateShop(shopCartOrderMerger, shopCarts, userDeliveryInfoResponseEntity.getData());
        long orderShopReduce = shopCartOrderMerger.getOrderReduce();

        // ===============================================开始平台优惠的计算==================================================
        // 计算平台优惠券，并返回平台优惠券信息
//        PlatformChooseCouponDTO couponDTO = new PlatformChooseCouponDTO(orderParam.getUserChangeCoupon(), orderParam.getCouponIds(), shopCartOrderMerger);
//        couponDTO.setUserId(userId);
//        ServerResponseEntity<ShopCartOrderMergerVO> couponOrderMergerResponseEntity = couponOrderFeignClient.staffChoosePlatformCoupon(couponDTO);
//
//        if (!couponOrderMergerResponseEntity.isSuccess()) {
//            return ServerResponseEntity.transform(couponOrderMergerResponseEntity);
//        }
//
//        shopCartOrderMerger = couponOrderMergerResponseEntity.getData();
//        shopCartOrderMerger.setUserId(userId);
//        ServerResponseEntity<ShopCartOrderMergerVO> calculateLevelDiscountResponseEntity = userLevelAndScoreOrderFeignClient.staffCalculateLevelAndScoreDiscount(shopCartOrderMerger);
//        if (!calculateLevelDiscountResponseEntity.isSuccess()) {
//            return ServerResponseEntity.transform(calculateLevelDiscountResponseEntity);
//        }
//        shopCartOrderMerger = calculateLevelDiscountResponseEntity.getData();
//
//        // ===============================================结束平台优惠的计算==================================================
//
//        // 结束平台优惠的计算之后，还要重算一遍金额
//        confirmOrderManager.recalculateAmountWhenFinishingCalculatePlatform(shopCartOrderMerger, userDeliveryInfoResponseEntity.getData());

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
        shopCartOrderMerger.setUserId(userId);
        // 缓存计算新
        log.info("当前订单缓存门店id：{}",shopCartOrderMerger.getStoreId());
        log.info("当前订单计算折扣:{}",shopCartOrderMerger.getTotalLevelAmount());
        confirmOrderManager.cacheCalculatedInfo(userId, shopCartOrderMerger);
        return ServerResponseEntity.success(shopCartOrderMerger);
    }

    /**
     * 购物车/立即购买  提交代客订单
     */
    @PostMapping("/submit")
    @ApiOperation(value = "提交代客订单，返回支付流水号", notes = "根据传入的参数判断是否为购物车提交订单，同时对购物车进行删除，用户开始进行支付")
    public ServerResponseEntity<List<Long>> submitOrders(@Valid @RequestBody SubmitOrderDTO submitOrderParam) {
        log.info("提交代客订单 submitOrderParam:{}", submitOrderParam);
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()) {
            throw new LuckException("当前导购不存在");
        }
        if (submitOrderParam.getPayType() == null) {
            throw new LuckException("订单支付类型不能为空");
        }
        Long userId;
        if (null == submitOrderParam.getUserId()) {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(staffData.getData().getMobile());
            if (!userData.isSuccess() || null == userData.getData()) {
                throw new LuckException("当前导购未注册会员");
            }
            userId = userData.getData().getUserId();
        } else {
            userId = submitOrderParam.getUserId();
        }
        Long storeId = staffData.getData().getStoreId();

        // 在这里面已经生成了订单id了
        ServerResponseEntity<ShopCartOrderMergerVO> checkResult = submitOrderManager.checkSubmitInfo(submitOrderParam, userId);
        if (!checkResult.isSuccess()) {
            return ServerResponseEntity.transform(checkResult);
        }
        ShopCartOrderMergerVO mergerOrder = checkResult.getData();
        log.info("当前门店id：{}",mergerOrder.getStoreId());
        mergerOrder.setStoreId(storeId);


        //获取当前导购的触点。
        TentacleContentDTO tentacleContent = new TentacleContentDTO();
        tentacleContent.setBusinessId(staffData.getData().getId());
        tentacleContent.setTentacleType(4);
        ServerResponseEntity<TentacleContentVO> TentacleContentVOResponse = tentacleContentFeignClient.findOrCreateByContent(tentacleContent);
        if (!TentacleContentVOResponse.isSuccess() || null == TentacleContentVOResponse.getData()) {
            throw new LuckException("代客下单，获取导购触点失败。");
        }
        log.info("代客下单，提交订单重新获取导购的触点。TentacleNo：{}",TentacleContentVOResponse.getData().getTentacleNo());
        mergerOrder.setTentacleNo(TentacleContentVOResponse.getData().getTentacleNo());



        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();

        // 锁定库存
        submitOrderManager.tryLockStock(shopCartOrders);

        if (Objects.equals(mergerOrder.getOrderType(), OrderType.DAIKE)) {
            // 锁定优惠券
            submitOrderManager.tryLockCoupon(mergerOrder);
        }


        // 锁积分
        submitOrderManager.tryLockScore(mergerOrder);

        mergerOrder.setUserId(userId);
        mergerOrder.setStoreId(AuthUserContext.get().getStoreId());
        mergerOrder.setPayType(submitOrderParam.getPayType());
        // 提交订单
        mergerOrder.setBuyStaffId(staffData.getData().getId());
        List<Long> orderIds = orderService.submit(mergerOrder);

        return ServerResponseEntity.success(orderIds);
    }

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
    
        Date endTime = DateUtil.offsetMillisecond(submitOrderPayAmountInfo.getCreateTime(), orderCancelConfigProperties.fetchDelayTime());
        //Date endTime = DateUtil.offsetMillisecond(submitOrderPayAmountInfo.getCreateTime(), RocketMqConstant.CANCEL_TIME_INTERVAL);
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

    @GetMapping("/create_order_status")
    @ApiOperation(value = "根据订单id获取订单是否创建成功", notes = "订单数量")
    @ApiImplicitParam(name = "orderId", value = "订单流水号", required = true, dataType = "String")
    public ServerResponseEntity<Integer> getCreateOrderStatus(@RequestParam("orderId") Long orderId) {
        return ServerResponseEntity.success(orderService.countByOrderId(orderId));
    }


    /**
     * 分页获取
     */
    @GetMapping("/search_order")
    @ApiOperation(value = "代客订单列表信息查询", notes = "根据订单编号或者订单中商品名称搜索")
    public ServerResponseEntity<PageVO<EsOrderVO>> searchOrder(OrderSearchDTO orderSearchDTO) {
        Long userId = AuthUserContext.get().getUserId();
        orderSearchDTO.setBuyStaffId(userId);
        orderSearchDTO.setDeleteStatus(0);
        PageVO<EsOrderVO> esOrderPageVO = orderService.orderPage(orderSearchDTO);
        Long staffUserId = null;
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (staffData != null && staffData.isSuccess() && staffData.getData() != null) {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(staffData.getData().getMobile());
            if (userData != null && userData.isSuccess() && userData.getData() != null) {
                staffUserId = userData.getData().getUserId();
            }
        }


        // 处理下发货完成时能否查看物流
        for (EsOrderVO esOrderVO : esOrderPageVO.getList()) {
            if (Objects.equals(esOrderVO.getShopId(), Constant.PLATFORM_SHOP_ID)) {
                esOrderVO.setShopName(Constant.PLATFORM_SHOP_NAME);
            }
            int updateOrViewDeliveryInfo = 0;
            if (!Objects.equals(esOrderVO.getDeliveryType(), DeliveryType.DELIVERY.value())) {
                esOrderVO.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
                continue;
            }
            for (EsOrderItemVO orderItem : esOrderVO.getOrderItems()) {
                if (Objects.nonNull(orderItem.getDeliveryType()) && Objects.equals(orderItem.getDeliveryType(), DeliveryType.DELIVERY.value())) {
                    updateOrViewDeliveryInfo = 1;
                    break;
                }
            }
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(esOrderVO.getUserId());
            if (userData.isSuccess() && null != userData.getData()) {
                esOrderVO.setUserName(userData.getData().getNickName());
                esOrderVO.setUserMobile(userData.getData().getUserMobile());
            }
            esOrderVO.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
            if (staffUserId != null && esOrderVO.getUserId().equals(staffUserId)) {
                esOrderVO.setStaffOrder(true);
            } else {
                esOrderVO.setStaffOrder(false);
            }
        }
        log.info("当前代客订单列表数据:{},{}",esOrderPageVO.getTotal(),esOrderPageVO.getList().size());
        return ServerResponseEntity.success(esOrderPageVO);
    }


    /**
     * 订单详情信息接口
     */
    @GetMapping("/order_detail")
    @ApiOperation(value = "订单详情信息", notes = "根据订单号获取订单详情信息")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true, dataType = "Long")
    public ServerResponseEntity<OrderShopVO> orderDetail(@RequestParam(value = "orderId") Long orderId, @RequestParam(value = "userId", required = false) Long userId) {
        ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(AuthUserContext.get().getUserId());
        if (!staffData.isSuccess() || null == staffData.getData()) {
            throw new LuckException("当前导购不存在");
        }
        OrderShopVO orderShopDto = new OrderShopVO();
        orderShopDto.setCurrentStaffOrder(false);
        if (null == userId) {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(staffData.getData().getMobile());
            if (!userData.isSuccess() || null == userData.getData()) {
                throw new LuckException("当前导购未注册会员");
            }
            userId = userData.getData().getUserId();
            orderShopDto.setCurrentStaffOrder(true);
        } else {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(staffData.getData().getMobile());
            if (userData.isSuccess() && null != userData.getData() && userId.equals(userData.getData().getUserId())) {
                orderShopDto.setCurrentStaffOrder(true);
            }
        }
        Order order = orderService.getOrderByOrderIdAndUserId(orderId, userId);
        if (Objects.equals(order.getShopId(), Constant.PLATFORM_SHOP_ID)) {
            order.setShopName(Constant.PLATFORM_SHOP_NAME);
        }
        OrderAddr orderAddr = orderAddrService.getByOrderAddrId(order.getOrderAddrId());

        List<OrderItemVO> orderItems = orderItemService.listOrderItemAndLangByOrderId(orderId);

        setOrderShopDto(orderShopDto, order, orderAddr, orderItems);

        List<OrderRefundVO> orderRefunds = orderRefundService.getProcessingOrderRefundByOrderId(order.getOrderId());
        long alreadyRefundAmount = 0L;
        for (OrderRefundVO orderRefund : orderRefunds) {
            alreadyRefundAmount = alreadyRefundAmount + orderRefund.getRefundAmount();
            // 整单退款
            if (Objects.equals(RefundType.ALL.value(), orderRefund.getRefundType())) {
                orderShopDto.setCanRefund(false);
                // 统一的退款单号
                for (OrderItemVO orderItemDto : orderItems) {
                    orderItemDto.setFinalRefundId(orderRefund.getRefundId());
                }
                break;
            }
            // 单项退款，每个单号都不一样
            for (OrderItemVO orderItemDto : orderItems) {
                if (Objects.equals(orderItemDto.getOrderItemId(), orderRefund.getOrderItemId())) {
                    orderItemDto.setFinalRefundId(orderRefund.getRefundId());
                }
            }

        }
        orderShopDto.setCanRefundAmount(order.getActualTotal() - alreadyRefundAmount);
        if (order.getRefundStatus() != null && !Objects.equals(order.getRefundStatus(), RefundStatusEnum.DISAGREE.value())) {
            orderShopDto.setFinalRefundId(orderItems.get(0).getFinalRefundId());
        }

        return ServerResponseEntity.success(orderShopDto);
    }


    /**
     * 取消订单
     */
    @PutMapping("/cancel/{orderId}")
    @ApiOperation(value = "根据订单号取消订单", notes = "根据订单号取消订单")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true, dataType = "String")
    public ServerResponseEntity<String> cancel(@PathVariable("orderId") Long orderId, @RequestParam(value = "userId") Long userId) {
        Order order = orderService.getOrderByOrderIdAndUserId(orderId, userId);
        if (!Objects.equals(order.getStatus(), OrderStatus.UNPAY.value()) && !Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            // 订单已支付，无法取消订单
            return ServerResponseEntity.showFailMsg("订单已支付，无法取消订单");
        }
        // 如果订单未支付的话，将订单设为取消状态
        orderService.cancelOrderAndGetCancelOrderIds(Collections.singletonList(order.getOrderId()));
        return ServerResponseEntity.success();
    }


    /**
     * 插入数据
     *
     * @param orderShopDto 插入对象
     * @param order        订单信息
     * @param orderAddr    订单地址
     * @param orderItems   订单项列表
     */
    private void setOrderShopDto(OrderShopVO orderShopDto, Order order, OrderAddr orderAddr, List<OrderItemVO> orderItems) {
        // 处理下发货完成时能否查看物流
        int updateOrViewDeliveryInfo = 0;
        if (!Objects.equals(order.getDeliveryType(), DeliveryType.DELIVERY.value())) {
            orderShopDto.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
        }
        for (OrderItemVO orderItem : orderItems) {
            if (Objects.nonNull(orderItem.getDeliveryType()) && Objects.equals(orderItem.getDeliveryType(), DeliveryType.DELIVERY.value())) {
                updateOrViewDeliveryInfo = 1;
                break;
            }
        }
        orderShopDto.setUserId(order.getUserId());
        orderShopDto.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
        orderShopDto.setOrderScore(order.getOrderScore());
        orderShopDto.setShopId(order.getShopId());
        orderShopDto.setDeliveryType(order.getDeliveryType());
        orderShopDto.setShopName(order.getShopName());
        orderShopDto.setActualTotal(order.getActualTotal());
        OrderAddrVO orderAddrVO = new OrderAddrVO();
        BeanUtils.copyProperties(orderAddr, orderAddrVO);
        orderShopDto.setOrderAddr(orderAddrVO);
        orderShopDto.setPayType(order.getPayType());
        orderShopDto.setTransfee(order.getFreightAmount());
        orderShopDto.setReduceAmount(Math.abs(order.getReduceAmount()));
        orderShopDto.setCreateTime(order.getCreateTime());
        orderShopDto.setRemarks(order.getRemarks());
        orderShopDto.setOrderType(order.getOrderType());
        orderShopDto.setStatus(order.getStatus());
        // 返回满减优惠金额，优惠券优惠金额和店铺优惠总额
        orderShopDto.setDiscountMoney(order.getDiscountAmount());
        orderShopDto.setShopCouponMoney(order.getShopCouponAmount());
        orderShopDto.setShopAmount(order.getReduceAmount() - order.getPlatformAmount());
        //返回平台优惠券，平台等级，平台积分优惠金额和平台免运费金额
        orderShopDto.setPlatformCouponAmount(order.getPlatformCouponAmount());
        orderShopDto.setMemberAmount(order.getMemberAmount());
        orderShopDto.setScoreAmount(order.getScoreAmount());
        orderShopDto.setPlatformFreeFreightAmount(order.getPlatformFreeFreightAmount());
        orderShopDto.setShopChangeFreeAmount(order.getShopChangeFreeAmount());
        // 付款时间
        orderShopDto.setPayTime(order.getPayTime());
        // 发货时间
        orderShopDto.setDeliveryTime(order.getDeliveryTime());
        // 完成时间
        orderShopDto.setFinallyTime(order.getFinallyTime());
        // 取消时间
        orderShopDto.setCancelTime(order.getCancelTime());
        // 更新时间
        orderShopDto.setUpdateTime(order.getUpdateTime());
        //订单发票id
        orderShopDto.setOrderInvoiceId(order.getOrderInvoiceId());

        // 可以退款的状态，并在退款时间内
        if (order.getStatus() > OrderStatus.UNPAY.value() && order.getStatus() < OrderStatus.CLOSE.value() && orderRefundService.checkRefundDate(order)) {
            orderShopDto.setCanRefund(true);
            orderShopDto.setCanAllRefund(true);
            for (OrderItemVO orderItem : orderItems) {
                // 有正在退款中的订单项
                if (orderItem.getRefundStatus() != null && !Objects.equals(orderItem.getRefundStatus(), RefundStatusEnum.DISAGREE.value())) {
                    // 无法整单退款
                    orderShopDto.setCanAllRefund(false);
                }
            }
            // 正在进行整单退款
            if (order.getRefundStatus() != null && !Objects.equals(order.getRefundStatus(), RefundStatusEnum.DISAGREE.value())) {
                // 所有订单项都没办法退款
                orderShopDto.setCanAllRefund(false);
            }
        }

        orderShopDto.setOrderItems(orderItems);
        orderShopDto.setTotal(order.getTotal());
        orderShopDto.setTotalNum(order.getAllCount());
    }

    /**
     * 购物车价格3折兜底判断
     * @param cartItems
     */
    private void checkPrice(List<ShopCartItemVO> cartItems){
        for (ShopCartItemVO cartItem : cartItems) {
            Long marketPriceFee = cartItem.getMarketPriceFee();
            Long bottomPrice = marketPriceFee * 3 / 10;
            //如果售价小于吊牌价三折 。抛出异常
            if(cartItem.getPriceFee() < bottomPrice){
                Assert.faild("当前订单异常，无法提交订单请联系客服处理！");
            }
        }

    }
}
