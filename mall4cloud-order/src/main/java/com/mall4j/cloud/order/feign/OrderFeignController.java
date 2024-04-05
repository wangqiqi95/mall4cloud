package com.mall4j.cloud.order.feign;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.OrderSelectDto;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmOrderFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.vo.OrderSelectVo;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.order.bo.*;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.dto.*;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.manager.SubmitOrderManager;
import com.mall4j.cloud.api.order.vo.*;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticBO;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticListBO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.api.user.feign.UserFeignClient;
import com.mall4j.cloud.api.user.feign.UserRegisterFeignClient;
import com.mall4j.cloud.api.user.vo.UserApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.order.dto.OrderShopDTO;
import com.mall4j.cloud.common.order.vo.*;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.order.constant.RefundStatusEnum;
import com.mall4j.cloud.order.mapper.OrderItemMapper;
import com.mall4j.cloud.order.mapper.OrderMapper;
import com.mall4j.cloud.order.mapper.OrderRefundMapper;
import com.mall4j.cloud.order.mapper.OrderSettlementMapper;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.service.OrderAnalysisService;
import com.mall4j.cloud.order.service.OrderItemService;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.utils.ZhlsApiUtil;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FrozenWatermelon
 * @date 2020/12/25
 */
@RestController
@Slf4j
public class OrderFeignController implements OrderFeignClient {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CrmOrderFeignClient crmOrderFeignClient;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderAnalysisService orderAnalysisService;
    @Autowired
    UserRegisterFeignClient userRegisterFeignClient;
    @Autowired
    OrderSettlementMapper orderSettlementMapper;

    @Autowired
    private StoreFeignClient storeFeignClient;


    @Value("${mall4cloud.expose.permission:}")
    private Boolean permission;

    @Override
    public ServerResponseEntity<OrderAmountVO> getOrdersAmountAndIfNoCancel(List<Long> orderIds) {
        List<OrderStatusBO> orderStatus = orderService.getOrdersStatus(orderIds);
        if (CollectionUtil.isEmpty(orderStatus)) {
            return ServerResponseEntity.fail(ResponseEnum.ORDER_NOT_EXIST);
        }

        for (OrderStatusBO statusBO : orderStatus) {
            // 订单已关闭
            if (statusBO.getStatus() == null || Objects.equals(statusBO.getStatus(), OrderStatus.CLOSE.value())) {
                return ServerResponseEntity.showFailMsg("订单已关闭");
            }
        }
        OrderAmountVO orderAmountVO = orderService.getOrdersActualAmount(orderIds);
        return ServerResponseEntity.success(orderAmountVO);
    }

    @Override
    public ServerResponseEntity<List<OrderStatusBO>> getOrdersStatus(List<Long> orderIds) {
        List<OrderStatusBO> orderStatusList = orderService.getOrdersStatus(orderIds);
        return ServerResponseEntity.success(orderStatusList);
    }

    @Override
    public ServerResponseEntity<List<OrderSimpleAmountInfoBO>> getOrdersSimpleAmountInfo(List<Long> orderIds) {
        return ServerResponseEntity.success(orderService.getOrdersSimpleAmountInfo(orderIds));
    }

    @Override
    public ServerResponseEntity<List<OrderAmountInfoBO>> getOrdersAmountInfo(List<Long> orderIds) {
        return ServerResponseEntity.success(orderService.getOrdersAmountInfo(orderIds));
    }

    @Override
    public ServerResponseEntity<EsOrderBO> getEsOrder(Long orderId) {
        EsOrderBO esOrderBO = orderService.getEsOrder(orderId);
        return ServerResponseEntity.success(esOrderBO);
    }

    @Override
    public ServerResponseEntity<EsOrderBO> getEsDistributionOrder(Long orderId) {
        EsOrderBO esOrderBO = orderService.getEsDistributionOrder(orderId);
        return ServerResponseEntity.success(esOrderBO);
    }

