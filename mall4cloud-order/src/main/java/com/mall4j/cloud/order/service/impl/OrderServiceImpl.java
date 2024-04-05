package com.mall4j.cloud.order.service.impl;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.biz.dto.channels.EcDeliveryProductInfo;
import com.mall4j.cloud.api.biz.dto.channels.EcFreightProductInfo;
import com.mall4j.cloud.api.biz.dto.channels.request.EcDeliverySendRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.DeliveryInfo;
import com.mall4j.cloud.api.biz.dto.livestore.request.DeliverySendRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.ProductInfo;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.delivery.dto.CalculateAndGetDeliverInfoDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.delivery.feign.DeliveryCompanyFeignClient;
import com.mall4j.cloud.api.delivery.feign.DeliveryFeignClient;
import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.distribution.feign.DistributionJointVentureCommissionFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmOrderFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.api.docking.skq_sqb.config.SQBParams;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByCancelOrder;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByQueryResult;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.QueryResultRespTender;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBCancelOrderResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBQueryResultResp;
import com.mall4j.cloud.api.docking.skq_sqb.feign.LitePosApiFeignClient;
import com.mall4j.cloud.api.feign.SearchOrderFeignClient;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.order.bo.*;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.constant.SQBOrderStatus;
import com.mall4j.cloud.api.order.dto.*;
import com.mall4j.cloud.api.order.manager.ConfirmOrderManager;
import com.mall4j.cloud.api.order.vo.*;
import com.mall4j.cloud.api.payment.feign.PayInfoFeignClient;
import com.mall4j.cloud.api.payment.vo.PayInfoFeignVO;
import com.mall4j.cloud.api.payment.bo.SQBOrderPaySuccessBO;
import com.mall4j.cloud.api.payment.feign.PayInfoFeignClient;
import com.mall4j.cloud.api.payment.vo.GetPayInfoByOrderIdsAndPayTypeVO;
import com.mall4j.cloud.api.platform.constant.ChannelTypeEnum;
import com.mall4j.cloud.api.platform.dto.StaffQueryDTO;
import com.mall4j.cloud.api.platform.feign.StaffFeignClient;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.StaffVO;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.platform.vo.TentacleContentVO;
import com.mall4j.cloud.api.platform.vo.TentacleVo;
import com.mall4j.cloud.api.product.bo.PlatformCommissionOrderItemBO;
import com.mall4j.cloud.api.product.feign.CategoryShopFeignClient;
import com.mall4j.cloud.api.product.feign.ShopCartFeignClient;
import com.mall4j.cloud.api.product.feign.SkuFeignClient;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.api.product.manager.ShopCartAdapter;
import com.mall4j.cloud.api.user.bo.UserScoreBO;
import com.mall4j.cloud.api.user.dto.DistributionUserQueryDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.cache.constant.UserCacheNames;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.constant.SendTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.i18n.I18nMessage;
import com.mall4j.cloud.common.i18n.LanguageEnum;
import com.mall4j.cloud.common.order.bo.DeliveryModeBO;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.order.constant.DeliveryType;
import com.mall4j.cloud.common.order.constant.OrderSource;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.order.dto.OrderInvoiceDTO;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuAndSkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.*;
import com.mall4j.cloud.common.util.csvExport.CsvExportModel;
import com.mall4j.cloud.common.util.csvExport.ExcelExportDataLabel;
import com.mall4j.cloud.order.bo.SubmitOrderPayAmountInfoBO;
import com.mall4j.cloud.order.config.OrderCancelConfigProperties;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.dto.app.DistributionOrderSearchDTO;
import com.mall4j.cloud.order.dto.app.DistributionRankingDTO;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.dto.multishop.OrderAdminDTO;
import com.mall4j.cloud.order.dto.multishop.OrderItemDTO;
import com.mall4j.cloud.order.dto.platform.request.SyncSharerInfoRequest;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.mapper.OrderSettlementMapper;
import com.mall4j.cloud.order.model.*;
import com.mall4j.cloud.order.service.*;
import com.mall4j.cloud.order.utils.ZhlsApiUtil;
import com.mall4j.cloud.order.vo.*;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderSettlementMapper orderSettlementMapper;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemLangService orderItemLangService;
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private OrderRefundMapper orderRefundMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private CategoryShopFeignClient categoryShopFeignClient;
    @Autowired
    private DeliveryFeignClient deliveryFeignClient;
    @Autowired
    private DeliveryCompanyFeignClient deliveryCompanyFeignClient;
    @Autowired
    private OrderAddrService orderAddrService;
    @Autowired
    private OrderSettlementService orderSettlementService;
    @Autowired
    private ShopCartFeignClient shopCartFeignClient;
    @Autowired
    private SearchOrderFeignClient searchOrderFeignClient;
    @Autowired
    private OnsMQTemplate stockMqTemplate;
    @Autowired
    private OnsMQTemplate couponMqTemplate;
    @Autowired
    private OnsMQTemplate orderCancelTemplate;
    @Autowired
    private OnsMQTemplate sendNotifyToUserTemplate;
    @Autowired
    private OnsMQTemplate groupOrderCancelMqTemplate;
    @Autowired
    private OnsMQTemplate seckillOrderCancelMqTemplate;
    @Autowired
    private OnsMQTemplate userScoreTemplate;
    @Autowired
    private OrderInvoiceService orderInvoiceService;

    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private StaffFeignClient staffFeignClient;

    @Autowired
    private StoreFeignClient storeFeignClient;

    @Autowired
    private CrmOrderFeignClient crmOrderFeignClient;

    @Autowired
    private ThreadPoolExecutor orderThreadPoolExecutor;

    @Autowired
    private ConfirmOrderManager confirmOrderManager;

    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private SkuFeignClient skuFeignClient;

    @Autowired
    private ShopCartAdapter shopCartAdapter;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ZhlsApiUtil zhlsApiUtil;
    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    OnsMQTemplate orderNotifyDistributionTemplate;
    @Autowired
    OnsMQTemplate stdOrderNotifyTemplate;
    @Autowired
    SegmentFeignClient segmentFeignClient;
    @Autowired
    OnsMQTemplate ecOrderNotifyStockTemplate;
    @Autowired
    ChannlesFeignClient channlesFeignClient;
    @Autowired
    private PayInfoFeignClient payInfoFeignClient;
    @Autowired
    private LitePosApiFeignClient litePosApiFeignClient;
    @Autowired
    private SQBParams sqbParams;
    @Autowired
    OnsMQTemplate ecOrderRebuildDistributionTemplate;
    @Autowired
    private OrderCancelConfigProperties orderCancelConfigProperties;


    @Autowired
    private DistributionJointVentureCommissionFeignClient jointVentureCommissionFeignClient;

    @Override
    public Order getByOrderId(Long orderId) {
        return orderMapper.getByOrderId(orderId);
    }

    @Override
    public Order getByOrderNumber(String orderNumber) {
        return orderMapper.getByOrderNumber(orderNumber);
    }

    @Override
    public void update(Order order) {
        orderMapper.update(order);
    }

    @Override
    public void deleteById(Long orderId) {
        orderMapper.deleteById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> submit(ShopCartOrderMergerVO mergerOrder) {
        //如果没有设置订单来源，默认为普通订单。
        if(mergerOrder.getOrderSource() == null){
            mergerOrder.setOrderSource(OrderSource.NORMAL.value());
        }
        Long userId = mergerOrder.getUserId();
        List<Long> orderIds = new ArrayList<>();
        // 订单商品参数
        List<ShopCartOrderVO> shopCartOrders = mergerOrder.getShopCartOrders();

        List<OrderSettlement> orderSettlements = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        List<OrderItem> orderItems = new ArrayList<>();
        List<Long> shopCartItemIds = new ArrayList<>();
        OrderAddr orderAddr = mapperFacade.map(mergerOrder.getUserAddr(), OrderAddr.class);
        // 快递和同城配送的地址信息
        if (Objects.equals(mergerOrder.getDvyType(), DeliveryType.DELIVERY.value()) && Objects.isNull(orderAddr)) {
            // 请填写收货地址
            throw new LuckException("请填写收货地址");
        }
        // 保存收货地址
        orderAddrService.save(orderAddr);

        // 每个店铺生成一个订单
        for (ShopCartOrderVO shopCartOrderDto : shopCartOrders) {
            Order order = getOrder(userId, mergerOrder.getDvyType(), mergerOrder.getTentacleNo(), shopCartOrderDto, mergerOrder.getStoreId(),mergerOrder.getWechatOrderId(),shopCartOrderDto.getOrderNumber());
            order.setOrderSource(mergerOrder.getOrderSource());
            order.setSourceId(mergerOrder.getSourceId());
            order.setBuyStaffId(mergerOrder.getBuyStaffId());
            order.setPayType(mergerOrder.getPayType());
            if (null != order.getBuyStaffId()) {
                order.setDistributionRelation(4);
            }
            //保存发票信息，每个店铺一个
            if (CollectionUtil.isNotEmpty(mergerOrder.getOrderInvoiceList())) {
                for (OrderInvoiceDTO orderInvoiceDTO : mergerOrder.getOrderInvoiceList()) {
                    if (Objects.equals(order.getShopId(), orderInvoiceDTO.getShopId())) {
                        OrderInvoice orderInvoice = mapperFacade.map(orderInvoiceDTO, OrderInvoice.class);
                        orderInvoice.setInvoiceState(1);
                        orderInvoice.setInvoiceType(1);
                        orderInvoice.setInvoiceContext(1);
                        orderInvoice.setApplicationTime(new Date());
                        orderInvoice.setOrderId(order.getOrderId());
                        orderInvoice.setShopId(order.getShopId());
                        orderInvoiceService.save(orderInvoice);
                    }
                }
            }
            // 插入订单结算表
            OrderSettlement orderSettlement = new OrderSettlement();
            orderSettlement.setIsClearing(0);
            orderSettlement.setOrderId(order.getOrderId());
            orderSettlement.setOrderNumber(order.getOrderNumber());
            orderSettlement.setPayAmount(order.getActualTotal());
            orderSettlement.setVersion(0);
            //如果用使用积分，结算表将积分价格插入
            if (mergerOrder.getIsScorePay() != null && mergerOrder.getIsScorePay() == 1) {
                orderSettlement.setPayScore(shopCartOrderDto.getUseScore());
            } else {
                orderSettlement.setPayScore(0L);
            }
            orderSettlements.add(orderSettlement);

            List<ShopCartItemDiscountVO> shopCartItemDiscounts = shopCartOrderDto.getShopCartItemDiscounts();
            for (ShopCartItemDiscountVO shopCartItemDiscount : shopCartItemDiscounts) {
                List<ShopCartItemVO> shopCartItems = shopCartItemDiscount.getShopCartItems();
                for (ShopCartItemVO shopCartItem : shopCartItems) {
                    if (!Objects.equals(shopCartItem.getCartItemId(), 0L)) {
                        shopCartItemIds.add(shopCartItem.getCartItemId());
                    }
                    if (order.getOrderItems() == null) {
                        order.setOrderItems(new ArrayList<>());
                    }
                    OrderItem orderItem = getOrderItem(order, shopCartItem);
                    orderItems.add(orderItem);
                    order.getOrderItems().add(orderItem);
                }
            }
            //设置赠品信息
            List<OrderGiftInfoVO> giftInfoAppVOList = mergerOrder.getGiftInfoAppVOList();
            if (CollectionUtil.isNotEmpty(giftInfoAppVOList)) {
                OrderGiftInfoVO orderGiftInfoVO1 = giftInfoAppVOList.stream().filter(orderGiftInfoVO -> orderGiftInfoVO.getIsChoose() == 1).findFirst().orElse(null);
                if (orderGiftInfoVO1 != null) {
                    OrderItem orderItem = buildOrderItem(order, orderGiftInfoVO1);
                    orderItems.add(orderItem);
                }
            }

            //设置赠品信息
            order.setOrderAddrId(orderAddr.getOrderAddrId());
            order.setTraceId(mergerOrder.getTraceId());
            orders.add(order);
            orderIds.add(order.getOrderId());
        }
        // 计算分账费率(平台佣金)
        calculatePlatformCommission(orders, orderItems);


        orderMapper.saveBatch(orders);

        orderItemService.saveBatch(orderItems);
//        orderItemLangService.saveOrderItemLang(orderItems);
        orderSettlementMapper.saveBatch(orderSettlements);

        // 清空购物车
        shopCartFeignClient.deleteItem(shopCartItemIds);

        // 发送消息，如果三十分钟后没有支付，则取消订单
        SendResult sendResult = orderCancelTemplate.syncSend(orderIds, orderCancelConfigProperties.getLevel());
        //SendResult sendResult = orderCancelTemplate.syncSend(orderIds, RocketMqConstant.CANCEL_ORDER_DELAY_LEVEL);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        zhlsApiUtil.addOrder(orders,"1110");
        return orderIds;
    }

    private OrderItem buildOrderItem(Order order, OrderGiftInfoVO orderGiftInfoVO1) {
        OrderItem orderItem = new OrderItem();
        orderItem.setType(1);
        orderItem.setGiftActivityId(orderGiftInfoVO1.getGiftActivityId().longValue());
        orderItem.setOrderId(order.getOrderId());
        orderItem.setOrderNumber(order.getOrderNumber());
        orderItem.setShopId(order.getShopId());
        orderItem.setSkuId(orderGiftInfoVO1.getSkuId());
        orderItem.setSpuId(orderGiftInfoVO1.getSpuId());
        // TODO 国际化
//        orderItem.setOrderItemLangList(getOrderItemLangList(shopCartItem));
        orderItem.setSkuName(orderGiftInfoVO1.getSkuName());
        orderItem.setSpuName(orderGiftInfoVO1.getSpuName());
        orderItem.setCount(orderGiftInfoVO1.getNum());
        // 待发货数量
        orderItem.setBeDeliveredNum(orderGiftInfoVO1.getNum());
        orderItem.setPic(orderGiftInfoVO1.getImgUrl());
        orderItem.setPrice(0L);
        orderItem.setUserId(order.getUserId());
        orderItem.setSpuTotalAmount(0L);
        orderItem.setIsComm(0);
        orderItem.setShopCartTime(new Date());
        // 分销金额
        orderItem.setDistributionAmount(0L);
        orderItem.setDistributionParentAmount(0L);
        //平台的补贴优惠金额
        orderItem.setPlatformShareReduce(0L);
        // 实际订单项支付金额
        orderItem.setActualTotal(0L);
        // 分摊优惠金额
        orderItem.setShareReduce(0L);
        //推广员卡号
        orderItem.setDistributionUserId(0L);
        // 积分价格（单价）
        orderItem.setScoreFee(0L);
        //使用积分价格
        orderItem.setUseScore(0L);
        orderItem.setScoreAmount(0L);
        orderItem.setMemberAmount(0L);

        orderItem.setPlatformCouponAmount(0L);
        orderItem.setShopCouponAmount(0L);
        orderItem.setDiscountAmount(0L);
        orderItem.setPlatformFreeFreightAmount(0L);
        // 获取积分，确认收货之后才会获取积分
        orderItem.setGainScore(0L);
        // 生成订单之后才会进行订单改价
        orderItem.setShopChangeFreeAmount(0L);
        // 改价之后才会商家免运费，注意：要是改变了规则，这里也要改
        orderItem.setFreeFreightAmount(0L);
        return orderItem;
    }

    /**
     * 计算分摊金额
     */
    private void calculatePlatformCommission(List<Order> orders, List<OrderItem> orderItems) {
        List<PlatformCommissionOrderItemBO> platformCommissionOrderItems = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            PlatformCommissionOrderItemBO platformCommissionOrderItemBO = new PlatformCommissionOrderItemBO();
            platformCommissionOrderItemBO.setShopId(orderItem.getShopId());
            platformCommissionOrderItemBO.setCategoryId(orderItem.getCategoryId());
            platformCommissionOrderItemBO.setSkuId(orderItem.getSkuId());
            platformCommissionOrderItems.add(platformCommissionOrderItemBO);
        }


//        ServerResponseEntity<List<PlatformCommissionOrderItemBO>> listServerResponseEntity = categoryShopFeignClient.calculatePlatformCommission(platformCommissionOrderItems);
//        if (!listServerResponseEntity.isSuccess()) {
//            throw new LuckException(listServerResponseEntity);
//        }
//
//
//        List<PlatformCommissionOrderItemBO> data = listServerResponseEntity.getData();
//        log.info("计算分摊金额，orderItem:{},PlatformCommissionOrderItemBOs:{}", JSONObject.toJSONString(orderItems), JSONObject.toJSONString(data));
        for (OrderItem orderItem : orderItems) {
            orderItem.setRate(0D);
            // 平台佣金
            orderItem.setPlatformCommission(0L);
        }
        Map<Long, List<OrderItem>> orderIdMap = orderItems.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
        for (Order order : orders) {
            Long totalPlatformCommission = 0L;
            List<OrderItem> orderItemList = orderIdMap.get(order.getOrderId());
            for (OrderItem orderItem : orderItemList) {
                totalPlatformCommission += orderItem.getPlatformCommission();
            }
            order.setPlatformCommission(totalPlatformCommission);
        }

    }

    @Override
    public List<OrderStatusBO> getOrdersStatus(List<Long> orderIds) {
        List<OrderStatusBO> orderStatusList = orderMapper.getOrdersStatus(orderIds);
        for (Long orderId : orderIds) {
            boolean hasOrderId = false;
            for (OrderStatusBO orderStatusBO : orderStatusList) {
                if (Objects.equals(orderStatusBO.getOrderId(), orderId)) {
                    hasOrderId = true;
                    break;
                }
            }
            if (!hasOrderId) {
                OrderStatusBO orderStatusBO = new OrderStatusBO();
                orderStatusBO.setOrderId(orderId);
                orderStatusList.add(orderStatusBO);
            }
        }
        return orderStatusList;
    }

    @Override
    public OrderAmountVO getOrdersActualAmount(List<Long> orderIds) {
        return orderMapper.getOrdersActualAmount(orderIds);
    }

    @Override
    public boolean isPay(Long orderId, Long userId) {
        return BooleanUtil.isTrue(orderMapper.isPay(orderId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByToPaySuccess(PayNotifyBO message, List<OrderStatusBO> ordersStatus) {
        if (CollectionUtil.isEmpty(ordersStatus)) {
            log.error("无法找到订单信息，message: + " + Json.toJsonString(message));
            return;
        }
        /**
         * 修改通过支付状态字段来过滤掉已经支付的订单，通过订单状态来判断会出现问题就是，
         * 订单超过半小时未支付会被系统重置为已取消。这时候不能正确的修改到订单的支付状态。
         */
        List<Long> needUpdateOrderIds = ordersStatus.stream()
                .filter(orderStatusBO -> Objects.equals(orderStatusBO.getIsPayed(), 0))
                .map(OrderStatusBO::getOrderId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(needUpdateOrderIds)) {
            log.error("需要更新付款状态的订单为空,PayNotifyBO：{}, List<OrderStatusBO>:{}",Json.toJsonString(message),Json.toJsonString(ordersStatus));
            return;
        }
        Integer orderType = ordersStatus.get(0).getOrderType();
        // 团购订单
        if (Objects.equals(orderType, OrderType.GROUP.value())) {
            // 待成团
            orderMapper.updateByToGroupPaySuccess(needUpdateOrderIds, message.getPayType());
        } else {
            // 待发货
            orderMapper.updateByToPaySuccess(needUpdateOrderIds, message.getPayType());
        }
        orderSettlementMapper.updateToPaySuccess(needUpdateOrderIds, message.getPayId(), message.getPayType());

    }

    @Override
    public List<OrderSimpleAmountInfoBO> getOrdersSimpleAmountInfo(List<Long> orderIds) {


        List<OrderSimpleAmountInfoBO> ordersSimpleAmountInfos = orderMapper.getOrdersSimpleAmountInfo(orderIds);

        List<DistributionAmountWithOrderIdBO> distributionAmountWithOrderIds = orderItemService.sumTotalDistributionAmountByOrderIds(orderIds);

        for (OrderSimpleAmountInfoBO ordersSimpleAmountInfo : ordersSimpleAmountInfos) {
            ordersSimpleAmountInfo.setShopAmount(ordersSimpleAmountInfo.getReduceAmount() - ordersSimpleAmountInfo.getPlatformAmount());
            Long orderId = ordersSimpleAmountInfo.getOrderId();
            ordersSimpleAmountInfo.setDistributionAmount(0L);
            // 订单总分销金额
            for (DistributionAmountWithOrderIdBO distributionAmountWithOrderId : distributionAmountWithOrderIds) {
                if (Objects.equals(distributionAmountWithOrderId.getOrderId(), orderId)) {
                    ordersSimpleAmountInfo.setDistributionAmount(distributionAmountWithOrderId.getDistributionAmount() + distributionAmountWithOrderId.getDistributionParentAmount());
                }
            }
        }

        return ordersSimpleAmountInfos;
    }

    /**
     * 取消订单和mq日志要同时落地
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderAndGetCancelOrderIds(List<Long> orderIds) {
        List<OrderStatusBO> ordersStatus = getOrdersStatus(orderIds);
        List<Long> cancelOrderIds = new ArrayList<>();
        Integer orderType = null;
        for (OrderStatusBO orderStatusBO : ordersStatus) {
            if (orderStatusBO.getStatus() != null
                    && !Objects.equals(orderStatusBO.getStatus(), OrderStatus.CLOSE.value())
                    && !Objects.equals(orderStatusBO.getStatus(), OrderStatus.PAYED.value())
                    && !Objects.equals(orderStatusBO.getStatus(), OrderStatus.WAIT_GROUP.value())) {
                cancelOrderIds.add(orderStatusBO.getOrderId());
                orderType = orderStatusBO.getOrderType();
            }
        }
        if (CollectionUtil.isEmpty(cancelOrderIds)) {
            return;
        }
        
        // 获取订单流水信息，如果订单流水信息不为空,并且支付方式是10。那就调用收钱吧取消订单接口
        GetPayInfoByOrderIdsAndPayTypeVO payInfoVO = payInfoFeignClient.getPayInfoByOrderIdsAndPayType(orderIds, PayType.SQB_LITE_POS.value()).getData();
        log.info("取消订单-获取订单流水信息{}", payInfoVO);
        if(Objects.nonNull(payInfoVO)) {
            // 查询销售类结果判断订单类型，只有收钱吧那边订单状态为【待操作】才能取消订单
            SQBBodyByQueryResult sqbBodyByQueryResult = new SQBBodyByQueryResult();
            // 品牌编号，系统对接前由"收钱吧"分配并提供
            sqbBodyByQueryResult.setBrand_code(sqbParams.getBrand_code());
            // 商户内部使用的门店编号
            sqbBodyByQueryResult.setStore_sn(sqbParams.getStore_sn());
            // 门店收银机编号，如果没有请传入"0"
            sqbBodyByQueryResult.setWorkstation_sn("0");
            // 商户订单号，在商户系统中唯一
            sqbBodyByQueryResult.setCheck_sn(payInfoVO.getOrderNumber());
            //sqbBodyByQueryResult.setOrderSn();
            ServerResponseEntity<SQBQueryResultResp> queryResultRespResponse = litePosApiFeignClient.queryResult(sqbBodyByQueryResult);
        
            if (queryResultRespResponse.isFail()) {
                log.error("订单编号："+payInfoVO.getOrderNumber()+","+queryResultRespResponse.getMsg());
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            SQBQueryResultResp sqbQueryResultResp = queryResultRespResponse.getData();
            log.info("查询收钱吧订单信息：{}", JSONObject.toJSONString(sqbQueryResultResp));
            
            // 订单状态0：已取消，1：待操作，2：操作中，3：等待结果中，4：操作完成，5：部分完成，6：操作失败，7：已终止
            if (Objects.nonNull(sqbQueryResultResp) && SQBOrderStatus.WAIT_OPERATION.value().equals(sqbQueryResultResp.getOrderStatus())) {
                // 组装公共参数
                SQBBodyByCancelOrder sqbBodyByCancelOrder = new SQBBodyByCancelOrder();
                // 请求编号，每次请求必须唯一
                sqbBodyByCancelOrder.setRequest_id(RandomUtil.getUniqueKey(true));
                // 品牌编号，系统对接前由"收钱吧"分配并提供
                sqbBodyByCancelOrder.setBrand_code(sqbParams.getBrand_code());
                // 商户内部使用的门店编号
                sqbBodyByCancelOrder.setOriginal_store_sn(sqbParams.getStore_sn());
                // 原始门店收银机编号
                sqbBodyByCancelOrder.setOriginal_workstation_sn("0");
                // 本系统为该订单生成的订单序列号
                // sqbBodyByCancelOrder.setOriginalOrderSn();
                // 原始商户订单号
                sqbBodyByCancelOrder.setOriginal_check_sn(payInfoVO.getOrderNumber());
                ServerResponseEntity<SQBCancelOrderResp> sqbCancelOrderRespServerResponseEntity = litePosApiFeignClient.cancelOrderOperation(sqbBodyByCancelOrder);
                if (sqbCancelOrderRespServerResponseEntity.isFail()) {
                    log.info("调用收钱吧取消订单接口失败,返回参数为:【{}】",sqbCancelOrderRespServerResponseEntity);
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }else if(Objects.nonNull(sqbQueryResultResp) && SQBOrderStatus.CANCELED.value().equals(sqbQueryResultResp.getOrderStatus())){
                // 收钱吧查询订单状态为已取消,不用调用收钱吧取消订单接口
                log.info("收钱吧查询订单状态为已取消,不做任何操作");
            }else if(Objects.nonNull(sqbQueryResultResp) && SQBOrderStatus.WAIT_RESULT.value().equals(sqbQueryResultResp.getOrderStatus())){
                log.info("订单在支付密码操作页面,无法取消订单");
                Assert.faild("订单正在付款中,无法取消");
            }else if(Objects.nonNull(sqbQueryResultResp) && SQBOrderStatus.SUCCESS.value().equals(sqbQueryResultResp.getOrderStatus())){
                // 调用支付成功接口
                List<QueryResultRespTender> tenderList = sqbQueryResultResp.getTenders();
                if( CollectionUtil.isNotEmpty(tenderList) ){
                    QueryResultRespTender tender = tenderList.get(0);
                    if( Objects.nonNull(tender) && "3".equals(tender.getPayStatus()) ){
                        SQBOrderPaySuccessBO orderPaySuccessBO = new SQBOrderPaySuccessBO();
                        orderPaySuccessBO.setOrderNumber(payInfoVO.getOrderNumber());
                        orderPaySuccessBO.setBizPayNo(tender.getTenderSn());
                        orderPaySuccessBO.setCallbackContent("取消接口查询结果:"+ JSON.toJSONString(sqbQueryResultResp));
                        
                        payInfoFeignClient.sqbOrderPaySuccess(orderPaySuccessBO);
                    }
                }
                
                return;
            }else{
                log.info("订单状态不符合取消订单状态");
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }else {
            //不是收钱吧订单就是微信支付的订单,通知微信取消订单,避免超时支付
            cancelOrderCancelPayOrder(orderIds);
        }

        int updateStatus = orderMapper.cancelOrders(cancelOrderIds);
        List<EsOrderBO> esOrderList = getEsOrderList(orderIds);
        List<Order> orders = new ArrayList<>();
        esOrderList.forEach( temp -> {
            Order o = BeanUtil.copyProperties(temp, Order.class);
            orders.add(o);
        });
        zhlsApiUtil.addOrder(orders,"1130");

        // 订单已经支付就不能取消了
        if (updateStatus == 0) {
            throw new LuckException("订单状态已发生改变，请刷新后再试");
        }
        // 团购订单
        if (Objects.equals(orderType, OrderType.GROUP.value())) {
            SendResult sendResult = groupOrderCancelMqTemplate.syncSend(orderIds);
            if (sendResult == null || sendResult.getMessageId() == null) {
                // 消息发不出去就抛异常，发的出去无所谓
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            log.info("订单取消 团购订单 mq ---> success");
            // 秒杀有自己的库存
        } else if (Objects.equals(orderType, OrderType.SECKILL.value())) {
            SendResult sendResult = seckillOrderCancelMqTemplate.syncSend(orderIds.get(0));
            if (sendResult == null || sendResult.getMessageId() == null) {
                // 消息发不出去就抛异常，发的出去无所谓
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            log.info("订单取消 秒杀订单 mq ---> success");
        } else {
            // 解锁库存状态
            SendResult sendResult = stockMqTemplate.syncSend(orderIds);
            if (sendResult == null || sendResult.getMessageId() == null) {
                // 消息发不出去就抛异常，发的出去无所谓
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            log.info("订单取消 解锁库存 mq ---> success");
        }

        if (Objects.equals(orderType, OrderType.ORDINARY.value()) || Objects.equals(orderType, OrderType.DAIKE.value())) {
            // 解锁优惠券状态
            SendResult sendResult = couponMqTemplate.syncSend(orderIds);
            // 为什么不用批量消息，因为rocketmq不支持topic不一样发批量消息
            if (sendResult == null || sendResult.getMessageId() == null) {
                // 消息发不出去就抛异常，发的出去无所谓
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }
        if (Objects.equals(orderType, OrderType.ORDINARY.value()) || Objects.equals(orderType, OrderType.SCORE.value())) {
            // 解锁用户积分状态
            UserScoreBO userScoreBo = new UserScoreBO();
            userScoreBo.setUserId(ordersStatus.get(0).getUserId());
            userScoreBo.setOrderIds(orderIds);
            SendResult sendResult = userScoreTemplate.syncSend(userScoreBo);
            if (sendResult == null || sendResult.getMessageId() == null) {
                // 消息发不出去就抛异常，发的出去无所谓
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
        }

        // todo 如果涉及到用户积分成长值之类的，要考虑下要不要分布式，取消的订单，除了支付成功，不然都不会改变订单状态
    }

    //订单取消之前先取消支付单号。
    private void cancelOrderCancelPayOrder(List<Long> orderIds){
        ServerResponseEntity<List<PayInfoFeignVO>> serverResponse = payInfoFeignClient.getNotPayPayInfoByOrderids(orderIds);
        log.info("查询订单的支付详情，参数：{},查询结果:{}",orderIds,JSONObject.toJSONString(serverResponse));
        if(serverResponse==null || serverResponse.isFail() || serverResponse.getData()==null || CollectionUtil.isEmpty(serverResponse.getData()) ){
            return;
        }

        PayInfoFeignVO payInfoFeignVO =  serverResponse.getData().get(0);
        Order order = orderMapper.getByOrderId(Long.parseLong(payInfoFeignVO.getOrderId()));

        payInfoFeignClient.cancelWechatPayOrder(order.getOrderNumber());
    }

    @Override
    public Order getOrderByOrderIdAndUserId(Long orderId, Long userId) {
        Order order = orderMapper.getOrderByOrderIdAndUserId(orderId, userId);
        if (order == null) {
            // 订单不存在
            throw new LuckException(ResponseEnum.ORDER_NOT_EXIST);
        }
        return order;
    }

    @Override
    public OrderVO getOrderByOrderId(Long orderId) {
        Order order = orderMapper.getOrderByOrderIdAndShopId(orderId, AuthUserContext.get().getTenantId());
        if (order == null) {
            // 订单不存在
            throw new LuckException(ResponseEnum.ORDER_NOT_EXIST);
        }
        return mapperFacade.map(order, OrderVO.class);
    }

    @Override
    public int receiptOrder(Long orderId) {
        return orderMapper.receiptOrder(orderId);
    }

    @Override
    public List<OrderAmountInfoBO> getOrdersAmountInfo(List<Long> orderIds) {


        List<OrderSimpleAmountInfoBO> ordersSimpleAmountInfo = getOrdersSimpleAmountInfo(orderIds);

        List<RefundAmountWithOrderIdBO> refundAmountWithOrderIds = orderRefundMapper.sumRefundSuccessAmountByOrderId(orderIds);

        List<OrderAmountInfoBO> orderAmountInfos = mapperFacade.mapAsList(ordersSimpleAmountInfo, OrderAmountInfoBO.class);

        for (OrderAmountInfoBO orderAmountInfo : orderAmountInfos) {
            Long orderId = orderAmountInfo.getOrderId();
            orderAmountInfo.setRefundSuccessAmount(0L);
            // 订单退款金额
            for (RefundAmountWithOrderIdBO refundAmountWithOrderId : refundAmountWithOrderIds) {
                if (Objects.equals(refundAmountWithOrderId.getOrderId(), orderId)) {
                    orderAmountInfo.setRefundSuccessAmount(refundAmountWithOrderId.getRefundAmount());
                    orderAmountInfo.setPlatformRefundAmount(refundAmountWithOrderId.getPlatformRefundAmount());
                }
            }
        }
        return orderAmountInfos;
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderMapper.deleteOrder(orderId);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
//    @GlobalTransactional(rollbackFor = Exception.class)
    public void delivery(DeliveryOrderDTO deliveryOrderParam) {
        //发货构建参数对象
        log.info("发货修改订单状态，执行参数对象DeliveryOrderDTO:{}",JSONObject.toJSONString(deliveryOrderParam));
        deliveryFeignClient.saveDeliveryInfo(deliveryOrderParam);

        orderItemService.updateByDeliveries(deliveryOrderParam.getSelectOrderItems(), deliveryOrderParam.getDeliveryType());
        // 算下未发货商品数量
        int unDeliveryNum = orderItemService.countUnDeliveryNumByOrderId(deliveryOrderParam.getOrderId());
        if (Objects.equals(deliveryOrderParam.getDeliveryType(), DeliveryType.DELIVERY.value())) {
            String companyName = deliveryCompanyFeignClient.getByDeliveryCompanyByCompanyId(deliveryOrderParam.getDeliveryCompanyId()).getData();
            StringBuilder sb = new StringBuilder();
            for (DeliveryOrderItemDTO selectOrderItem : deliveryOrderParam.getSelectOrderItems()) {
                sb.append(selectOrderItem.getSpuName()).append(StrUtil.COMMA);
            }
            sb.deleteCharAt(sb.length() - 1);
            // 获取订单发货信息
            Order order = orderMapper.getDeliveryInfoByOrderId(deliveryOrderParam.getOrderId());
            // TODO 消息推送-发货提醒,通知其他服务
            SendNotifyBO sendNotifyBO = new SendNotifyBO();
            sendNotifyBO.setUserId(order.getUserId());
            sendNotifyBO.setBizId(order.getOrderId());
            sendNotifyBO.setSpuName(sb.toString());
            sendNotifyBO.setShopId(order.getShopId());
            sendNotifyBO.setMobile(order.getMobile());
            sendNotifyBO.setDvyName(companyName);
            sendNotifyBO.setSendType(SendTypeEnum.DELIVERY.getValue());
            sendNotifyBO.setDvyFlowId(deliveryOrderParam.getDeliveryNo());
            List<SendNotifyBO> sendNotifyList = Collections.singletonList(sendNotifyBO);
            sendNotifyToUserTemplate.syncSend(sendNotifyList);
        }
        //修改为发货状态
        if (Objects.equals(unDeliveryNum, 0)) {
            Date date = new Date();
            Order order = new Order();
            order.setOrderId(deliveryOrderParam.getOrderId());
            order.setStatus(OrderStatus.CONSIGNMENT.value());
            order.setUpdateTime(date);
            order.setDeliveryTime(date);
            //判断是无需物流还是快递发货,如果是无需快递，则判断该订单是否包含有物流的项，若有物流则配送类型为快递配送，没有物流则不变
            if (orderItemService.getDevTypeByOrderId(deliveryOrderParam.getOrderId())) {
                order.setDeliveryType(DeliveryType.DELIVERY.value());
            } else {
                order.setDeliveryType(deliveryOrderParam.getDeliveryType());
            }
            orderMapper.update(order);
            // TODO 全部发货完成的消息通知
//            if(Objects.equals(order.getDvyType(), DeliveryType.DELIVERY.value())){
//                Delivery delivery = deliveryService.getById(deliveryOrder.getDeliveryCompanyId());
//                order.setDvyFlowId(deliveryOrder.getDeliveryNo());
//                dvyName = "";
//                if(delivery.getDvyName() != null){
//                    dvyName = delivery.getDvyName();
//                }
//            }
//            notifyTemplateService.sendNotifyOfDelivery(order,dvyName, SendType.DELIVERY);
        }
        EsOrderBO esOrderBO = this.getEsOrder(deliveryOrderParam.getOrderId());
        //视频号3.0订单发货需要调用视频号发货的接口同步信息
        if(esOrderBO!=null && StrUtil.isNotEmpty(esOrderBO.getTraceId())){
            ServerResponseEntity<UserApiVO> userApiVOResponse = userFeignClient.getUserById(esOrderBO.getUserId());



            UserApiVO userApiVO = userApiVOResponse.getData();
            //组装视频号订单 发货对象
            DeliverySendRequest deliverySendRequest = new DeliverySendRequest();
            List<DeliveryInfo> list = new ArrayList<>();
            DeliveryInfo deliveryInfo = new DeliveryInfo();
            List<ProductInfo> productInfos = new ArrayList<>();
            for (DeliveryOrderItemDTO selectOrderItem : deliveryOrderParam.getSelectOrderItems()) {
                ProductInfo productInfo = new ProductInfo();
                productInfo.setOutProductId(StrUtil.toString(selectOrderItem.getSpuId()));
                productInfo.setOutSkuId(StrUtil.toString(selectOrderItem.getSkuId()));
                productInfo.setProductCnt(selectOrderItem.getChangeNum().longValue());
                productInfos.add(productInfo);
            }
            deliveryInfo.setProductInfoList(productInfos);
            deliveryInfo.setWaybillId(deliveryOrderParam.getDeliveryNo());

            //转换成视频号3.0支持的物流公司
            ServerResponseEntity<String> serverResponse = liveStoreClient.getWechatDeliveryCodeByDeliveryId(deliveryOrderParam.getDeliveryCompanyId());
            if(serverResponse!=null && serverResponse.isSuccess()){
                deliveryInfo.setDeliveryId(serverResponse.getData());
            }
//            deliveryInfo.setDeliveryId(deliveryOrderParam.getDeliveryCompanyCode());
//            if(StrUtil.equals(deliveryOrderParam.getDeliveryCompanyCode(),"YUNDA")){
//                deliveryInfo.setDeliveryId("YD");
//            }
            //转换成物流公司
            deliveryInfo.setDeliveryId(deliveryOrderParam.getDeliveryCompanyCode());
            if(StrUtil.equals(deliveryOrderParam.getDeliveryCompanyCode(),"YUNDA")){
                deliveryInfo.setDeliveryId("YD");
            }
            list.add(deliveryInfo);

            deliverySendRequest.setDeliveryList(list);
            if (Objects.equals(unDeliveryNum, 0)) {
                deliverySendRequest.setFinishAllDelivery(1);
                deliverySendRequest.setShipDoneTime(DateUtil.now());
            }else{
                deliverySendRequest.setFinishAllDelivery(0);
            }
            deliverySendRequest.setOpenid(userApiVO.getOpenId());
            deliverySendRequest.setOutOrderId(esOrderBO.getOrderNumber());
            liveStoreClient.orderDeliverySend(deliverySendRequest);
        }

        //视频号4.0订单 发货
        if(esOrderBO!=null && esOrderBO.getOrderSource()==OrderSource.CHANNELS.value()){
            EcDeliverySendRequest request = new EcDeliverySendRequest();
            request.setOrder_id(esOrderBO.getWechatOrderId());
            request.setOut_order_id(esOrderBO.getOrderId());
            List<EcDeliveryProductInfo> delivery_list = new ArrayList<>();
            EcDeliveryProductInfo deliveryInfo = new EcDeliveryProductInfo();

            //转换成视频号支持的物流公司
            ServerResponseEntity<String> serverResponse = channlesFeignClient.getChannelsDeliveryCodeByDeliveryId(deliveryOrderParam.getDeliveryCompanyId());
            if(serverResponse!=null && serverResponse.isSuccess()){
                deliveryInfo.setDelivery_id(serverResponse.getData());
            }
            deliveryInfo.setWaybill_id(deliveryOrderParam.getDeliveryNo());
            deliveryInfo.setDeliver_type(1);

            List<EcFreightProductInfo> productInfos = new ArrayList<>();
            for (DeliveryOrderItemDTO selectOrderItem : deliveryOrderParam.getSelectOrderItems()) {
                EcFreightProductInfo productInfo = new EcFreightProductInfo();
                //转换成物流公司
                productInfo.setProduct_id(selectOrderItem.getSkuId());
                productInfo.setSku_id(selectOrderItem.getSkuId());
                productInfos.add(productInfo);
            }
            deliveryInfo.setProduct_infos(productInfos);
            delivery_list.add(deliveryInfo);
            request.setDelivery_list(delivery_list);
            channlesFeignClient.deliverysend(request);
        }


        List<Order> orders = new ArrayList<>();
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(deliveryOrderParam.getOrderId());
        List<EsOrderBO> esOrderList = getEsOrderList(orderIds);

        esOrderList.forEach(esOrder -> {
            Order o = BeanUtil.copyProperties(esOrder, Order.class);
            orders.add(o);
        });
        zhlsApiUtil.addOrder(orders,"1160");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeAmount(OrderAdminDTO orderAdminDTO) {
        Order dbOrder = orderMapper.getOrderAndOrderItemData(orderAdminDTO.getOrderId(), AuthUserContext.get().getTenantId());
        if (!Objects.equals(dbOrder.getShopId(), AuthUserContext.get().getTenantId())) {
            throw new LuckException(ResponseEnum.UNAUTHORIZED);
        }
        if (!Objects.equals(dbOrder.getStatus(), OrderStatus.UNPAY.value())) {
            throw new LuckException("订单状态已发生改变，无法更改订单金额");
        }
        //减少金额
        Long amount = 0L;
        //平台优惠金额
        Long platformAmount = 0L;
        Map<Long, OrderItemDTO> collect = orderAdminDTO.getOrderItems().stream().collect(Collectors.toMap(OrderItemDTO::getOrderItemId, orderItem -> orderItem));
        for (OrderItem orderItemDb : dbOrder.getOrderItems()) {
            OrderItemDTO orderItem = collect.get(orderItemDb.getOrderItemId());
            boolean notChange = Objects.isNull(orderItem.getChangeAmount()) ||
                    Objects.equals(orderItem.getChangeAmount(), orderItemDb.getActualTotal()) ||
                    orderItemDb.getShopChangeFreeAmount() > 0;
            if (notChange) {
                continue;
            }
            if (orderItem.getChangeAmount() < Constant.MIN_ORDER_AMOUNT) {
                throw new LuckException("订单金额必须大于0");
            }
            Long changeAmount;
            // 改变金额大于订单金额-仅修改店铺改价优惠金额
            if (orderItem.getChangeAmount() > orderItemDb.getActualTotal()) {
                changeAmount = orderItem.getChangeAmount() - orderItemDb.getActualTotal();
                orderItemDb.setShopChangeFreeAmount(orderItemDb.getShopChangeFreeAmount() + changeAmount);
                //实际金额 = 原实际金额 - 变化金额
                orderItemDb.setActualTotal(orderItem.getChangeAmount());
                amount = amount + changeAmount;
                continue;
            }
            changeAmount = orderItemDb.getActualTotal() - orderItem.getChangeAmount();
            //修改平台优惠金额
            Long playformItemAmount = 0L;
            // 如果改变后金额小于当前金额，则进行修改平台优惠金额
            if (orderItem.getChangeAmount() < orderItemDb.getActualTotal() && orderItemDb.getPlatformShareReduce() > 0) {
                //金额减少比例 = 减少金额  % 总金额
                double proportion = Arith.div(changeAmount, orderItemDb.getSpuTotalAmount(), 6);
                //平台优惠金额减少金额 = 减少比例 * 原平台优惠金额
                playformItemAmount = new Double(Arith.round(Arith.mul(proportion, orderItemDb.getPlatformShareReduce()), 0)).longValue();
                //平台优惠金额 = 原平台优惠金额 - 平台优惠金额减少金额
                orderItemDb.setPlatformShareReduce(orderItemDb.getPlatformShareReduce() - playformItemAmount);
            }
            //实际金额 = 原实际金额 - 变化金额
            orderItemDb.setActualTotal(orderItemDb.getActualTotal() - changeAmount);
            //总优惠金额 = 变化金额 + 原总优惠金额
            orderItemDb.setShareReduce(changeAmount + orderItemDb.getShareReduce());
            // 店铺改价金额为订单项减少的金额
            orderItemDb.setShopChangeFreeAmount(orderItemDb.getShopChangeFreeAmount() - changeAmount);
            amount = amount - changeAmount;
            platformAmount = platformAmount + playformItemAmount;
            // 优惠总额
            dbOrder.setReduceAmount(dbOrder.getReduceAmount() + changeAmount);
        }
        // 重新计算佣金
        calculatePlatformCommission(Collections.singletonList(dbOrder), dbOrder.getOrderItems());
        orderItemService.updateBatch(dbOrder.getOrderItems());
        OrderSettlement orderSettlement = orderSettlementService.getByOrderId(dbOrder.getOrderId());
        // 修改订单信息
        dbOrder.setActualTotal(dbOrder.getActualTotal() + amount);
        dbOrder.setPlatformAmount(dbOrder.getPlatformAmount() - platformAmount);
        // 计算运费变化金额
        Long changeFreightAmount = 0L;
        Long freightAmount = orderAdminDTO.getFreightAmount() + dbOrder.getPlatformFreeFreightAmount();
        if (!Objects.equals(dbOrder.getFreightAmount(), freightAmount)) {
            if (orderAdminDTO.getFreightAmount() < 0) {
                throw new LuckException("运费金额不能小于0");
            }
            changeFreightAmount = freightAmount - dbOrder.getFreightAmount();
            dbOrder.setFreeFreightAmount(dbOrder.getFreeFreightAmount() + changeFreightAmount);
            dbOrder.setFreightAmount(freightAmount);
        }
        dbOrder.setShopChangeFreeAmount(dbOrder.getShopChangeFreeAmount() + amount);
        dbOrder.setActualTotal(dbOrder.getActualTotal() + changeFreightAmount);
        orderSettlement.setPayAmount(dbOrder.getActualTotal());
        orderSettlementService.update(orderSettlement);
        // 修改运费信息
        orderMapper.update(dbOrder);
    }

    @Override
    public SubmitOrderPayAmountInfoBO getSubmitOrderPayAmountInfo(long[] orderIdList) {
        return orderMapper.getSubmitOrderPayAmountInfo(orderIdList);
    }

    @Override
    public EsOrderBO getEsOrder(Long orderId) {
        return orderMapper.getEsOrder(orderId);
    }

    @Override
    public EsOrderBO getEsDistributionOrder(Long orderId) {
        EsOrderBO esOrderBO = orderMapper.getEsOrder(orderId);
        if (esOrderBO != null) {
            buildOrderInfo(esOrderBO);
            esOrderBO.setOrderItemList(orderItemService.listOrderItemAndLangByOrderId(esOrderBO.getOrderId()));
        }
        return esOrderBO;
    }

    @Override
    public EsOrderBO getEsOrderByOrderNumber(String orderNumber) {
        return orderMapper.getEsOrderByOrderNumber(orderNumber);
    }

    @Override
    public EsOrderBO getEsOrderByWeChatOrderId(Long weChatOrderId) {
        return orderMapper.getEsOrderByWeChatOrderId(weChatOrderId);
    }

    @Override
    public List<EsOrderBO> getEsOrderByWeChatOrderIds(List<Long> weChatOrderIds) {
        return orderMapper.getEsOrderByWeChatOrderIds(weChatOrderIds);
    }

    @Override
    public List<EsOrderBO> getEsOrderByOrderIds(List<Long> orderIds) {
        return orderMapper.getEsOrderByOrderIds(orderIds);
    }

    private OrderItem getOrderItem(Order order, ShopCartItemVO shopCartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setType(0);
        orderItem.setOrderId(order.getOrderId());
        orderItem.setOrderNumber(order.getOrderNumber());
        orderItem.setShopId(shopCartItem.getShopId());
        orderItem.setSkuId(shopCartItem.getSkuId());
        orderItem.setSpuId(shopCartItem.getSpuId());
        orderItem.setCategoryId(shopCartItem.getCategoryId());
        // TODO 国际化
//        orderItem.setOrderItemLangList(getOrderItemLangList(shopCartItem));
        orderItem.setSkuName(shopCartItem.getSkuName());
        orderItem.setSpuName(shopCartItem.getSpuName());
        if (shopCartItem.getSkuId() != null && StrUtil.isBlank(shopCartItem.getSkuName())) {
            ServerResponseEntity<SkuVO> sku = skuFeignClient.getById(shopCartItem.getSkuId());
            if (sku != null && sku.isSuccess() && sku.getData() != null) {
                if (StrUtil.isNotBlank(sku.getData().getSkuName())) {
                    shopCartItem.setSkuName(sku.getData().getSkuName());
                }
            }
        }
        if (shopCartItem.getSpuId() != null && StrUtil.isBlank(shopCartItem.getSpuName())) {
            ServerResponseEntity<SpuVO> spu = spuFeignClient.getById(shopCartItem.getSpuId());
            if (spu != null && spu.isSuccess() && spu.getData() != null) {
                if (StrUtil.isNotBlank(spu.getData().getName())) {
                    shopCartItem.setSpuName(spu.getData().getName());
                }
            }
        }
        orderItem.setCount(shopCartItem.getCount());
        // 待发货数量
        orderItem.setBeDeliveredNum(shopCartItem.getCount());
        orderItem.setPic(shopCartItem.getImgUrl());
        orderItem.setPrice(shopCartItem.getSkuPriceFee());
        orderItem.setUserId(order.getUserId());
        orderItem.setSpuTotalAmount(shopCartItem.getTotalAmount());
        orderItem.setIsComm(0);
        orderItem.setShopCartTime(shopCartItem.getCreateTime());
        // 分销金额
        orderItem.setDistributionAmount(0L);
        orderItem.setDistributionParentAmount(0L);
        //平台的补贴优惠金额
        orderItem.setPlatformShareReduce(shopCartItem.getPlatformShareReduce());
        // 实际订单项支付金额
        orderItem.setActualTotal(shopCartItem.getActualTotal());
        // 分摊优惠金额
        orderItem.setShareReduce(shopCartItem.getShareReduce());
        //推广员卡号
        orderItem.setDistributionUserId(shopCartItem.getDistributionUserId());
        // 积分价格（单价）
        orderItem.setScoreFee(shopCartItem.getScoreFee());
        //使用积分价格
        orderItem.setUseScore(shopCartItem.getScorePrice() != null ? shopCartItem.getScorePrice() : 0L);
        orderItem.setScoreAmount(shopCartItem.getScoreReduce() != null ? shopCartItem.getScoreReduce() : 0L);
        orderItem.setMemberAmount(shopCartItem.getLevelReduce() != null ? shopCartItem.getLevelReduce() : 0L);

        orderItem.setPlatformCouponAmount(shopCartItem.getPlatformCouponAmount() != null ? shopCartItem.getPlatformCouponAmount() : 0L);
        orderItem.setShopCouponAmount(shopCartItem.getShopCouponAmount() != null ? shopCartItem.getShopCouponAmount() : 0L);
        orderItem.setDiscountAmount(shopCartItem.getDiscountAmount() != null ? shopCartItem.getDiscountAmount() : 0L);
        orderItem.setPlatformFreeFreightAmount(shopCartItem.getPlatformFreeFreightAmount() != null ? shopCartItem.getPlatformFreeFreightAmount() : 0L);
        // 获取积分，确认收货之后才会获取积分
        orderItem.setGainScore(0L);
        // 生成订单之后才会进行订单改价
        orderItem.setShopChangeFreeAmount(0L);
        // 改价之后才会商家免运费，注意：要是改变了规则，这里也要改
        orderItem.setFreeFreightAmount(0L);
        return orderItem;
    }

    /**
     * 获取订单项的国际化信息
     *
     * @param shopCartItem 购物车项
     * @return 订单项的国际化信息列表
     */
    private List<OrderItemLang> getOrderItemLangList(ShopCartItemVO shopCartItem) {
        List<OrderItemLang> orderItemLangList = new ArrayList<>();
        Map<Integer, String> spuNameMap = shopCartItem.getSpuLangList().stream()
                .filter(orderSpuLangVO -> StrUtil.isNotBlank(orderSpuLangVO.getSpuName()))
                .collect(Collectors.toMap(OrderSpuLangVO::getLang, OrderSpuLangVO::getSpuName));
        Map<Integer, String> skuNameMap = shopCartItem.getSkuLangList().stream()
                .filter(orderSkuLangVO -> StrUtil.isNotBlank(orderSkuLangVO.getSkuName()))
                .collect(Collectors.toMap(OrderSkuLangVO::getLang, OrderSkuLangVO::getSkuName));

        for (LanguageEnum value : LanguageEnum.values()) {
            Integer lang = value.getLang();
            if (!spuNameMap.containsKey(lang) && !skuNameMap.containsKey(lang)) {
                continue;
            }
            OrderItemLang orderItemLang = new OrderItemLang();
            orderItemLang.setLang(value.getLang());
            orderItemLang.setSkuName(LangUtil.getLangValue(skuNameMap, lang));
            orderItemLang.setSpuName(LangUtil.getLangValue(spuNameMap, lang));
            orderItemLangList.add(orderItemLang);
        }
        return orderItemLangList;
    }

    private Order getOrder(Long userId, Integer dvyType, String tentacleNo, ShopCartOrderVO shopCartOrderVo, Long storeId,Long wechatOrderId,String oldOrderNumber) {

        // 订单信息
        Order order = new Order();
        order.setOrderId(shopCartOrderVo.getOrderId());

        if(StrUtil.isNotEmpty(oldOrderNumber)){
            order.setOrderNumber(oldOrderNumber);
        }else{
            String orderNumber = getOrderNumber();
            log.info("生成订单，订单编号: {}", orderNumber);
            order.setOrderNumber(orderNumber);
        }


        order.setWechatOrderId(wechatOrderId);
        order.setTentacleNo(tentacleNo);
        order.setShopId(shopCartOrderVo.getShopId());
        order.setShopName(shopCartOrderVo.getShopName());
        // 用户id
        order.setUserId(userId);
        // 商品总额
        order.setTotal(shopCartOrderVo.getTotal());
        // 实际总额
        order.setActualTotal(shopCartOrderVo.getActualTotal());
        order.setStatus(OrderStatus.UNPAY.value());
        order.setIsPayed(0);
        order.setDeleteStatus(0);
        order.setAllCount(shopCartOrderVo.getTotalCount());
        order.setReduceAmount(shopCartOrderVo.getShopReduce());
        order.setFreightAmount(shopCartOrderVo.getTransfee());
        order.setRemarks(shopCartOrderVo.getRemarks());
        if (Objects.isNull(dvyType)) {
            order.setDeliveryType(DeliveryType.DELIVERY.value());
        }else{
            order.setDeliveryType(dvyType);
        }
        order.setOrderScore(shopCartOrderVo.getUseScore());
        order.setOrderType(shopCartOrderVo.getOrderType() != null ? shopCartOrderVo.getOrderType() : OrderType.ORDINARY.value());
        order.setPlatformAmount(shopCartOrderVo.getPlatformAmount() != null ? shopCartOrderVo.getPlatformAmount() : 0L);
        order.setScoreAmount(shopCartOrderVo.getScoreReduce() != null ? shopCartOrderVo.getScoreReduce() : 0L);
        order.setMemberAmount(shopCartOrderVo.getLevelReduce() != null ? shopCartOrderVo.getLevelReduce() : 0L);
        order.setPlatformCouponAmount(shopCartOrderVo.getPlatformCouponReduce() != null ? shopCartOrderVo.getPlatformCouponReduce() : 0L);
        order.setShopCouponAmount(shopCartOrderVo.getCouponReduce() != null ? shopCartOrderVo.getCouponReduce() : 0L);
        order.setDiscountAmount(shopCartOrderVo.getDiscountReduce() != null ? shopCartOrderVo.getDiscountReduce() : 0L);
        order.setPlatformAmount(shopCartOrderVo.getPlatformAmount() != null ? shopCartOrderVo.getPlatformAmount() : 0L);
        order.setReduceAmount(shopCartOrderVo.getShopReduce() != null ? shopCartOrderVo.getShopReduce() : 0L);
        order.setFreeFreightAmount(shopCartOrderVo.getFreeTransfee() != null ? shopCartOrderVo.getFreeTransfee() : 0L);
        order.setPlatformFreeFreightAmount(shopCartOrderVo.getLevelFreeTransfee() != null ? shopCartOrderVo.getLevelFreeTransfee() : 0L);
        order.setShopChangeFreeAmount(0L);
        order.setIsSettled(0L);
        order.setStoreId(storeId);
        //如果订单类型不为 视频号4.0就在创建的时候构建分销信息， 视频号4.0的订单在支付成功后延迟1分钟计算分销信息。
        if(order.getOrderSource()!=OrderSource.CHANNELS.value()){
            buildDistributionInfo(order);
        }
        return order;
    }

    public String getOrderNumber() {
        String orderNumber = "SKX";
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        //打乱顺序
//        time = cn.hutool.core.util.RandomUtil.randomString(time, 14);

        orderNumber = orderNumber + time + cn.hutool.core.util.RandomUtil.randomNumbers(6);
        return orderNumber;
    }

    /**
     * 视频号的订单在支付的时候才会有分享员信息，这里在支付成功后重新根据触点信息更新订单的分销信息。
     * @param orderId
     * @param tentacleNo
     */
    @Override
    public void reBuildDistributionInfo(Long orderId,String tentacleNo){
        Order order = this.getByOrderId(orderId);
        order.setTentacleNo(tentacleNo);
        buildDistributionInfo(order);
        orderMapper.updateDistributioninfo(order);

        List<Long> orderIds = new ArrayList<>();
        orderIds.add(order.getOrderId());

        // 分销订单有自己的操作，分销订单不包含积分订单
        // 订单成功 ----> 分销分账
        PayNotifyBO message = new PayNotifyBO();
        message.setPayType(PayType.WECHATPAY.value());
        message.setOrderIds(orderIds);

        SendResult sendResult = orderNotifyDistributionTemplate.syncSend(message);
        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    /**
     * 处理分销业绩逻辑
     */
    private void buildDistributionInfo(Order order) {
        order.setDistributionRelation(0);
        order.setDistributionAmount(0L);
        order.setDistributionStatus(0);
        order.setDistributionUserId(0L);
        order.setDistributionUserType(0);
        order.setDistributionStoreId(order.getStoreId());
        order.setDevelopingAmount(0L);
        order.setDevelopingStatus(0);
        order.setDevelopingUserId(0L);
        order.setDevelopingStoreId(0L);
        ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(order.getUserId());
        if (!userData.isSuccess() && null == userData.getData()) {
            throw new LuckException("下单人信息异常");
        }
        UserApiVO user = userData.getData();
        //用户的手机号码不为空，则根据当前下单会员手机号码查询导购表，判断是否为导购自购。
        if(StrUtil.isNotEmpty(user.getUserMobile())){
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffByMobile(user.getUserMobile());
            //导购
            if (staffData.isSuccess() && null != staffData.getData() && staffData.getData().getStatus() == 0) {
                order.setDistributionUserId(staffData.getData().getId());
                order.setDistributionStoreId(staffData.getData().getStoreId());
                order.setDistributionUserType(1);
                order.setDistributionRelation(3);
                return;
            }
        }

        //威客
        if (null != user.getVeekerStatus() && user.getVeekerStatus() == 1) {
            order.setDistributionUserId(user.getUserId());
            order.setDistributionUserType(2);
            order.setDistributionRelation(3);
            //上级
            if (null != user.getStaffId()) {
                ServerResponseEntity<StaffVO> responseEntity = staffFeignClient.getStaffById(user.getStaffId());
                if (responseEntity.isSuccess() && null != responseEntity.getData()) {
                    if (responseEntity.getData().getStatus() == 0){
                        order.setDevelopingUserId(responseEntity.getData().getId());
                        order.setDevelopingStoreId(responseEntity.getData().getStoreId());
                    }
                    order.setDistributionStoreId(responseEntity.getData().getStoreId());
                }
                order.setDistributionRelation(2);
            }
            return;
        }
        if (StringUtils.isNotEmpty(order.getTentacleNo())){
            ServerResponseEntity<TentacleContentVO> responseEntity = tentacleContentFeignClient.findByTentacleNo(order.getTentacleNo());
            if (!responseEntity.isSuccess() || null == responseEntity.getData() || null == responseEntity.getData().getTentacle()) {
                log.info("查询触点信息失败 order:{}", order);
                return;
            }
            order.setDistributionRelation(1);
            TentacleVo tentacleVo = responseEntity.getData().getTentacle();
            if (tentacleVo.getTentacleType() == 1) {
                // TODO 处理门店相关
            } else if (tentacleVo.getTentacleType() == 2) {
                order.setDistributionRelation(2);
                //上级
                if (null != user.getStaffId()) {
                    ServerResponseEntity<StaffVO> userStaff = staffFeignClient.getStaffById(user.getStaffId());
                    if (userStaff.isSuccess() && null != userStaff.getData() && userStaff.getData().getStatus() == 0) {
                        order.setDevelopingUserId(userStaff.getData().getId());
                        order.setDevelopingStoreId(userStaff.getData().getStoreId());
                    }
                }
            } else if (tentacleVo.getTentacleType() == 3) {
                order.setDistributionUserType(2);
                ServerResponseEntity<UserApiVO> witkeyUserData = userFeignClient.getUserData(tentacleVo.getBusinessId());
                if (!witkeyUserData.isSuccess() && null == witkeyUserData.getData() || witkeyUserData.getData().getVeekerStatus() != 1) {
                    return;
                }
                if (null != witkeyUserData.getData().getStaffId()) {
                    ServerResponseEntity<StaffVO> staff = staffFeignClient.getStaffById(witkeyUserData.getData().getStaffId());
                    if (staff.isSuccess() && null != staff.getData()) {
                        if (staff.getData().getStatus() == 0){
                            order.setDevelopingUserId(staff.getData().getId());
                            order.setDevelopingStoreId(staff.getData().getStoreId());
                        }
                        order.setDistributionStoreId(staff.getData().getStoreId());
                    }
                }
                order.setDistributionUserId(tentacleVo.getBusinessId());
            } else if (tentacleVo.getTentacleType() == 4) {
                order.setDistributionUserType(1);
                ServerResponseEntity<StaffVO> staff = staffFeignClient.getStaffById(tentacleVo.getBusinessId());
                if (!staff.isSuccess() || null == staff.getData() || staff.getData().getStatus() == 1) {
                    return;
                }
                order.setDistributionStoreId(staff.getData().getStoreId());
                order.setDistributionUserId(tentacleVo.getBusinessId());
            }
        } else {
            //上级
            if (null != user.getStaffId()) {
                ServerResponseEntity<StaffVO> userStaff = staffFeignClient.getStaffById(user.getStaffId());
                if (userStaff.isSuccess() && null != userStaff.getData()) {
                    if (userStaff.getData().getStatus() == 0){
                        order.setDevelopingUserId(userStaff.getData().getId());
                        order.setDevelopingStoreId(userStaff.getData().getStoreId());
                    }
                    order.setDistributionRelation(2);
                    order.setDistributionStoreId(userStaff.getData().getStoreId());
                }
            }
        }
    }

    @Override
    public Long sumTotalDistributionAmountByOrderItem(List<OrderItem> orderItems) {
        // 订单总分销金额
        long totalDistributionAmount = 0L;
        if (CollectionUtil.isNotEmpty(orderItems)) {
            for (OrderItem orderItem : orderItems) {
                if (Objects.isNull(orderItem.getDistributionUserId())) {
                    continue;
                }
                // 如果改订单项已经退款了的话，分销员的佣金就已经回退了，不需要继续算钱
                if (Objects.equals(orderItem.getRefundStatus(), RefundStatusEnum.SUCCEED.value())) {
                    continue;
                }
                // 分销佣金
                if (orderItem.getDistributionAmount() != null && orderItem.getDistributionAmount() > 0) {
                    totalDistributionAmount = totalDistributionAmount + orderItem.getDistributionAmount();
                }
                // 上级分销佣金
                if (orderItem.getDistributionParentAmount() != null && orderItem.getDistributionParentAmount() > 0) {
                    totalDistributionAmount = totalDistributionAmount + orderItem.getDistributionParentAmount();
                }
            }
        }
        return totalDistributionAmount;
    }

    @Override
    public Order getOrderAndOrderItemData(Long orderId, Long shopId) {
        Order order = orderMapper.getOrderAndOrderItemData(orderId, shopId);
        orderLangHandle(order);
        return order;
    }

    @Override
    public OrderCountVO countNumberOfStatus(Long userId) {
        return orderMapper.countNumberOfStatus(userId);
    }

    @Override
    public PageVO<EsOrderVO> orderPage(OrderSearchDTO orderSearchDTO) {

        //原逻辑手动做分页，根据数据中订单数量判断查询es还是数据库。先注释掉原逻辑
        return getOrderPage(orderSearchDTO);


        // 请求商品名称前后的空格
//        if (!Objects.isNull(orderSearchDTO.getSpuName())) {
//            orderSearchDTO.setSpuName(orderSearchDTO.getSpuName().trim());
//        }
//        PageVO<EsOrderVO> orderPage = new PageVO<>();
//        orderPage.setList(new ArrayList<>());
//        Date beginTime = DateUtil.offsetDay(DateUtil.beginOfDay(new Date()), -Constant.ES_ORDER_DATE).toJdkDate();
//        OrderListCountVO orderCount = orderMapper.countOrderList(orderSearchDTO, beginTime);
//        orderPage.setPages(PageUtil.getPages(orderCount.getAllCount(), orderSearchDTO.getPageSize()));
//        orderPage.setTotal(Long.valueOf(orderCount.getAllCount()));
//        PageAdapter pageAdapter = new PageAdapter(orderSearchDTO.getPageNum(), orderSearchDTO.getPageSize());
//        if (Objects.equals(orderCount.getAllCount(), 0) || orderCount.getAllCount() < pageAdapter.getBegin()) {
//            return orderPage;
//        }
//        int end = pageAdapter.getBegin() + pageAdapter.getSize() < orderCount.getAllCount() ? pageAdapter.getBegin() + pageAdapter.getSize() : orderCount.getAllCount();
//        int begin = pageAdapter.getBegin();
//        int size = orderSearchDTO.getPageSize();
//        // 数据库中查询订单数据
//        if (begin < orderCount.getCount() && end <= orderCount.getCount()) {
//            orderPage.getList().addAll(getOrderList(orderSearchDTO, pageAdapter));
//            return orderPage;
//        } else if (begin < orderCount.getCount() && end > orderCount.getCount()) {
//            size = orderCount.getCount() % size;
//            pageAdapter.setSize(size);
//            Date startTime = orderSearchDTO.getStartTime();
//            orderSearchDTO.setStartTime(beginTime);
//            orderPage.getList().addAll(getOrderList(orderSearchDTO, pageAdapter));
//            orderSearchDTO.setStartTime(startTime);
//        }
//
//        // es中查询订单数据
//        if (orderSearchDTO.getPageSize() > size) {
//            orderSearchDTO.setBegin(0);
//            orderSearchDTO.setPageSize(orderSearchDTO.getPageSize() - size);
//        } else {
//            orderSearchDTO.setBegin(pageAdapter.getBegin() - orderCount.getCount());
//        }
//        orderSearchDTO.setEndTime(beginTime);
////        ServerResponseEntity<List<EsOrderVO>> orderResponse = searchOrderFeignClient.getOrderList(orderSearchDTO);
////        orderPage.getList().addAll(orderResponse.getData());
//        return orderPage;
    }

    @Override
    public PageVO<EsOrderVO> distributionJointVentureOrderPage(DistributionJointVentureOrderSearchDTO searchDTO) {

        Long total = orderMapper.listDistributionJointVentureOrderCount(searchDTO);
        PageVO<EsOrderVO> pageVO = new PageVO<>();

        // 没订单返回空
        if (Objects.equals(total, 0L)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }

        List<EsOrderVO> orderVOList = orderMapper.listDistributionJointVentureOrder(searchDTO, new PageAdapter(searchDTO.getPageNum(), searchDTO.getPageSize()));
        if (orderVOList != null && orderVOList.size() > 0) {
            orderVOList.forEach(order -> {
                List<EsOrderItemVO> orderItems = order.getOrderItems();
                if (orderItems != null && orderItems.size() > 0) {
                    orderItems.forEach(item -> {
                        if (item.getSkuId() != null && StrUtil.isBlank(item.getSkuName())) {
                            ServerResponseEntity<SkuVO> sku = skuFeignClient.getById(item.getSkuId());
                            if (sku != null && sku.isSuccess() && sku.getData() != null) {
                                if (StrUtil.isNotBlank(sku.getData().getSkuName())) {
                                    item.setSkuName(sku.getData().getSkuName());
                                }
                            }
                        }
                        if (item.getSpuId() != null && StrUtil.isBlank(item.getSpuName())) {
                            ServerResponseEntity<SpuVO> spu = spuFeignClient.getById(item.getSpuId());
                            if (spu != null && spu.isSuccess() && spu.getData() != null) {
                                if (StrUtil.isNotBlank(spu.getData().getName())) {
                                    item.setSpuName(spu.getData().getName());
                                }
                            }
                        }
                    });
                }
            });
        }

        pageVO.setList(orderVOList);
        pageVO.setTotal(total);
        pageVO.setPages(PageUtil.getPages(total, searchDTO.getPageSize()));

        return pageVO;
    }

    @Override
    public DistributionJointVentureOrderPreApplyInfoVO countDistributionJointVentureOrderPreApplyInfo(DistributionJointVentureOrderSearchDTO searchDTO) {
//        DistributionJointVentureOrderPreApplyInfoVO applyInfoVO = orderMapper.countDistributionJointVentureOrderPreApplyInfo(searchDTO);
//        if (applyInfoVO == null) {
//            return new DistributionJointVentureOrderPreApplyInfoVO();
//        }
        DistributionJointVentureOrderPreApplyInfoVO applyInfoVO=new DistributionJointVentureOrderPreApplyInfoVO();
        DistributionJointVentureOrderRefundSearchDTO djvors = new DistributionJointVentureOrderRefundSearchDTO();
        djvors.setRefundStartTime(searchDTO.getPayStartTime());
        djvors.setRefundEndTime(searchDTO.getPayEndTime());
        djvors.setStoreIdList(searchDTO.getStoreIdList());
        djvors.setJointVentureCommissionStatus(searchDTO.getJointVentureCommissionStatus());
        djvors.setJointVentureRefundStatus(0);
        DistributionJointVentureOrderPayRefundRespDTO applyInfoVO1 = orderMapper.totalDistributionJointVenturePayRefund(djvors);
        if (applyInfoVO1 == null) {
            return applyInfoVO;
        }
        applyInfoVO.setOrderTurnover(applyInfoVO1.getPayActualTotal()-applyInfoVO1.getFreightAmountTotal()-applyInfoVO1.getRefundAmountTotal());
        applyInfoVO.setTotalOrderTurnover(applyInfoVO1.getPayActualTotal());
        applyInfoVO.setDividedOrderTurnover(applyInfoVO1.getRefundAmountTotal());

        ServerResponseEntity<Long> customerRateResp = jointVentureCommissionFeignClient.getCustomerRateByCustomerId(searchDTO.getJointVentureCommissionCustomerId());
        if (customerRateResp.isFail()) {
            throw new LuckException(customerRateResp.getMsg());
        }
        applyInfoVO.setCommissionRate(customerRateResp.getData());
        double rate = new BigDecimal(Optional.ofNullable(applyInfoVO.getCommissionRate()).orElse(0L)).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        applyInfoVO.setCommissionAmount((long) ((applyInfoVO.getOrderTurnover()) * rate));
        applyInfoVO.setDividedCommissionAmount((long) (applyInfoVO.getDividedOrderTurnover() * rate));
        return applyInfoVO;
    }

    private PageVO<EsOrderVO> getOrderPage(OrderSearchDTO orderSearchDTO) {
//        PageDTO pageDTO = new PageDTO();
//        pageDTO.setPageNum(orderSearchDTO.getPageNum());
//        pageDTO.setPageSize(orderSearchDTO.getPageSize());
//
//        PageVO<EsOrderVO> page = PageUtil.doPage(pageDTO, () -> orderMapper.list(orderSearchDTO));

//        orderSearchDTO.setPageNum(orderSearchDTO.getPageNum()-1);

        Long total = orderMapper.newListCount(orderSearchDTO);
        log.info("订单总数：{}",total);
        PageVO<EsOrderVO> pageVO = new PageVO<>();

        // 没订单返回空
        if (Objects.equals(total, 0L)) {
            pageVO.setList(Collections.emptyList());
            return pageVO;
        }

        List<EsOrderVO> orderVOList = orderMapper.list(orderSearchDTO, new PageAdapter(orderSearchDTO.getPageNum(), orderSearchDTO.getPageSize()));
        log.info("订单当前数1：{}",orderVOList.size());
        if (orderVOList != null && orderVOList.size() > 0) {
            orderVOList.forEach(order -> {
                List<EsOrderItemVO> orderItems = order.getOrderItems();
                if (orderItems != null && orderItems.size() > 0) {
                    orderItems.forEach(item -> {
                        if (item.getSkuId() != null && StrUtil.isBlank(item.getSkuName())) {
                            ServerResponseEntity<SkuVO> sku = skuFeignClient.getById(item.getSkuId());
                            if (sku != null && sku.isSuccess() && sku.getData() != null) {
                                if (StrUtil.isNotBlank(sku.getData().getSkuName())) {
                                    item.setSkuName(sku.getData().getSkuName());
                                }
                            }
                        }
                        if (item.getSpuId() != null && StrUtil.isBlank(item.getSpuName())) {
                            ServerResponseEntity<SpuVO> spu = spuFeignClient.getById(item.getSpuId());
                            if (spu != null && spu.isSuccess() && spu.getData() != null) {
                                if (StrUtil.isNotBlank(spu.getData().getName())) {
                                    item.setSpuName(spu.getData().getName());
                                }
                            }
                        }
                    });
                }
            });
        }
        log.info("订单当前数2：{}",orderVOList.size());
        pageVO.setList(orderVOList);
        pageVO.setTotal(total);
        pageVO.setPages(PageUtil.getPages(total, orderSearchDTO.getPageSize()));

        return pageVO;
    }



//    private List<EsOrderVO> getOrderList(OrderSearchDTO orderSearchDTO, PageAdapter pageAdapter) {
//
//        List<EsOrderVO> list = orderMapper.list(orderSearchDTO, pageAdapter);
//        for (EsOrderVO esOrderVO : list) {
//            OrderLangUtil.esOrderList(esOrderVO.getOrderItems());
//        }
//        return list;
//    }

    @Override
    public int countByOrderId(Long orderId) {
        return orderMapper.countByOrderId(orderId);
    }

    @Override
    public List<UserOrderStatisticVO> countOrderByUserIds(List<Long> userIds) {
        // 统计用户的订单相关信息
        // es一个月内的，直接查数据库(一个月后的数据进行冷备)，一个月之前的数据查es
        // 所以需要将数据库中的订单信息和es中的订单加起来才行完整的订单信息

        // 查询数据库中订单信息

        List<UserOrderStatisticVO> orderStatisticList = new ArrayList<>();
        if(CollUtil.isNotEmpty(userIds)){
            orderStatisticList = orderMapper.countOrderByUserIds(userIds);
        }
        // 暂时无冷备策略，下面的代码不执行，等有了冷备策略后再执行

        // 从es中聚合多个用户的订单信息
//        ServerResponseEntity<List<UserOrderStatisticVO>> orderStatisticResponse = searchOrderFeignClient.countOrderByUserIds(userIds);
//        if (orderStatisticResponse.isSuccess()) {
//            // 将订单数据整合到一起
//            List<UserOrderStatisticVO> data = orderStatisticResponse.getData();
//            if (CollUtil.isEmpty(data)) {
//                return orderStatisticList;
//            }
//            Map<Long, UserOrderStatisticVO> orderStatisticMap = data.stream().collect(Collectors.toMap(UserOrderStatisticVO::getUserId, (k) -> k));
//            for (UserOrderStatisticVO orderStatisticVO : orderStatisticList) {
//                Long userId = orderStatisticVO.getUserId();
//                UserOrderStatisticVO statisticVO = orderStatisticMap.get(userId);
//                if (Objects.isNull(statisticVO)) {
//                    continue;
//                }
//                // 消费金额
//                BigDecimal consAmountEs = statisticVO.getConsAmount();
//                if (Objects.nonNull(consAmountEs)) {
//                    BigDecimal consAmount = orderStatisticVO.getConsAmount();
//                    orderStatisticVO.setActualAmount(consAmount.add(consAmountEs));
//                }
//                // 平均折扣 ，reduceAmount/consTimes
//                BigDecimal reduceAmountEs = statisticVO.getReduceAmount();
//                int consTimes = statisticVO.getConsTimes();
//                if (Objects.nonNull(reduceAmountEs)) {
//                    BigDecimal reduceAmount = orderStatisticVO.getReduceAmount();
//                    consTimes = consTimes + orderStatisticVO.getConsTimes();
//                    if (consTimes > 0) {
//                        BigDecimal averDiscount = reduceAmount.add(reduceAmountEs).divide(new BigDecimal(Integer.toString(consTimes)), 0, BigDecimal.ROUND_HALF_UP);
//                        orderStatisticVO.setAverDiscount(averDiscount);
//                    }
//                    orderStatisticVO.setConsTimes(consTimes);
//                }
//                // 实付金额
//                BigDecimal actualAmountEs = statisticVO.getActualAmount();
//                if (Objects.nonNull(actualAmountEs)) {
//                    orderStatisticVO.setActualAmount(orderStatisticVO.getActualAmount().add(actualAmountEs));
//                }
//                // 最新消费时间, 根据es中order的冷备逻辑，es中存储的是之前的数据
//                // 从数据库中统计的消费时间必定是最新的消费时间
//                // 所以只需要判断，如果数据库中没有最新消费时间，而es中有最新消费时间，就用es中的就好了
//                if (Objects.isNull(orderStatisticVO.getReConsTime()) && Objects.nonNull(statisticVO.getReConsTime())) {
//                    orderStatisticVO.setReConsTime(statisticVO.getReConsTime());
//                }
//            }
//        }

        return orderStatisticList;
    }

    @Override
    public PageVO<OrderVO> pageByUserId(PageDTO pageDTO, String userId) {
        PageVO<OrderVO> orderPage = new PageVO<>();
        int total = orderMapper.countPageOrderByUserId(userId);
        List<OrderVO> list = orderMapper.pageByUserId(new PageAdapter(pageDTO.getPageNum(), pageDTO.getPageSize()), userId);
        orderPage.setPages(PageUtil.getPages(total, pageDTO.getPageSize()));
        orderPage.setTotal(Long.valueOf(total));
        orderPage.setList(list);
        return orderPage;
    }

    @Override
    public List<SendNotifyBO> listByOrderIds(List<Long> orderIds) {
        return orderMapper.listByOrderIds(orderIds);
    }

    @Override
    public List<Long> getOrderUserIdsBySearchDTO(OrderSearchDTO orderSearchDTO) {
        return orderMapper.getOrderUserIdsBySearchDTO(orderSearchDTO);
    }

    @Override
    public List<Long> listUserIdByPurchaseNum(Integer isPayed, Integer deleteStatus, Date startDate, Date endDate, Integer status, Long minNum, Long maxNum) {
        List<Long> userIds = orderMapper.listUserIdByPurchaseNum(isPayed, deleteStatus, startDate, endDate, status, minNum, maxNum);
        return userIds;
    }

    @Override
    public List<Long> listUserIdByAverageActualTotal(Integer isPayed, Integer deleteStatus, Date startDate, Date endDate, Integer status, Long minAmount, Long maxAmount) {
        List<Long> userIds = orderMapper.listUserIdByAverageActualTotal(isPayed, deleteStatus, startDate, endDate, status, minAmount, maxAmount);
        return userIds;
    }

    @Override
    public List<OrderProdEffectRespVO> getProdEffectByDateAndProdIds(List<Long> spuIds, Date startTime, Date endTime) {
        return orderMapper.getProdEffectByDateAndProdIds(spuIds, startTime, endTime);
    }

    @Override
    public List<FlowOrderVO> listFlowOrderByOrderIds(Collection<Long> orderIds) {
        return orderMapper.listFlowOrderByOrderIds(orderIds);
    }

    @Override
    public List<Long> listOrderId(Integer orderStatus, Date startTime) {
        return orderMapper.listOrderId(orderStatus, startTime);
    }

    @Override
    public List<Long> confirmOrderListOrderId(Integer orderStatus, Date startTime) {
        return orderMapper.confirmOrderListOrderId(orderStatus, startTime);
    }

    @Override
    public List<Long> listDistributionOrderId(Integer orderStatus, Date startTime) {
        return orderMapper.listDistributionOrderId(orderStatus, startTime);
    }

    @Override
    public int settledOrder(List<Long> orderIds) {
        return orderMapper.settledOrder(orderIds);
    }

    @Override
    public List<OrderVO> listOrderBySettledTimeAndShopId(Long shopId, Date startTime, Date endTime) {
        return null;
    }

    @Override
    public List<EsOrderBO> pageEsOrder(OrderSearchDTO orderSearchDTO) {
        PageAdapter pageAdapter = new PageAdapter(orderSearchDTO.getPageNum(), orderSearchDTO.getPageSize());
        List<Order> list = orderMapper.listOrder(orderSearchDTO, pageAdapter);
        List<EsOrderBO> orderList = new ArrayList<>();
        for (Order order : list) {
            EsOrderBO esOrderBO = mapperFacade.map(order, EsOrderBO.class);
            esOrderBO.setOrderItems(new ArrayList<>());
            for (OrderItem orderItem : order.getOrderItems()) {
                EsOrderItemBO esOrderItemBO = mapperFacade.map(orderItem, EsOrderItemBO.class);
                esOrderBO.getOrderItems().add(esOrderItemBO);
                for (OrderItemLang orderItemLang : orderItem.getOrderItemLangList()) {
                    if (Objects.equals(orderItemLang.getLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
                        esOrderItemBO.setSpuNameZh(orderItemLang.getSpuName());
                        esOrderItemBO.setSkuNameZh(orderItemLang.getSkuName());
                    } else if (Objects.equals(orderItemLang.getLang(), LanguageEnum.LANGUAGE_EN.getLang())) {
                        esOrderItemBO.setSpuNameEn(orderItemLang.getSpuName());
                        esOrderItemBO.setSkuNameEn(orderItemLang.getSkuName());
                    }
                }
            }
            orderList.add(esOrderBO);
        }
        return orderList;
    }

    @Override
    public Long countOrderNum(OrderSearchDTO orderSearchDTO) {
        Long orderNum = orderMapper.countOrderNum(orderSearchDTO.getStartTime(), orderSearchDTO.getEndTime());
        if (Objects.isNull(orderNum)) {
            orderNum = Constant.ZERO_LONG;
        }
        return orderNum;
    }

    @Override
    public Date minOrderCreateTime() {
        return orderMapper.minOrderCreateTime();
    }

    @Override
    public SendNotifyBO getOrderInfoByOrderId(Long orderId) {
        // 获取订单信息
        EsOrderBO order = orderMapper.getEsOrder(orderId);
        List<OrderItemVO> orderItems = orderItemService.listOrderItemAndLangByOrderId(orderId);
        StringBuilder sb = new StringBuilder();
        for (OrderItemVO selectOrderItem : orderItems) {
            String skuName = Objects.isNull(selectOrderItem.getSkuName()) ? "" : selectOrderItem.getSkuName();
            sb.append(selectOrderItem.getSpuName()).append(skuName).append(StrUtil.COMMA);
        }
        sb.deleteCharAt(sb.length() - 1);

        SendNotifyBO sendNotifyBO = new SendNotifyBO();
        sendNotifyBO.setUserId(order.getUserId());
        sendNotifyBO.setBizId(order.getOrderId());
        sendNotifyBO.setMobile(order.getMobile());
        sendNotifyBO.setSpuName(sb.toString());
        sendNotifyBO.setPrice(PriceUtil.toDecimalPrice(order.getActualTotal()).toString());
        sendNotifyBO.setShopId(order.getShopId());
        return sendNotifyBO;
    }

    @Override
    public void updateRefundStatusBatchById(List<Order> orders) {
        orderMapper.updateRefundStatusBatchById(orders);
    }

    @Override
    public List<EsOrderBO> getEsOrderList(List<Long> orderIds) {
        return orderMapper.getEsOrderList(orderIds);
    }

    @Override
    public void zhlsApiUtilAddOrder(List<Long> orderIds) {
        List<Order> orders = new ArrayList<>();
        List<EsOrderBO> esOrderList = getEsOrderList(orderIds);
        esOrderList.forEach( temp -> {
                Order order = BeanUtil.copyProperties(temp, Order.class);
                orders.add(order);
            });

        zhlsApiUtil.addOrder(orders,"1150");
    }

    @Override
    public void updateDistributionStatusBatchById(List<Long> orderIds, Integer distributionStatus, Date distributionSettleTime, Date distributionWithdrawTime) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return;
        }
        orderMapper.updateDistributionStatusBatchById(orderIds, distributionStatus, distributionSettleTime, distributionWithdrawTime);
    }

    @Override
    public void updateDevelopingStatusBatchById(List<Long> orderIds, Integer developingStatus, Date developingSettleTime, Date developingWithdrawTime) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return;
        }
        orderMapper.updateDevelopingStatusBatchById(orderIds, developingStatus, developingSettleTime, developingWithdrawTime);
    }

    @Override
    public List<DistributionOrderVO> listPageWithDistributionStaff(PageDTO pageDTO, DistributionOrderSearchDTO distributionOrderSearchDTO) {
        return orderMapper.listPageWithDistributionStaff(distributionOrderSearchDTO, new PageAdapter(pageDTO));
    }

    @Override
    public List<DistributionOrderVO> listPageWithDistributionUser(PageDTO pageDTO, DistributionOrderSearchDTO distributionOrderSearchDTO) {
        return orderMapper.listPageWithDistributionUser(distributionOrderSearchDTO, new PageAdapter(pageDTO));
    }

    @Override
    public PageVO<EsOrderBO> pageDistributionOrders(PageDTO pageDTO, DistributionQueryDTO queryDTO) {
        if (buildQueryCondition(queryDTO)) {
            return new PageVO<>();
        }
        if (StrUtil.isNotBlank(queryDTO.getDistributionStaffMobile())) {
            ServerResponseEntity<StaffVO> staff = staffFeignClient.getStaffByMobile(queryDTO.getDistributionStaffMobile());
            if (staff != null && staff.isSuccess() && staff.getData() != null) {
                queryDTO.getDistributionUserIds().add(staff.getData().getId());
            }
        }
        if (StrUtil.isNotBlank(queryDTO.getDistributionUserMobile())) {
            ServerResponseEntity<StaffVO> staff = staffFeignClient.getStaffByMobile(queryDTO.getDistributionUserMobile());
            if (staff != null && staff.isSuccess() && staff.getData() != null) {
                queryDTO.getDistributionUserIds().add(staff.getData().getId());
            }
            ServerResponseEntity<UserApiVO> userByMobile = userFeignClient.getUserByMobile(queryDTO.getDistributionUserMobile());
            if (userByMobile != null && userByMobile.isSuccess() && userByMobile.getData() != null) {
                queryDTO.getDistributionUserIds().add(userByMobile.getData().getUserId());
            }
        }
        if (StrUtil.isNotBlank(queryDTO.getDevelopingUserMobile())) {
            ServerResponseEntity<StaffVO> staff = staffFeignClient.getStaffByMobile(queryDTO.getDevelopingUserMobile());
            if (staff != null && staff.isSuccess() && staff.getData() != null) {
                queryDTO.setDevelopingUserId(staff.getData().getId());
            }
        }
        if (StrUtil.isNotBlank(queryDTO.getDistributionUserNo())) {
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffNo(queryDTO.getDistributionUserNo());
            ServerResponseEntity<List<StaffVO>> byStaffQueryDTO = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (byStaffQueryDTO != null && byStaffQueryDTO.isSuccess() && byStaffQueryDTO.getData() != null && byStaffQueryDTO.getData().size() > 0) {
                byStaffQueryDTO.getData().forEach(staff -> {
                    queryDTO.getDistributionUserIds().add(staff.getId());
                });
            }
        }
        if (StrUtil.isNotBlank(queryDTO.getDevelopingUserNo())) {
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffNo(queryDTO.getDistributionUserNo());
            ServerResponseEntity<List<StaffVO>> byStaffQueryDTO = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (byStaffQueryDTO != null && byStaffQueryDTO.isSuccess() && byStaffQueryDTO.getData() != null && byStaffQueryDTO.getData().size() > 0) {
                byStaffQueryDTO.getData().forEach(staff -> {
                    queryDTO.setDevelopingUserId(staff.getId());
                });
            }
        }
        log.info("执行商品分销订单查询，查询参数:{}",JSONObject.toJSONString(queryDTO));
        PageVO<EsOrderBO> objectPageVO = PageUtil.doPage(pageDTO, () -> orderMapper.listDistributionOrders(queryDTO));
        objectPageVO.getList().forEach(esOrderBO -> {
            buildOrderInfo(esOrderBO);
            esOrderBO.setIsDevelopingOrder(queryDTO.getIsDevelopingOrder());
            esOrderBO.setOrderItemList(orderItemService.listOrderItemAndLangByOrderId(esOrderBO.getOrderId()));
        });
        return objectPageVO;
    }

    private void buildOrderInfo(EsOrderBO orderBO) {
        if (null != orderBO.getStoreId()) {
            StoreVO storeVO = storeFeignClient.findByStoreId(orderBO.getStoreId());
            if (null != storeVO) {
                orderBO.setStoreName(storeVO.getName());
                orderBO.setStoreCode(storeVO.getStoreCode());
            }
        }
        ServerResponseEntity<UserApiVO> buyUserData = userFeignClient.getInsiderUserData(orderBO.getUserId());
        if (buyUserData.isSuccess() && null != buyUserData.getData()) {
            orderBO.setUserName(buyUserData.getData().getNickName());
            orderBO.setUserMobile(buyUserData.getData().getUserMobile());
        }
        if (orderBO.getDistributionUserType() == 1 && orderBO.getDistributionUserId() > 0) {
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(orderBO.getDistributionUserId());
            if (staffData.isSuccess() && null != staffData.getData()) {
                orderBO.setDistributionUserName(staffData.getData().getStaffName());
                orderBO.setDistributionUserMobile(staffData.getData().getMobile());
                orderBO.setDistributionUserNo(staffData.getData().getStaffNo());
            }
        } else if (orderBO.getDistributionUserType() == 2 && orderBO.getDistributionUserId() > 0) {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getInsiderUserData(orderBO.getDistributionUserId());
            if (userData.isSuccess() && null != userData.getData()) {
                orderBO.setDistributionUserName(userData.getData().getNickName());
                orderBO.setDistributionUserMobile(userData.getData().getUserMobile());
            }
        }
        if (null != orderBO.getDevelopingUserId() && orderBO.getDevelopingUserId() > 0) {
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(orderBO.getDevelopingUserId());
            if (staffData.isSuccess() && null != staffData.getData()) {
                orderBO.setDevelopingUserName(staffData.getData().getStaffName());
                orderBO.setDevelopingUserMobile(staffData.getData().getMobile());
                orderBO.setDevelopingUserNo(staffData.getData().getStaffNo());
            }
        }
    }

    @Override
    public List<EsOrderBO> listDistributionOrders(DistributionQueryDTO queryDTO) {
        if (buildQueryCondition(queryDTO)) {
            return new ArrayList<>();
        }
        List<EsOrderBO> esOrderBOS = orderMapper.listDistributionOrders(queryDTO);
        esOrderBOS.forEach(esOrderBO -> {
            esOrderBO.setOrderItemList(orderItemService.listOrderItemAndLangByOrderId(esOrderBO.getOrderId()));
        });
        return esOrderBOS;
    }

    @Override
    public List<EsOrderBO> listDistributionJointVentureOrders(DistributionJointVentureOrderSearchDTO searchDTO) {
        return orderMapper.listDistributionJointVentureOrders(searchDTO);
    }

    @Override
    public List<EsOrderBO> listDistributionJointVentureOrdersV2(DistributionJointVentureOrderSearchDTO searchDTO) {
        return orderMapper.listDistributionJointVentureOrdersV2(searchDTO);
    }
    @Override
    public List<DistributionJointVentureOrderRefundRespDTO> listDistributionJointVentureRefundOrders(DistributionJointVentureOrderRefundSearchDTO searchDTO) {
        return orderMapper.listDistributionJointVentureRefundOrders(searchDTO);
    }


    @Override
    public boolean buildQueryCondition(DistributionQueryDTO queryDTO) {
        List<Long> orderIds = new ArrayList<>();
        List<Long> distributionUserIds = new ArrayList<>();
        List<Integer> distributionUserTypes = new ArrayList<>();
        if (StringUtils.isNotEmpty(queryDTO.getProductName())) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSpuName(queryDTO.getProductName());
            List<OrderItemVO> orderItemVOList = orderItemService.listOrderItems(orderItem);
            if (CollectionUtils.isEmpty(orderItemVOList)) {
                return true;
            }
            orderIds.addAll(orderItemVOList.stream().map(OrderItemVO::getOrderId).distinct().collect(Collectors.toList()));
        }
        if (StringUtils.isNotEmpty(queryDTO.getRefundNo())) {
            List<OrderRefundSimpleVO> list = orderRefundMapper.listOrderIdsByRefundId(queryDTO.getRefundNo());
            if (CollectionUtils.isEmpty(list)) {
                return true;
            } else {
                orderIds.addAll(list.stream().map(OrderRefundSimpleVO::getOrderId).distinct().collect(Collectors.toList()));
            }
        }
        if (StringUtils.isNotEmpty(queryDTO.getBuyMobile())) {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(queryDTO.getBuyMobile());
            if (!userData.isSuccess() || null == userData.getData()) {
                return true;
            } else {
                queryDTO.setBuyUserId(userData.getData().getUserId());
            }
        }
        if (StringUtils.isNotEmpty(queryDTO.getDistributionStaffNo())) {
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffNo(queryDTO.getDistributionStaffNo());
            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (!staffData.isSuccess() || CollectionUtils.isEmpty(staffData.getData())) {
                return true;
            } else {
                distributionUserIds.addAll(staffData.getData().stream().map(StaffVO::getId).collect(Collectors.toList()));
                distributionUserTypes.add(1);
            }
        }
        if (StringUtils.isNotEmpty(queryDTO.getDistributionStaffMobile())) {
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffByMobile(queryDTO.getDistributionStaffMobile());
            if (!staffData.isSuccess() || null == staffData.getData()) {
                return true;
            } else {
                distributionUserIds.add(staffData.getData().getId());
                distributionUserTypes.add(1);
            }
        }
        if (StringUtils.isNotEmpty(queryDTO.getDistributionUserMobile())) {
            ServerResponseEntity<UserApiVO> userData = userFeignClient.getUserByMobile(queryDTO.getDistributionUserMobile());
            if (!userData.isSuccess() || null == userData.getData()) {
                return true;
            } else {
                distributionUserIds.add(userData.getData().getUserId());
                distributionUserTypes.add(2);
            }
        }
        if (StringUtils.isNotEmpty(queryDTO.getDevelopingUserNo())) {
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setStaffNo(queryDTO.getDevelopingUserNo());
            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (!staffData.isSuccess() || CollectionUtils.isEmpty(staffData.getData())) {
                return true;
            } else {
                queryDTO.setDevelopingUserId(staffData.getData().get(0).getId());
            }
        }
        if (StringUtils.isNotEmpty(queryDTO.getDevelopingUserMobile())) {
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffByMobile(queryDTO.getDevelopingUserMobile());
            if (!staffData.isSuccess() || null == staffData.getData()) {
                return true;
            } else {
                queryDTO.setDevelopingUserId(staffData.getData().getId());
            }
        }
        if (CollectionUtils.isNotEmpty(queryDTO.getOrderIds())) {
            orderIds.addAll(queryDTO.getOrderIds());
        }
        queryDTO.setOrderIds(orderIds.stream().distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(queryDTO.getDistributionUserIds())) {
            distributionUserIds.addAll(queryDTO.getDistributionUserIds());
        }
        queryDTO.setDistributionUserIds(distributionUserIds.stream().distinct().collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(queryDTO.getDistributionUserTypes())) {
            queryDTO.setDistributionUserTypes(distributionUserTypes.stream().distinct().collect(Collectors.toList()));
        }
        return false;
    }


    @DS("slave")
    @Override
    public Long distributionSalesStat(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.distributionSalesStat(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Long storeDistributionSalesStat(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.storeDistributionSalesStat(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countDistributionRanking(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionRanking(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countDistributionStoreRanking(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionStoreRanking(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countDistributionOrderNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionOrderNum(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countStoreDistributionOrderNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countStoreDistributionOrderNum(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countDistributionOrderUserNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionOrderUserNum(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countStoreDistributionUserNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countStoreDistributionUserNum(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countStoreDistributionUserNum(DistributionSalesStatDTO distributionSalesStatDTO, DistributionUserQueryDTO queryDTO) {
        return orderMapper.countStoreDistributionWitkeyNum(distributionSalesStatDTO, queryDTO);
    }

    @DS("slave")
    @Override
    public Integer countStoreDistributionOrderUserNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countStoreDistributionOrderUserNum(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countStoreDistributionOrderUserNum(DistributionSalesStatDTO distributionSalesStatDTO, DistributionUserQueryDTO queryDTO) {
        return orderMapper.countStoreOrderUserNum(distributionSalesStatDTO, queryDTO);
    }

    @DS("slave")
    @Override
    public Long countStoreDistributionCommission(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countStoreDistributionCommission(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Long countStoreDistributionOrderUserAmount(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countStoreDistributionOrderUserAmount(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Long countStoreDistributionOrderUserAmount(DistributionSalesStatDTO distributionSalesStatDTO, DistributionUserQueryDTO queryDTO) {
        return orderMapper.countStoreOrderUserAmount(distributionSalesStatDTO, queryDTO);
    }

    @DS("slave")
    @Override
    public Integer countDistributionOrderStaffNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionOrderStaffNum(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Integer countDistributionOrderSpuNum(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionOrderSpuNum(distributionSalesStatDTO);
    }

    @DS("slave")
    @Override
    public Long countDistributionOrderTotalPrice(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionOrderTotalPrice(distributionSalesStatDTO);
    }

    @Override
    public Long countOrderAmountByUserId(Long userId, Date startTime, Date endTime) {
        return orderMapper.countOrderAmountByUserId(userId, startTime, endTime);
    }


    @DS("slave")
    @Override
    public PageVO<DistributionRankingDTO> pageDistributionRanking(PageDTO pageDTO, DistributionSalesStatDTO distributionSalesStatDTO) {
        PageVO<DistributionRankingDTO> objectPageVO = PageUtil.doPage(pageDTO, () -> orderMapper.listDistributionRanking(distributionSalesStatDTO));
        if (CollectionUtils.isEmpty(objectPageVO.getList())) {
            return new PageVO<>();
        }
        objectPageVO.getList().forEach(distributionRankingDTO -> {
            ServerResponseEntity<StaffVO> staffData = staffFeignClient.getStaffById(distributionRankingDTO.getStaffId());
            if (staffData.isSuccess() && null != staffData.getData()) {
                distributionRankingDTO.setStaffName(staffData.getData().getStaffName());
                StoreVO storeVO = storeFeignClient.findByStoreId(staffData.getData().getStoreId());
                if (null != storeVO) {
                    distributionRankingDTO.setStoreId(storeVO.getStoreId());
                    distributionRankingDTO.setStoreName(storeVO.getName());
                }
            }
        });
        return objectPageVO;
    }

    @DS("slave")
    @Override
    public PageVO<DistributionStoreStatisticsVO> pageStoreStatistics(PageDTO pageDTO, DistributionSalesStatDTO distributionSalesStatDTO) {
        PageVO<DistributionStoreStatisticsVO> pageVO = PageUtil.doPage(pageDTO, () -> orderMapper.pageStoreStatistics(distributionSalesStatDTO));
        if (CollectionUtils.isNotEmpty(pageVO.getList())) {
            pageVO.getList().forEach(distributionStoreStatisticsVO -> {
                StoreVO storeVO = storeFeignClient.findByStoreId(distributionStoreStatisticsVO.getStoreId());
                if (null != storeVO) {
                    distributionStoreStatisticsVO.setStoreName(storeVO.getName());
                    distributionStoreStatisticsVO.setStoreCode(storeVO.getStoreCode());
                }
                distributionSalesStatDTO.setDistributionStoreId(distributionStoreStatisticsVO.getStoreId());
                distributionStoreStatisticsVO.setRefundNum(orderRefundMapper.countStoreDistributionRefundNum(distributionSalesStatDTO));
                distributionStoreStatisticsVO.setStaffNum(this.countDistributionOrderStaffNum(distributionSalesStatDTO));
            });
        }
        return pageVO;
    }

    @DS("slave")
    @Override
    public List<DistributionStoreStatisticsVO> storeStatistics(DistributionSalesStatDTO distributionSalesStatDTO) {
        List<DistributionStoreStatisticsVO> list=orderMapper.pageStoreStatistics(distributionSalesStatDTO);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(distributionStoreStatisticsVO -> {
                StoreVO storeVO = storeFeignClient.findByStoreId(distributionStoreStatisticsVO.getStoreId());
                if (null != storeVO) {
                    distributionStoreStatisticsVO.setStoreName(storeVO.getName());
                    distributionStoreStatisticsVO.setStoreCode(storeVO.getStoreCode());
                }
                distributionSalesStatDTO.setDistributionStoreId(distributionStoreStatisticsVO.getStoreId());
                distributionStoreStatisticsVO.setRefundNum(orderRefundMapper.countStoreDistributionRefundNum(distributionSalesStatDTO));
                distributionStoreStatisticsVO.setStaffNum(this.countDistributionOrderStaffNum(distributionSalesStatDTO));
            });
        }
        return list;
    }

    @Override
    public void exportDistributionOrder(DistributionQueryDTO queryDTO, HttpServletResponse response) {
        CsvExportModel model = new CsvExportModel();
        model.setMaxSize(500000);
        model.setPageSize(100);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageNum(1);
//        model.setHeader(LambdaUtils.mapToList(getOrderExportLabels(), l -> new CsvExportLabel(l.getLabelName(), l.getLabelKey())));
        model.setHandler(m -> {
            JSONArray data = getData(queryDTO, pageDTO);
            log.info("export order page:" + m.getPageNo() + "count:" + m.getTotalSize() + ",thisPageCount:" + (data == null ? 0 : data.size()));
            return data;
        });
        //读取并写入数据
//        model.readAndWrite();
//        try {
//            model.outCsvStream(response, new File(model.getResult().get(0)));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public OrderShopVO stdDetailByOrderId(Long orderId) {
        OrderShopVO orderShopDto = new OrderShopVO();
        Order order = this.getByOrderId(orderId);
        if (Objects.nonNull(order.getStoreId())) {
            order.setShopName(Constant.PLATFORM_SHOP_NAME);
        }
        OrderAddr orderAddr = orderAddrService.getByOrderAddrId(order.getOrderAddrId());

        List<OrderItemVO> orderItems = orderItemService.listOrderItemAndLangByOrderId(orderId);

        setOrderShopDto(orderShopDto, order, orderAddr, orderItems);

        return orderShopDto;

    }

    @Override
    public Long countDistributionCommission(DistributionSalesStatDTO distributionSalesStatDTO) {
        return orderMapper.countDistributionCommission(distributionSalesStatDTO);
    }

    /**
     * 预提交接口
     *
     * @param confirmDTO
     * @return
     */
    @Override
    public ShopCartOrderMergerVO confirm(OrderConfirmDTO confirmDTO) throws ExecutionException, InterruptedException {
        // 将要返回给前端的完整的订单信息confirm
        if (Objects.isNull(confirmDTO.getDvyType())) {
            confirmDTO.setDvyType(DeliveryType.DELIVERY.value());
        }
        ShopCartOrderMergerVO shopCartOrderMerger = new ShopCartOrderMergerVO();
        shopCartOrderMerger.setActivityDTO(confirmDTO.getActivityDTO());
        shopCartOrderMerger.setOrderType(OrderType.ONEPRICE);

        //组装shopcartItem
        List<ShopCartItemVO> shopCartItems = buildShopCartItemVoList(confirmDTO.getOrderConfirmItemDTOList(), confirmDTO.getStoreId());
        // 购物车
        List<ShopCartVO> shopCarts = shopCartAdapter.conversionShopCart(shopCartItems);

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();


        // 异步计算运费，运费暂时和优惠券没啥关联，可以与优惠券异步计算，获取用户地址，自提信息
        CompletableFuture<ServerResponseEntity<UserDeliveryInfoVO>> deliveryFuture = CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            return deliveryFeignClient.calculateAndGetDeliverInfo(new CalculateAndGetDeliverInfoDTO(confirmDTO.getAddrId(), confirmDTO.getDvyType(), null, shopCartItems));
        }, orderThreadPoolExecutor);


        // 运费用异步计算，最后要等运费出结果
        ServerResponseEntity<UserDeliveryInfoVO> userDeliveryInfoResponseEntity = deliveryFuture.get();
        if (!userDeliveryInfoResponseEntity.isSuccess()) {
            throw new LuckException("运费计算异常");
        }

        confirmOrderManager.recalculateAmountWhenFinishingCalculateShop(shopCartOrderMerger, shopCarts, userDeliveryInfoResponseEntity.getData());
        // 重新插入spu、sku
        shopCartOrderMerger.setOrderShopReduce(confirmDTO.getOrderReduce());
        shopCartOrderMerger.setStoreId(confirmDTO.getStoreId());
        // 缓存计算新
        confirmOrderManager.cacheCalculatedInfo(confirmDTO.getUserId(), shopCartOrderMerger);
        return shopCartOrderMerger;
    }


    @Override
    public void editPlatformRemark(Long orderId, String remark) {
        orderMapper.editPlatformRemark(orderId, remark);
    }

    @Override
    public String getBorrowLivingRoomId(String openId) {
        log.info("通过openId获取缓存直播间，查询参数：{}",openId);
        if(StrUtil.isEmpty(openId)){
            return "";
        }
        String keys = UserCacheNames.BORROW_LIVING_ROOMG + ":" + openId;
        return redisTemplate.boundValueOps(keys).get();
    }

    @Override
    public JSONObject yesterdayOrderStatistics() {
        String yesterday = DateUtil.yesterday().toDateStr();
        String beginTime = yesterday + " 00:00:00";
        String endTime = yesterday + " 23:59:59";
        YesterdayOrderStatisticsVO orderStatistics = orderMapper.yesterdayOrderStatistics(beginTime,endTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("give_order_amount_sum",orderStatistics.getGiveOrderAmountSum());
        jsonObject.put("give_order_num_sum",orderStatistics.getGiveOrderNumSum());
        jsonObject.put("payment_amount_sum",orderStatistics.getPaymentAmountSum());
        jsonObject.put("payed_num_sum",orderStatistics.getPayedNumSum());
        jsonObject.put("ref_date",DateUtil.parse(beginTime).getTime()+"");
        return jsonObject;
    }

    @Override
    public int syncWechatOrderId(Long orderId, Long wechatOrderId) {
        return orderMapper.syncWechatOrderId(orderId,wechatOrderId);
    }

    @Override
    public int syncWeichatPromotionInfo(SyncWeichatPromotionInfoDTO syncWeichatPromotionInfoDTO) {
        return orderMapper.syncWeichatPromotionInfo(syncWeichatPromotionInfoDTO);
    }

    @Override
    public int syncChannelsSharerInfo(SyncSharerInfoRequest request) {
        return orderMapper.syncChannelsSharerInfo(request);
    }

    @Override
    public Long userConsumeAmount(Long userId, String beginTime, String endTime) {
        return orderMapper.userConsumeAmount(userId, beginTime, endTime);
    }

    @Override
    public int jointVentureCommissionOrderSettled(List<Long> orderIds, Integer jointVentureCommissionStatus) {
        return orderMapper.updateJointVentureCommissionStatusBatchById(orderIds, jointVentureCommissionStatus, new Date(), null);
    }

    @Override
    public CalculateDistributionCommissionResultVO calculateDistributionCommissionByUserId(Long userId) {
        return orderMapper.calculateDistributionCommissionByUserId(userId);
    }

    @Override
    public List<Long> getMatchedOrderIdList(OrderSearchDTO orderSearchDTO) {
        if (buildOrderSearchDTO(orderSearchDTO)) {
            return Collections.emptyList();
        }
        return orderMapper.getMatchedOrderIdList(orderSearchDTO);
    }

    private boolean buildOrderSearchDTO(OrderSearchDTO orderSearchDTO) {
        List<Long> userIds = new ArrayList<>();
        String distributionSearchKey = orderSearchDTO.getDistributionSearchKey();
        if (Objects.equals(orderSearchDTO.getDistributionType(), 1) && StringUtils.isNotEmpty(distributionSearchKey)) {
            StaffQueryDTO staffQueryDTO = new StaffQueryDTO();
            staffQueryDTO.setKeyword(distributionSearchKey);
            ServerResponseEntity<List<StaffVO>> staffData = staffFeignClient.findByStaffQueryDTO(staffQueryDTO);
            if (!staffData.isSuccess() || CollectionUtils.isEmpty(staffData.getData())) {
                return true;
            } else {
                userIds.addAll(staffData.getData().stream().map(StaffVO::getId).collect(Collectors.toList()));
            }
            orderSearchDTO.setDistributionUserIdList(userIds);
        }
        if (Objects.equals(orderSearchDTO.getDistributionType(), 2) && StringUtils.isNotEmpty(distributionSearchKey)) {
            ServerResponseEntity<List<UserApiVO>> userData = userFeignClient.findWeekerByKeyword(distributionSearchKey);
            if (!userData.isSuccess() || null == userData.getData()) {
                return true;
            } else {
                userIds.addAll(userData.getData().stream().map(UserApiVO::getUserId).collect(Collectors.toList()));
            }
            orderSearchDTO.setDevelopingUserIdList(userIds);
        }
        return false;
    }

    private List<ShopCartItemVO> buildShopCartItemVoList(List<OrderConfirmItemDTO> orderConfirmItemDTOList, Long storeId) {
        List<ShopCartItemVO> shopCartItemVOList = new ArrayList<>();
        orderConfirmItemDTOList.forEach(orderConfirmItemDTO -> {
            ServerResponseEntity<SpuAndSkuVO> spuAndSkuResponse = spuFeignClient.getSpuAndSkuById(orderConfirmItemDTO.getSpuId(), orderConfirmItemDTO.getSkuId(), storeId);
            if (spuAndSkuResponse.isSuccess()) {
                SpuAndSkuVO data = spuAndSkuResponse.getData();
                String deliveryMode = data.getSpu().getDeliveryMode();
                ShopCartItemVO shopCartItemVO = new ShopCartItemVO();
                shopCartItemVO.setDeliveryMode(deliveryMode);
                shopCartItemVO.setDeliveryModeBO(JSONObject.parseObject(deliveryMode, DeliveryModeBO.class));
                shopCartItemVO.setImgUrl(data.getSpu().getMainImgUrl());
                shopCartItemVO.setSpuName(data.getSpu().getName());
                shopCartItemVO.setActualTotal(orderConfirmItemDTO.getActualTotal());
                shopCartItemVO.setTotalAmount(orderConfirmItemDTO.getTotalPriceFee());
                shopCartItemVO.setShareReduce(orderConfirmItemDTO.getTotalReducePrice());
                shopCartItemVO.setPriceFee(orderConfirmItemDTO.getPriceFee());
                shopCartItemVO.setShopId(Constant.PLATFORM_SHOP_ID);
                shopCartItemVO.setCount(orderConfirmItemDTO.getCount());
                shopCartItemVO.setScoreFee(data.getSku().getScoreFee());
                shopCartItemVO.setScorePrice(Constant.ZERO_LONG);
                shopCartItemVO.setSpuId(data.getSku().getSpuId());
                shopCartItemVO.setSkuId(data.getSku().getSkuId());
                shopCartItemVO.setPlatformShareReduce(0L);
                shopCartItemVO.setSkuPriceFee(data.getSku().getMarketPriceFee());
                shopCartItemVOList.add(shopCartItemVO);
            }
        });
        return shopCartItemVOList;
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
        orderShopDto.setOrderId(order.getOrderId());
        orderShopDto.setOrderNumber(order.getOrderNumber());
        orderShopDto.setUpdateOrViewDeliveryInfo(updateOrViewDeliveryInfo);
        orderShopDto.setOrderScore(order.getOrderScore());
        orderShopDto.setShopId(order.getShopId());
        orderShopDto.setStoreId(order.getStoreId());
        orderShopDto.setDeliveryType(order.getDeliveryType());
        orderShopDto.setShopName(order.getShopName());
        orderShopDto.setActualTotal(order.getActualTotal());
        orderShopDto.setOrderAddr(mapperFacade.map(orderAddr, OrderAddrVO.class));
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
        orderShopDto.setUserId(order.getUserId());
        orderShopDto.setDistributionUserType(order.getDistributionUserType());
        orderShopDto.setDistributionUserId(order.getDistributionUserId());
        orderShopDto.setDevelopingUserId(order.getDevelopingUserId());
    }


    private JSONArray getData(DistributionQueryDTO queryDTO, PageDTO pageDTO) {
        JSONArray jsonArray = new JSONArray();
        List<EsOrderBO> esOrderBOList = listDistributionOrders(queryDTO);
        if (pageDTO.getPageNum() > 1) {
            return null;
        } else {
            pageDTO.setPageNum(pageDTO.getPageNum() + 1);
        }
        esOrderBOList.forEach(esOrderBO -> {
            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(esOrderBO);
            jsonArray.add(jsonObject);
        });
        return jsonArray;
    }

    private List<ExcelExportDataLabel> getOrderExportLabels() {
        List<ExcelExportDataLabel> labels = new ArrayList<>();
        labels.add(new ExcelExportDataLabel("商品购买数量", "allCount"));
        labels.add(new ExcelExportDataLabel("订单号", "orderId"));
        labels.add(new ExcelExportDataLabel("手机号", "mobile"));
        labels.add(new ExcelExportDataLabel("下单门店", "storeName"));
        return labels;
    }


    /**
     * 转换金额格式
     *
     * @param num
     * @return
     */
    private String conversionPrices(String num) {
        if (StrUtil.isBlank(num)) {
            return num;
        }
        BigDecimal b1 = new BigDecimal(num);
        BigDecimal b2 = new BigDecimal(Constant.PRICE_MAGNIFICATION);
        double price = b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return Double.toString(price);
    }


    /**
     * 订单语言信息处理
     *
     * @param order 订单
     */
    private void orderLangHandle(Order order) {
        Integer lang = I18nMessage.getLang();
        for (OrderItem orderItem : order.getOrderItems()) {
            if (CollUtil.isEmpty(orderItem.getOrderItemLangList())) {
                continue;
            }
            Map<Integer, OrderItemLang> langMap = orderItem.getOrderItemLangList().stream().collect(Collectors.toMap(OrderItemLang::getLang, s -> s));
            OrderItemLang orderItemLang;
            if (langMap.containsKey(lang)) {
                orderItemLang = langMap.get(lang);
            } else {
                orderItemLang = langMap.get(Constant.DEFAULT_LANG);
            }
            if (Objects.isNull(orderItemLang)) {
                continue;
            }
            orderItem.setSpuName(orderItemLang.getSpuName());
            orderItem.setSkuName(orderItemLang.getSkuName());
        }
    }

    @Override
    public int checkIsOrderIntoShops(CheckOrderDTO checkOrderDTO) {
        log.info("判断用户单笔消费金额及是否在指定门店中进行了消费条件:{}", checkOrderDTO);
        AtomicReference<Integer> checkStatus = new AtomicReference<>(0);
        String orderType = checkOrderDTO.getOrderType();
        Long userId = checkOrderDTO.getUserId();
        String startTime = checkOrderDTO.getStartTime();
        String endTime = checkOrderDTO.getEndTime();
        Long orderNum = null;
        if(Objects.nonNull(checkOrderDTO.getOrderNum()) && !checkOrderDTO.getOrderNum().equals(0L)){
            orderNum = checkOrderDTO.getOrderNum();
        }

        // 校验订单指定类型是否存在，如果不存在将不做任何操作
        if (StrUtil.isNotBlank(orderType)) {
            String[] types = orderType.split(",");
            List<OrderSelectVo> orderSelectVos = new ArrayList<>();

            // 组装调用crm查询全渠道订单查询接口参数
            OrderSelectDto orderSelectDto = new OrderSelectDto();
            Integer pageIndex = 0;
            Integer pageSize = 1000;
            DateTime starDate = DateUtil.parseDateTime(startTime);
            DateTime dateTime = DateUtil.offsetDay(starDate, -1);
            String time = DateUtil.formatDateTime(dateTime);
            orderSelectDto.setTime_start(time);
            orderSelectDto.setTime_end(endTime);
            orderSelectDto.setMobile(checkOrderDTO.getPhone());
            Integer totalCount = 0;
            do {
                pageIndex ++;
                orderSelectDto.setPage_index(pageIndex);
                orderSelectDto.setPage_size(pageSize);
                ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> crmPageResultServerResponseEntity = crmOrderFeignClient.orderSelect(orderSelectDto);
                if (crmPageResultServerResponseEntity != null && crmPageResultServerResponseEntity.isSuccess() && crmPageResultServerResponseEntity.getData() != null) {
                    CrmPageResult<List<OrderSelectVo>> data = crmPageResultServerResponseEntity.getData();
                    log.info("调用crm接口查询全渠道订单,{}",data.getJsondata());
                    orderSelectVos.addAll(data.getJsondata());
                    totalCount = Math.toIntExact(data.getTotal_count());
                }
            }while (totalCount > pageIndex * pageSize);

            // 遍历所有订单类型，比对是否有满足单笔消费金额的订单；同时还要计算得出是否有在指定店铺下进行过消费
            //AtomicInteger atomic = new AtomicInteger(orderNum.intValue());
            AtomicReference<Long> finalOrderNum = new AtomicReference<>(orderNum);
            List<Integer> typeIntList = Arrays.stream(types).map(type -> {
                Integer typeInt = Integer.valueOf(type);
                return typeInt;
            }).collect(Collectors.toList());
            AtomicInteger checkNum = new AtomicInteger();
            typeIntList.forEach(type -> {
                log.info("校验消费限制，当前订单类型为,{}",type);
                if (type.equals(ChannelTypeEnum.APP_SHOP.getShopFlag())) {
                    Long orderPrice = 0L;
                    if(Objects.nonNull(finalOrderNum.get())){
                        orderPrice = finalOrderNum.get()*100;
                    }
                    // 比对小程序类型订单价格
                    log.info("开始小程序订单数据校验, 当前订单类型为：{}",type);
                    int isOrderIntoShops = orderMapper.checkIsOrderIntoShops(userId, startTime, endTime, orderPrice, checkOrderDTO.getStoreIdList());
                    log.info("小程序订单数据校验结果,{}",isOrderIntoShops);
                    if(isOrderIntoShops > 0){
                        checkStatus.set(1);
                    }
                }
                log.info("crm全渠道订单数据,{}",JSONObject.toJSONString(orderSelectVos));
                if (CollectionUtil.isEmpty(orderSelectVos)) {
                    return;
                }
                if (type.equals(ChannelTypeEnum.POS_SHOP.getShopFlag())) {
                    // 比对云POS类型订单价格
                    checkNum.addAndGet(checkOrderData(checkOrderDTO, startTime, endTime, finalOrderNum.get(), orderSelectVos, ChannelTypeEnum.POS_SHOP.getShopType()));
                }
                if (type.equals(ChannelTypeEnum.TAO_BAO_SHOP.getShopFlag())) {
                    // 比对淘宝类型订单价格
                    checkNum.addAndGet(checkOrderData(checkOrderDTO, startTime, endTime, finalOrderNum.get(), orderSelectVos, ChannelTypeEnum.TAO_BAO_SHOP.getShopType()));
                }
                if (type.equals(ChannelTypeEnum.WEB_SHOP.getShopFlag())) {
                    // 比对线下类型订单价格
                    checkNum.addAndGet(checkOrderData(checkOrderDTO, startTime, endTime, finalOrderNum.get(), orderSelectVos, ChannelTypeEnum.WEB_SHOP.getShopType()));
                }
                if (type.equals(ChannelTypeEnum.JIN_DONG_SHOP.getShopFlag())) {
                    // 比对京东类型订单价格
                    checkNum.addAndGet(checkOrderData(checkOrderDTO, startTime, endTime, finalOrderNum.get(), orderSelectVos, ChannelTypeEnum.JIN_DONG_SHOP.getShopType()));
                }
                if (type.equals(ChannelTypeEnum.DOU_YIN_SHOP.getShopFlag())) {
                    // 比对抖音类型订单价格
                    checkNum.addAndGet(checkOrderData(checkOrderDTO, startTime, endTime, finalOrderNum.get(), orderSelectVos, ChannelTypeEnum.DOU_YIN_SHOP.getShopType()));
                }
            });
            if(checkNum.get() > 0){
                checkStatus.set(1);
            }
        }
        return checkStatus.get();
    }

    private Integer checkOrderData(CheckOrderDTO checkOrderDTO, String startTime, String endTime, Long orderNum, List<OrderSelectVo> orderSelectVos, String shopType) {
        Long check = 0L;

        if (Objects.nonNull(orderNum) && CollectionUtil.isEmpty(checkOrderDTO.getStoreCodeList())){
            log.info("进入单笔消费校验，当前参数为：{}，所获取的订单开始时间为：{}结束时间为：{},指定的订单金额为：{}", checkOrderDTO, startTime, endTime, orderNum);
            check = orderSelectVos.stream().filter(o -> o.getChannelType().equals(shopType))
                    .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                    .filter(o -> o.getPayment().compareTo(new BigDecimal(orderNum)) >= 0).count();
        }else if (Objects.isNull(orderNum) && CollectionUtil.isNotEmpty(checkOrderDTO.getStoreCodeList())){
            log.info("进入指定门店校验，当前参数为：{}，所获取的订单开始时间为：{}结束时间为：{},指定的订单金额为：{}", checkOrderDTO, startTime, endTime, orderNum);
            check = orderSelectVos.stream().filter(o -> o.getChannelType().equals(shopType))
                    .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                    .filter(o -> checkOrderDTO.getStoreCodeList().contains(o.getShopCode())).count();
        }else if(Objects.nonNull(orderNum) && CollectionUtil.isNotEmpty(checkOrderDTO.getStoreCodeList())){
            log.info("进入满足所有消费校验，当前参数为：{}，所获取的订单开始时间为：{}结束时间为：{},指定的订单金额为：{}", checkOrderDTO, startTime, endTime, orderNum);
            check = orderSelectVos.stream().filter(o -> o.getChannelType().equals(shopType))
                    .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                    .filter(o -> checkOrderDTO.getStoreCodeList().contains(o.getShopCode()))
                    .filter(o -> o.getPayment().compareTo(new BigDecimal(orderNum)) >= 0).count();
        }else {
            log.info("没有满足所有消费校验，当前参数为：{}，所获取的订单开始时间为：{}结束时间为：{},指定的订单金额为：{}", checkOrderDTO, startTime, endTime, orderNum);
        }
        log.info("crm满足条件订单数：{}", check);

        return check.intValue();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createEcOrder(ShopCartOrderMergerVO shopCartOrderMergerVO) {
        // 生成订单编号，订单id。
        for (ShopCartOrderVO shopCartOrder : shopCartOrderMergerVO.getShopCartOrders()) {
            ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_ORDER);
            if (!segmentIdResponse.isSuccess()) {
                throw new LuckException(segmentIdResponse.getMsg());
            }
            Long orderId = segmentIdResponse.getData();
            // 设置订单id
            shopCartOrder.setOrderId(orderId);
            String orderNumber = "SKX";
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            orderNumber = orderNumber + time + cn.hutool.core.util.RandomUtil.randomNumbers(6);
            shopCartOrder.setOrderNumber(orderNumber);
        }

        // 锁定优惠券
        // 提交订单
        List<Long> orderIds = this.submit(shopCartOrderMergerVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ecOrderPay(Long outOrderId,String tentacleNo) {
        // 通过外部id查询视频号订单
        EsOrderBO orderBO = this.getEsOrderByWeChatOrderId(outOrderId);

        List<Long> orderIds = new ArrayList<>();
        orderIds.add(orderBO.getOrderId());

        PayNotifyBO message = new PayNotifyBO();
        message.setPayType(PayType.WECHATPAY.value());
        message.setOrderIds(orderIds);
//        List<OrderStatusBO> ordersStatus = this.getOrdersStatus(message.getOrderIds());

        // 待发货
        orderMapper.updateByToPaySuccess(message.getOrderIds(), message.getPayType());
        orderSettlementMapper.updateToPaySuccess(message.getOrderIds(), message.getPayId(), message.getPayType());
        //视频号4.0订单支付成功，延迟1分钟重算分销信息
        SendResult sendResult = ecOrderRebuildDistributionTemplate.syncSend(orderBO.getOrderId(),5);

        // 分销订单有自己的操作，分销订单不包含积分订单
        // 订单成功 ----> 分销分账
//        sendResult = orderNotifyDistributionTemplate.syncSend(message);
//        if (sendResult == null || sendResult.getMessageId() == null) {
//            throw new LuckException(ResponseEnum.EXCEPTION);
//        }

        //视频号4.0 支付成功，扣减视频号可售卖库存
        sendResult = ecOrderNotifyStockTemplate.syncSend(orderBO.getOrderId());
        if (sendResult == null || sendResult.getMessageId() == null) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        // 支付成功后 推送订单明细至中台
//        sendResult = stdOrderNotifyTemplate.syncSend(orderBO.getOrderId(), RocketMqConstant.ORDER_NOTIFY_STD_TOPIC_TAG);
//        if (sendResult == null || sendResult.getMessageId() == null) {
//            throw new LuckException(ResponseEnum.EXCEPTION);
//        }
    }

    @Override
    public void ecCancelOrder(Long outOrderId) {
        // 通过外部id查询视频号订单
        EsOrderBO orderBO = this.getEsOrderByWeChatOrderId(outOrderId);
        if(orderBO==null){
            Assert.faild("外部订单号不存在。");
        }

        List<Long> orderIds = Arrays.asList(orderBO.getOrderId());
        orderMapper.cancelOrders(orderIds);

        List<Order> orders = new ArrayList<>();
        EsOrderBO esOrderBO = getEsOrder(orderBO.getOrderId());
        Order o = BeanUtil.copyProperties(esOrderBO, Order.class);
        orders.add(o);

        zhlsApiUtil.addOrder(orders,"1130");
    }
}