    @Override
    public ServerResponseEntity<EsOrderBO> getEsOrderByOrderNumber(String orderNumber) {
        EsOrderBO esOrderBO = orderService.getEsOrderByOrderNumber(orderNumber);
        return ServerResponseEntity.success(esOrderBO);
    }

    @Override
    public ServerResponseEntity<EsOrderBO> getEsOrderByWechatOrderId(Long wechatOrderId) {
        EsOrderBO esOrderBO = orderService.getEsOrderByWeChatOrderId(wechatOrderId);
        return ServerResponseEntity.success(esOrderBO);
    }

    @Override
    public ServerResponseEntity<List<Long>> submit(ShopCartOrderMergerVO mergerOrder) {
        return ServerResponseEntity.success(orderService.submit(mergerOrder));
    }

    @Override
    public ServerResponseEntity<List<UserOrderStatisticVO>> countOrderByUserIds(List<Long> userIds) {
        return ServerResponseEntity.success(orderService.countOrderByUserIds(userIds));
    }

    @Override
    public ServerResponseEntity<List<Long>> getOrderUserIdsBySearchDTO(OrderSearchDTO orderSearchDTO) {
        return ServerResponseEntity.success(orderService.getOrderUserIdsBySearchDTO(orderSearchDTO));
    }

    @Override
    public ServerResponseEntity<List<Long>> listUserIdByPurchaseNum(Integer isPayed, Integer deleteStatus, Date startDate, Date endDate, Integer status, Long minNum, Long maxNum) {
        List<Long> userIds = orderService.listUserIdByPurchaseNum(isPayed, deleteStatus, startDate, endDate, status, minNum, maxNum);
        return ServerResponseEntity.success(userIds);
    }

    @Override
    public ServerResponseEntity<List<Long>> listUserIdByAverageActualTotal(Integer isPayed, Integer deleteStatus, Date startDate, Date endDate, Integer status, Long minAmount, Long maxAmount) {
        List<Long> userIds = orderService.listUserIdByAverageActualTotal(isPayed, deleteStatus, startDate, endDate, status, minAmount, maxAmount);
        return ServerResponseEntity.success(userIds);
    }
//
//    @Override
//    public ServerResponseEntity<List<CustomerRetainVO>> getTradeRetained(CustomerRetainedDTO customerRetainedDTO) {
//        List<CustomerRetainVO> list = orderAnalysisService.getTradeRetained(customerRetainedDTO);
//        return ServerResponseEntity.success(list);
//    }

    @Override
    public ServerResponseEntity<List<OrderProdEffectRespVO>> getProdEffectByDateAndProdIds(List<Long> spuIds, Long startTime, Long endTime) {
        return ServerResponseEntity.success(orderService.getProdEffectByDateAndProdIds(spuIds, new Date(startTime), new Date(endTime)));
    }

    @Override
    public ServerResponseEntity<List<CustomerRetainVO>> getTradeRetained(CustomerRetainedDTO customerRetainedDTO) {
        List<CustomerRetainVO> list = orderAnalysisService.getTradeRetained(customerRetainedDTO);
        return ServerResponseEntity.success(list);
    }

    @Override
    public ServerResponseEntity<List<FlowOrderVO>> listFlowOrderByOrderIds(Collection<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        List<FlowOrderVO> orderList = orderService.listFlowOrderByOrderIds(orderIds);
        return ServerResponseEntity.success(orderList);
    }

    @Override
    public ServerResponseEntity<List<UserOrderStatisticBO>> getPaidMemberByParam(MemberReqDTO param) {

        return ServerResponseEntity.success(orderMapper.getPaidMemberByParam(param));
    }

    @Override
    public ServerResponseEntity<UserOrderStatisticListBO> getMemberPayData(MemberReqDTO param) {
        // 老会员成交数据
        List<UserOrderStatisticBO> oldUserOrderStatistic = orderMapper.getMemberPayData(param, 0);
        // 新会员成交数据
        List<UserOrderStatisticBO> newUserOrderStatistic = orderMapper.getMemberPayData(param, 1);
        UserOrderStatisticListBO userOrderStatisticListBO = new UserOrderStatisticListBO();
        userOrderStatisticListBO.setOldUserOrderStatisticList(oldUserOrderStatistic);
        userOrderStatisticListBO.setNewUserOrderStatisticList(newUserOrderStatistic);
        return ServerResponseEntity.success(userOrderStatisticListBO);
    }

    @Override
    public ServerResponseEntity<Void> removeCacheTradeRetained(CustomerRetainedDTO customerRetainedDTO) {
        orderAnalysisService.removeCacheTradeRetained(customerRetainedDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateOrderItemBatchById(List<OrderItemVO> orderItemVOList) {
        List<OrderItem> orderItems = mapperFacade.mapAsList(orderItemVOList, OrderItem.class);
        orderItemMapper.updateBatch(orderItems);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<List<EsOrderBO>> pageEsOrder(OrderSearchDTO orderSearchDTO) {
        return ServerResponseEntity.success(orderService.pageEsOrder(orderSearchDTO));
    }

    @Override
    public ServerResponseEntity<Long> countOrderNum(OrderSearchDTO orderSearchDTO) {
        return ServerResponseEntity.success(orderService.countOrderNum(orderSearchDTO));
    }

    @Override
    public ServerResponseEntity<Date> minOrderCreateTime() {
        return ServerResponseEntity.success(orderService.minOrderCreateTime());
    }

    @Override
    public ServerResponseEntity<SendNotifyBO> getOrderDetailInfo(Long orderId) {
        return ServerResponseEntity.success(orderService.getOrderInfoByOrderId(orderId));
    }

    @Override
    public ServerResponseEntity<Integer> countNormalOrderByOrderIds(List<Long> orderIds) {
        int count;
        if (CollUtil.isEmpty(orderIds)) {
            count = 0;
        } else {
            count = orderMapper.countNormalOrderByOrderIds(orderIds);
        }
        return ServerResponseEntity.success(count);
    }

    @Override
    public ServerResponseEntity<List<SumAmountVO>> listSumDataByUserIds(List<Long> userIds) {
        return ServerResponseEntity.success(orderMapper.listSumDataByUserIds(userIds));
    }

    @Override
    public ServerResponseEntity<Long> hasBuySuccessProd(Long spuId, Long userId) {
        return ServerResponseEntity.success(orderMapper.hasBuySuccessProd(spuId, userId));
    }

    @Override
    public ServerResponseEntity<UserShoppingDataVO> calculateUserInShopData(Long userId) {
        return ServerResponseEntity.success(orderMapper.calculateUserInShopData(userId));
    }

    @Override
    public ServerResponseEntity<List<OrderItemVO>> getOrderItems(List<Long> orderItemIds) {
        return ServerResponseEntity.success(orderItemService.getOrderItems(orderItemIds));
    }

    @Override
    public ServerResponseEntity<List<OrderItemVO>> getOrderItemsByOrderIds(List<Long> orderIds) {
        return ServerResponseEntity.success(orderItemService.getOrderItemsByOrderIds(orderIds));
    }

    @Override
    public ServerResponseEntity<OrderItemVO> getOrderItem(Long orderItemId) {
        return ServerResponseEntity.success(orderItemMapper.getByItemId(orderItemId));
    }

    @Override
    public ServerResponseEntity<List<EsOrderBO>> getEsOrderList(List<Long> orderIds) {
        return ServerResponseEntity.success(orderService.getEsOrderList(orderIds));
    }

    @Override
    public ServerResponseEntity<Long> userConsumeAmount(Long userId, String startTime, String endTime, String orderType) {
        log.info("判断用户消金额查询条件:{},{},{},{}", userId, startTime, endTime, orderType);
        Long orderTotal = 0L;
        if (StrUtil.isNotBlank(orderType)) {
            String[] types = orderType.split(",");
            List<OrderSelectVo> orderSelectVos = new ArrayList<>();
            // 类型大于一种 则需要查询其他渠道类型的订单
//            if (types.length > 1) {
                ServerResponseEntity<UserApiVO> userRep = userFeignClient.getUserById(userId);
                if (userRep.isSuccess()) {
                    OrderSelectDto orderSelectDto = new OrderSelectDto();
                    Integer pageIndex = 0;
                    Integer pageSize = 1000;

                    DateTime starDate = DateUtil.parseDateTime(startTime);
                    DateTime dateTime = DateUtil.offsetDay(starDate, -1);
                    String time = DateUtil.formatDateTime(dateTime);
                    orderSelectDto.setTime_start(time);
                    orderSelectDto.setTime_end(endTime);
                    orderSelectDto.setMobile(userRep.getData().getPhone());
                    Integer totalCount = 0;
                    do {
                        pageIndex ++;
                        orderSelectDto.setPage_index(pageIndex);
                        orderSelectDto.setPage_size(pageSize);
                        ServerResponseEntity<CrmPageResult<List<OrderSelectVo>>> crmPageResultServerResponseEntity = crmOrderFeignClient.orderSelect(orderSelectDto);
                        if (crmPageResultServerResponseEntity != null && crmPageResultServerResponseEntity.isSuccess() && crmPageResultServerResponseEntity.getData() != null) {
                            CrmPageResult<List<OrderSelectVo>> data = crmPageResultServerResponseEntity.getData();
                            log.info("查询全渠道订单,{}",data.getJsondata());
                            orderSelectVos.addAll(data.getJsondata());
                            totalCount = Math.toIntExact(data.getTotal_count());
                        }
                    }while (totalCount > pageIndex * pageSize);

                }
//            }
            for (int i = 0;i < types.length; i++) {
                if (types[i].equals("1")) {
                    // 查询小程序类型订单
                    Long aTotal = orderService.userConsumeAmount(userId, startTime, endTime);
                    orderTotal += aTotal;
                    log.info("查询小程序消费金额：{}，总消费金额:{}",aTotal,orderTotal);
                }
                if (orderSelectVos == null || orderSelectVos.size() < 1) {
                    continue;
                }
                if (types[i].equals("2")) {
                    long total = 0L;
                    // 查询线下
                    total = orderSelectVos.stream().filter(o -> o.getChannelType().equals("线下门店"))
                            .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                            .mapToLong(o -> o.getPayment().multiply(new BigDecimal(100)).longValue()).sum();
                    orderTotal += total;
                    log.info("查询pos消费金额：{}，总消费金额:{}",total,orderTotal);
                }
                if (types[i].equals("3")) {
                    long total = 0L;
                    // 查询线下
                    total = orderSelectVos.stream().filter(o -> o.getChannelType().equals("淘宝"))
                            .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                            .mapToLong(o -> o.getPayment().multiply(new BigDecimal(100)).longValue()).sum();
                    orderTotal += total;
                    log.info("查询TAOBAO消费金额：{}，总消费金额:{}",total,orderTotal);
                }
                if (types[i].equals("4")) {
                    long total = 0L;
                    // 查询线下
                    total = orderSelectVos.stream().filter(o -> o.getChannelType().equals("官网"))
                            .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                            .mapToLong(o -> o.getPayment().multiply(new BigDecimal(100)).longValue()).sum();
                    orderTotal += total;
                    log.info("查询web消费金额：{}，总消费金额:{}",total,orderTotal);
                }
                if (types[i].equals("5")) {
                    long total = 0L;
                    // 查询线下
                    total = orderSelectVos.stream().filter(o -> o.getChannelType().equals("京东"))
                            .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                            .mapToLong(o -> o.getPayment().multiply(new BigDecimal(100)).longValue()).sum();
                    orderTotal += total;
                    log.info("查询JD消费金额：{}，总消费金额:{}",total,orderTotal);
                }
                if (types[i].equals("6")) {
                    long total = 0L;
                    // 查询线下
                    total = orderSelectVos.stream().filter(o -> o.getChannelType().equals("抖音"))
                            .filter(o -> DateUtil.isIn(DateUtil.parseDateTime(o.getPayTime()), DateUtil.parseDateTime(startTime), DateUtil.parseDateTime(endTime)))
                            .mapToLong(o -> o.getPayment().multiply(new BigDecimal(100)).longValue()).sum();
                    orderTotal += total;
                    log.info("查询DOUYIN消费金额：{}，总消费金额:{}",total,orderTotal);
                }
            }
        }

        return ServerResponseEntity.success(orderTotal);
    }

    @Override
    public ServerResponseEntity<Void> zhlsApiUtilAddOrder(List<Long> orderIds) {
        orderService.zhlsApiUtilAddOrder(orderIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Long> sumTotalDistributionAmountByOrderItem(List<EsOrderItemBO> orderItems) {
        // 订单总分销金额
        BigDecimal totalDistributionAmount = new BigDecimal(Constant.ZERO_LONG.toString());
        if (CollectionUtil.isNotEmpty(orderItems)) {
            for (EsOrderItemBO orderItem : orderItems) {
                if (Objects.isNull(orderItem.getDistributionUserId())) {
                    continue;
                }
                // 如果改订单项已经退款了的话，分销员的佣金就已经回退了，不需要继续算钱
                if (Objects.equals(orderItem.getReturnMoneySts(), RefundStatusEnum.SUCCEED.value())) {
                    continue;
                }
                // 分销佣金
                if (orderItem.getDistributionAmount() != null && orderItem.getDistributionAmount() > 0) {
                    totalDistributionAmount = totalDistributionAmount.add(new BigDecimal(orderItem.getDistributionAmount().toString()));
                }
                // 上级分销佣金
                if (orderItem.getDistributionParentAmount() != null && orderItem.getDistributionParentAmount() > 0) {
                    totalDistributionAmount = totalDistributionAmount.add(new BigDecimal(orderItem.getDistributionParentAmount().toString()));
                }
            }
        }
        return ServerResponseEntity.success(totalDistributionAmount.longValue());
    }

    @Override
    public ServerResponseEntity<List<EsOrderBO>> getListBySettledOrOrderIds(Integer settled, List<Long> orderIds) {
        return ServerResponseEntity.success(orderMapper.listBySettledOrOrderIds(settled, orderIds));
    }

    @Override
    public ServerResponseEntity<List<OrderItemVO>> listOrderItemByOrderIds(List<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return ServerResponseEntity.success(new ArrayList<>());
        }
        return ServerResponseEntity.success(orderItemMapper.listOrderItemByOrderIds(orderIds));
    }

    @Override
    public ServerResponseEntity<Boolean> updateOrderItemCommissionBatchById(List<OrderItemVO> orderItemVOList) {
        List<OrderItem> orderItems = mapperFacade.mapAsList(orderItemVOList, OrderItem.class);
        orderItemMapper.updateCommissionBatch(orderItems);
        return ServerResponseEntity.success(true);
    }

    @Override
    public ServerResponseEntity<Void> updateDistributionStatusBatchById(List<Long> orderIds, Integer distributionStatus, Date distributionSettleTime, Date distributionWithdrawTime) {
        orderService.updateDistributionStatusBatchById(orderIds, distributionStatus, distributionSettleTime, distributionWithdrawTime);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateDevelopingStatusBatchById(List<Long> orderIds, Integer developingStatus, Date developingSettleTime, Date developingWithdrawTime) {
        orderService.updateDevelopingStatusBatchById(orderIds, developingStatus, developingSettleTime, developingWithdrawTime);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> updateDistributionRefundStatusBatchByOrderItemId(List<Long> orderItemIds, Integer distributionRefundStatus) {
        orderItemService.updateDistributionRefundStatusBatchByOrderItemId(orderItemIds, distributionRefundStatus);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Integer> countItemByOrderId(Long orderId) {
        return ServerResponseEntity.success(orderItemMapper.countByOrderId(orderId));
    }

    @Override
    public ServerResponseEntity<List<EsOrderBO>> listByStaffAndTime(Long staffId, Date endTime) {
        return ServerResponseEntity.success(orderMapper.listByStaffAndTime(staffId, endTime));
    }

    @Override
    public ServerResponseEntity<List<EsOrderBO>> listDistributionOrders(DistributionQueryDTO queryDTO) {
        return ServerResponseEntity.success(orderService.listDistributionOrders(queryDTO));
    }

    @Override
    public ServerResponseEntity<List<EsOrderBO>> listDistributionJointVentureOrders(DistributionJointVentureOrderSearchDTO searchDTO) {
        return ServerResponseEntity.success(orderService.listDistributionJointVentureOrders(searchDTO));
    }

    @Override
    public ServerResponseEntity<List<EsOrderBO>> listDistributionJointVentureOrdersV2(DistributionJointVentureOrderSearchDTO searchDTO) {
        return ServerResponseEntity.success(orderService.listDistributionJointVentureOrdersV2(searchDTO));
    }

    @Override
    public ServerResponseEntity<List<DistributionJointVentureOrderRefundRespDTO>> listDistributionJointVentureRefundOrders(DistributionJointVentureOrderRefundSearchDTO searchDTO) {
        return ServerResponseEntity.success(orderService.listDistributionJointVentureRefundOrders(searchDTO));
    }

    @Override
    public ServerResponseEntity<Long> countOrderAmountByUserId(Long userId, Date startTime, Date endTime) {
        return ServerResponseEntity.success(orderService.countOrderAmountByUserId(userId, startTime, endTime));
    }

    @Override
    public ServerResponseEntity<List<OrderItemVO>> listOrderItemAndLangByOrderId(Long orderId) {
        return ServerResponseEntity.success(orderItemService.listOrderItemAndLangByOrderId(orderId));
    }

    @Override
    public ServerResponseEntity<OrderShopVO> stdDetail(Long orderId) {
        return ServerResponseEntity.success(orderService.stdDetailByOrderId(orderId));

    }

    @Override
    public ServerResponseEntity<ShopCartOrderMergerVO> confirm(OrderConfirmDTO confirmDTO) throws Exception{
        return ServerResponseEntity.success(orderService.confirm(confirmDTO));
    }

    @Override
    public ServerResponseEntity<Void> stdCancelOrder(List<Long> orderIds) {
        orderService.cancelOrderAndGetCancelOrderIds(orderIds);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> stdOrderConfirm(Long orderId) {
        orderService.receiptOrder(orderId);
        return ServerResponseEntity.success();
    }

    @Transactional
    @Override
    public ServerResponseEntity<Void> jointVentureCommissionOrderSettled(@RequestBody JointVentureCommissionOrderSettledDTO jointVentureCommissionOrderSettledDTO) {
        List<Long> orderIds = jointVentureCommissionOrderSettledDTO.getOrderIds();
        Integer jointVentureCommissionStatus = jointVentureCommissionOrderSettledDTO.getJointVentureCommissionStatus();
        int AffectedRows = orderService.jointVentureCommissionOrderSettled(orderIds, jointVentureCommissionStatus);
        if (orderIds.size() != AffectedRows) {
            throw new LuckException("联营分佣订单结算失败");
        }
        AffectedRows = orderItemService.jointVentureCommissionOrderItemSettled(orderIds, jointVentureCommissionStatus, null);
//        if (AffectedRows <= 0) {
//            throw new LuckException("联营分佣订单结算失败");
//        }
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<OrderItemVO> getOrderItemByOrderNumberAndSkuId(String orderNumber, Long skuId) {
        return ServerResponseEntity.success(orderItemService.getOrderItemByOrderNumberAndSkuId(orderNumber, skuId));
    }

    @Override
    public ServerResponseEntity<Void> syncWeichatPromotionInfo(SyncWeichatPromotionInfoDTO syncWeichatPromotionInfoDTO) {
        orderService.syncWeichatPromotionInfo(syncWeichatPromotionInfoDTO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<CalculateDistributionCommissionResultVO> calculateDistributionCommissionByUserId(Long userId) {
        return ServerResponseEntity.success(orderService.calculateDistributionCommissionByUserId(userId));
    }

    @Override
    public ServerResponseEntity<List<Long>> getMatchedOrderIdList(OrderSearchDTO orderSearchDTO) {
        return ServerResponseEntity.success(orderService.getMatchedOrderIdList(orderSearchDTO));
    }

    @Override
    public ServerResponseEntity<Integer> checkIsOrderIntoShops(CheckOrderDTO checkOrderDTO) {
        return ServerResponseEntity.success(orderService.checkIsOrderIntoShops(checkOrderDTO));
    }

    @Override
    public ServerResponseEntity<Integer> createEcOrder(ShopCartOrderMergerVO shopCartOrderMergerVO) {
        orderService.createEcOrder(shopCartOrderMergerVO);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> ecOrderPay(Long outOrderId,String tentacleNo) {

        orderService.ecOrderPay(outOrderId,tentacleNo);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> ecCancelOrder(Long outOrderId) {
        orderService.ecCancelOrder(outOrderId);
        return ServerResponseEntity.success();
    }

    @Override
    public ServerResponseEntity<Void> ecOrderConfirm(Long outOrderId) {
        EsOrderBO esOrderBO = orderService.getEsOrderByWeChatOrderId(outOrderId);
        if(esOrderBO==null){
            Assert.faild("外部订单编号查询订单失败。");
        }
        orderService.receiptOrder(esOrderBO.getOrderId());
        return ServerResponseEntity.success();
    }

    /**
     * copy by OrderController.page
     * 提供给openApi，其余客服查询接口使用
     * */
    @Override
    public ServerResponseEntity<PageVO<EsOrderVO>> supportPage(OrderSearchDTO orderSearchDTO) {
        PageVO<EsOrderVO> orderPage = orderService.orderPage(orderSearchDTO);

        List<Long> storeidList = orderPage.getList().stream().filter(s -> s != null).map(EsOrderVO::getStoreId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<StoreVO>> storesResponse = storeFeignClient.listByStoreIdList(storeidList);
        Map<Long,StoreVO> storeMaps = new HashMap<>();
        if(storesResponse!=null && storesResponse.isSuccess() && storesResponse.getData().size()>0){
            storeMaps = storesResponse.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId,p->p));
        }


        /**
         * 查询用户列表
         */
        List<Long> useridList = orderPage.getList().stream().map(EsOrderVO::getUserId).distinct().collect(Collectors.toList());
        ServerResponseEntity<List<UserApiVO>> usersResponse = userFeignClient.getUserBypByUserIds(useridList);
        Map<Long, UserApiVO> userMaps = new HashMap<>();
        if (usersResponse != null && usersResponse.isSuccess() && usersResponse.getData().size() > 0) {
            userMaps = usersResponse.getData().stream().collect(Collectors.toMap(UserApiVO::getUserId, p -> p));
        }

        for (EsOrderVO esOrderVO : orderPage.getList()) {
            StoreVO storeVO = storeMaps.get(esOrderVO.getStoreId());
            if(storeVO!=null){
                esOrderVO.setStoreName(storeVO.getName());
            }
            //用户信息
            UserApiVO user = userMaps.get(esOrderVO.getUserId());
            if (user != null) {
                esOrderVO.setUserName(user.getNickName());
                esOrderVO.setUserMobile(user.getPhone());
//                esOrderVO.setUserNo(user.getVipcode());
            }
        }

        if (BooleanUtil.isFalse(permission)){
            for (EsOrderVO esOrderVO : orderPage.getList()) {
                esOrderVO.setMobile(PhoneUtil.hideBetween(esOrderVO.getMobile()).toString());
            }
        }
        return ServerResponseEntity.success(orderPage);
    }
}
